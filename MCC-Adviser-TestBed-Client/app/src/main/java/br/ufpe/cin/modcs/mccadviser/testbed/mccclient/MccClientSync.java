package br.ufpe.cin.modcs.mccadviser.testbed.mccclient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.configuration.Server;
import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.exception.MCCException;

public class MccClientSync
{
    private Server server;
    private Socket socket;
    private String response = "";

    boolean error;
    boolean keepConnection;


    MccClientSync(Server server, boolean keepConnection)
    {
        this.server = server;
        this.keepConnection = keepConnection;
    }

    public Object executeRemoteMethod(final String methodName) throws MCCException
    {
        final Semaphore s = new Semaphore(0);

        Thread t = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    if (!isConnected())
                        connect();

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                    byte[] buffer = new byte[1024];

                    int bytesRead;
                    InputStream inputStream = socket.getInputStream();

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                    out.println("call#"+methodName);
                    out.flush();

                    while(true)
                    {

                        response = in.readLine();

                        if (response.contains("ACK#WELCOME"))
                            continue;
                        else
                            break;
                    }

                    //
                    //Log.d(App.TAG, "response: " + response);
                    //System.out.println("response: " + response);

                    if (!keepConnection)
                    {
                        out.println("call#close");
                        out.flush();
                    }

                    /*
                    while ((bytesRead = inputStream.read(buffer)) != -1)
                    {
                        byteArrayOutputStream.write(buffer, 0, bytesRead);
                        response += byteArrayOutputStream.toString("UTF-8");

                        Log.d(App.TAG, "response: " + response);
                        System.out.println("response: " + response);
                    }
                    */
                }
                catch (UnknownHostException e)
                {
                    e.printStackTrace();
                    throw new MCCException("The server is unreachable");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    throw new MCCException("Communication error with the server");
                }
                finally
                {
                    if ((socket != null) && (!keepConnection))
                    {
                        try
                        {
                            socket.close();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

                s.release();
            }
        };
        t.start();

        try
        {
            s.acquire();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }

        if (error)
        {
            //Log.e(App.TAG, response);
            throw new MCCException(response);
        }

        return response;
    }

    private boolean isConnected()
    {
        if (socket == null)
            return false;

        if (socket.isConnected())
            return true;

        return false;
    }

    private void connect() throws MCCException
    {
        try
        {
            socket = new Socket(server.getAddress(), server.getPort());
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
            throw new MCCException("The server is unreachable");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new MCCException("Communication error with the server");
        }
    }

    private  void disconnect() throws MCCException
    {
        if (socket != null)
        {
            try
            {
                socket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                throw new MCCException("Error to close connection");
            }
        }
    }
}