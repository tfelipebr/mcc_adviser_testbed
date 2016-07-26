package br.ufpe.cin.modcs.mccadviser.testbed.mccclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.configuration.Configuration;
import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.configuration.Experiment;
import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.configuration.Method;
import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.configuration.Server;
import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.configuration.ServerFTP;
import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.configuration.Target;
import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.exception.MCCException;
import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.instrumenter.ManagerFile;
import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.instrumenter.TimeLogger;
import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.utils.DateUtils;
import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.utils.SimpleUtil;

public class MainActivity extends BaseActivity implements Status
{
    private TextView iterationTextView;
    private TextView logTextView;
    private Button buttonRun, buttonExit;

    private StringBuilder logBuilder = new StringBuilder();

    private Configuration config;
    private Experiment experiment;
    private ResultList results = new ResultList();

    private String fileName;
    private boolean saveLogFile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRun = (Button) findViewById(R.id.runButton);
        buttonExit = (Button) findViewById(R.id.exitButton);
        iterationTextView = (TextView) findViewById(R.id.iterationTextView);
        logTextView = (TextView) findViewById(R.id.logTextView);

        buttonRun.setOnClickListener(run());

        buttonExit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                System.exit(0);
            }
        });

        try
        {
            loadConfiguration();
            //Log.d(App.TAG, config.toString());
        }
        catch(Exception e)
        {
            showAlert("Error", e.getMessage(), 0);
        }
    }

    private void enableButtons(final boolean enabled)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                buttonRun.setEnabled(enabled);
                //buttonExit.setEnabled(enabled);
            }
        });
    }

    private View.OnClickListener run()
    {
        return new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                logTextView.setText("");
                logBuilder = new StringBuilder();

                enableButtons(false);

                Thread t = new Thread()
                {
                    public void run()
                    {
                        try
                        {
                            runExperiment2();
                        }
                        catch(final Exception e)
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    enableButtons(true);
                                    showAlert("Error", e.getMessage(), 0);
                                }
                            });
                        }
                    }
                };

                t.start();
            }
        };
    }

    private void runExperiment2() throws Exception
    {
        Object resultMethod = null;

        Server server = config.getServer();
        MccClientSync cli = new MccClientSync(server, false);

        updateStatus("Starting experiment...");

        List<Method> methodList = config.getMethods();

        for (int iteration = 1; iteration <= experiment.getIterationCount(); iteration++)
        {
            updateIteration("Executing iteration ".concat(String.valueOf(iteration).concat(" of ").concat(String.valueOf(experiment.getIterationCount()))));

            for (int methodIndex = 0; methodIndex < methodList.size(); methodIndex++)
            {
                Method m = methodList.get(methodIndex);

                // Reflection - Instance of Application
                String applicationClassName = "br.ufpe.cin.modcs.mccadviser.testbed.mccclient.application.Application";
                Class<?> applicationClass = Class.forName(applicationClassName);
                Object application = applicationClass.newInstance();
                //

                if (m.getTarget().equals(Target.DEVICE))
                {
                    resultMethod = null;

                    updateStatus("Executing method ".concat(m.getName()).concat("() in this target= ").concat(m.getTarget().toString()));

                    TimeLogger tl = new TimeLogger();

                    tl.registerStart();

                    java.lang.reflect.Method getNameMethod = application.getClass().getMethod(m.getName());
                    resultMethod = getNameMethod.invoke(application); // explicit cast

                    Result measurement = tl.registerFinishing(iteration, methodIndex, m.getTarget(), m.getName(), resultMethod, getContext());

                    results.addResult(measurement);
                    updateStatus(measurement.getResultFormatted());
                }
                else
                if (m.getTarget().equals(Target.CLOUD))
                {
                    updateStatus("Executing method ".concat(m.getName()).concat("() in this target= ").concat(m.getTarget().toString()));

                    Result measurement = null;
                    TimeLogger timeLogger = new TimeLogger();

                    try
                    {
                        resultMethod = null;

                        timeLogger.registerStart();
                        resultMethod = (Object) cli.executeRemoteMethod("EXEC#".concat(m.getName()));
                        measurement = timeLogger.registerFinishing(iteration, methodIndex, m.getTarget(), m.getName(), resultMethod, getContext());

                        int indexError = resultMethod.toString().indexOf("ERROR");

                        results.addResult(measurement);
                        updateStatus(measurement.getResultFormatted());

                        if (indexError >= 0)
                          throw new Exception(resultMethod.toString());
                    }
                    catch(Exception e)
                    {
                        measurement = timeLogger.registerFinishing(iteration, methodIndex, m.getTarget(), m.getName(), resultMethod, getContext());
                        measurement.setError(true);
                        measurement.setErrorMessage(e.getMessage());

                        results.addResult(measurement);

                        updateStatus("Exception while trying to run ".concat(m.getName()).concat(" on the cloud").concat(e.getMessage()));
                        updateStatus("Running the method in device");

                        resultMethod = null;

                        timeLogger = new TimeLogger();

                        timeLogger.registerStart();

                        java.lang.reflect.Method getNameMethod = application.getClass().getMethod(m.getName());
                        resultMethod = getNameMethod.invoke(application); // explicit cast

                        measurement = timeLogger.registerFinishing(iteration, methodIndex, m.getTarget(), m.getName(), resultMethod, getContext());

                        results.addResult(measurement);
                        updateStatus(measurement.getResultFormatted());
                    }
                }
            }

            Thread.sleep(experiment.getDelayBetweenIteration());
        }

        try
        {
            fileName = "log-exec-time.csv";
            //fileName = "logTextView-exec-time_".concat(DateUtils.getTimestamp().concat(DeviceUtils.getDeviceId(getContext()))).concat(".csv");

            ManagerFile mf = new ManagerFile(fileName);
            saveLogFile = mf.writeFile(results.toString());
        }
        catch(IOException e)
        {
            e.printStackTrace();

            updateStatus("Error opening file for writing.");
        }
        finally
        {
            enableButtons(true);

            updateStatus("Finished!");

            if (saveLogFile && config.getFtp().isTransferLogToFTP())
                sendLogToFTP();
        }
    }

    private void sendLogToFTP()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                questionDialog(new CallbackAnswerDialog()
                {
                    @Override
                    public void positiveAnswer()
                    {
                        new TransferLogTask().execute();
                    }

                    @Override
                    public void negativeAnswer()
                    {
                    }
                }, "Do you want to send the log file to the FTP server?");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_clear)
        {
            questionDialog(new CallbackAnswerDialog()
            {
                @Override
                public void positiveAnswer()
                {
                    if (new ManagerFile(getContext()).clearFile("log-exec-time.csv"))
                        Toast.makeText(MainActivity.this, "Log file was erased", Toast.LENGTH_SHORT);
                    else
                        Toast.makeText(MainActivity.this, "Error! Log file wasn't erased", Toast.LENGTH_SHORT);
                }

                @Override
                public void negativeAnswer() {
                }
            }, "Do you want to clear the log file?");
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadConfiguration() throws Exception
    {
        createDirIfNotExists("modcs_mcc");

        config =  readConfigurationFile();
        experiment = config.getExperiment();
    }

    private static boolean createDirIfNotExists(String dirName) throws Exception
    {
        boolean ret = true;

        File file = new File(Environment.getExternalStorageDirectory(), dirName);

        if (!file.exists())
        {
            if (!file.mkdirs())
            {
                Log.e(App.TAG, "Error to create folder.");

                throw new Exception("Error! Unable to create the folder.");
            }
        }

        return ret;
    }

    private Configuration readConfigurationFile() throws Exception
    {
        Configuration config = null;

        createDirIfNotExists("modcs_mcc");

        File folder = new File(Environment.getExternalStorageDirectory(), "modcs_mcc");

        File xmlFile = new File(folder, "config.xml");
        if (!xmlFile.exists())
        {
            Experiment experiment = new Experiment(1, 0);

            Server mccServer = new Server("192.168.0.12", 1314);
            ServerFTP ftp = new ServerFTP("108.167.188.205", 21, "modcs@hotelbahialtda.com.br", "1314s$", true);

            config = new Configuration(experiment, mccServer, ftp);

            config.addMethod(new Method("m1", Target.CLOUD));
            config.addMethod(new Method("m2", Target.DEVICE));
            config.addMethod(new Method("m3", Target.CLOUD));

            boolean save = SimpleUtil.toXML(config, xmlFile);

            /*PrintWriter out = null;

            try
            {
                out = new PrintWriter(xmlFile);
                out.println(xml);
            }
            finally
            {
                out.close();
            }*/

            showAlert("Error", "Config.xml wasn't found! The file was recreated.", 0);
        }
        else
        {
            try
            {
                return SimpleUtil.fromXml(xmlFile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                throw new Exception(e.getMessage());
            }
        }

        return config;
    }

    protected void showAlert(String title, String message, final int fieldId)
    {
        new AlertDialog.Builder(getContext()).setTitle(title).setMessage(message
        ).setNeutralButton("Close",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which > 0)
                            getActivity().findViewById(fieldId).requestFocus();
                    }

                }).show();
    }

    private void questionDialog(final CallbackAnswerDialog cad, final String question)
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (cad != null) cad.positiveAnswer();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        if (cad != null) cad.negativeAnswer();
                        break;
                }
            }
        };

        new AlertDialog.Builder(getActivity()).setMessage(question).setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).create().show();
    }

    private interface CallbackAnswerDialog
    {
        void positiveAnswer();
        void negativeAnswer();
    }

    protected Context getContext()
    {
        return this;
    }

    protected Activity getActivity()
    {
        return this;
    }

    @Override
    public void updateStatus(String s)
    {
        logBuilder.append(s.concat("\n"));

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                logTextView.setText(logBuilder.toString());
            }
        });
    }

    public void updateIteration(final String iteration)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                iterationTextView.setText(iteration);
            }
        });
    }

    private class TransferLogTask extends AsyncTask<String, Void, Boolean>
    {
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        protected void onPreExecute()
        {
            this.dialog.setMessage("Sending log to FTP Server...");
            this.dialog.show();
        }

        protected Boolean doInBackground(final String... args)
        {
            File logFile = new File(new File(Environment.getExternalStorageDirectory(), "modcs_mcc"), fileName);

            //File file = new File(exportDir, logFile.getName());

            try
            {
                FTPClient con = null;

                try
                {
                    con = new FTPClient();

                    ServerFTP ftp = config.getFtp();

                    con.connect(ftp.getAddress());

                    if (con.login(ftp.getUsername(), ftp.getPassword()))
                    {
                        con.enterLocalPassiveMode(); // important!
                        con.setFileType(FTP.BINARY_FILE_TYPE);

                        FileInputStream in = new FileInputStream(logFile);
                        boolean result = con.storeFile(DateUtils.getTimestamp().concat("_".concat(fileName)), in);

                        in.close();

                        if (result)
                            Log.d(App.TAG, "The transfer of file was completed successfully");
                        else
                            Log.d(App.TAG, "Ftp error");

                        con.logout();
                        con.disconnect();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return true;
            }
            catch (Exception e)
            {
                Log.e(App.TAG, e.getMessage(), e);

                throw new MCCException("FTP Error");
            }
        }

        protected void onPostExecute(final Boolean success)
        {
            if (this.dialog.isShowing())
                this.dialog.dismiss();

            if (success)
                Toast.makeText(MainActivity.this, "The transfer was completed successfully!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainActivity.this, "FTP Error", Toast.LENGTH_SHORT).show();
        }

        void copyFile(File src, File dst) throws IOException
        {
            FileChannel inChannel = new FileInputStream(src).getChannel();
            FileChannel outChannel = new FileOutputStream(dst).getChannel();

            try
            {
                inChannel.transferTo(0, inChannel.size(), outChannel);
            }
            finally
            {
                if (inChannel != null)
                    inChannel.close();

                if (outChannel != null)
                    outChannel.close();
            }
        }
    }
}