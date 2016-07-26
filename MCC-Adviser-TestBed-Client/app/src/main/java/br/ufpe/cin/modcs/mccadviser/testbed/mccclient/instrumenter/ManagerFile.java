package br.ufpe.cin.modcs.mccadviser.testbed.mccclient.instrumenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * Class used to manage files inside the sdcard, such actions include for example to save the file inside the device.
 * @author airton
 *
 */
@SuppressLint("NewApi")
public class ManagerFile
{
    private static final String TAG = "ManageFile";

    private Context context;

    private boolean sdCardAvailable;
    private boolean sdCardWritableReadable;
    private boolean sdCardReadableOnly;

    private String logName;

    public ManagerFile(Context context)
    {
        this.context = context;
    }

    public ManagerFile(String logName)
    {
        this.logName = logName;
    }

    /**
     * Escreve no arquivo texto.
     * @param text Texto a ser escrito.
     * @return True se o texto foi escrito com sucesso.
     * @throws IOException
     */
    @SuppressLint("NewApi")
    public boolean writeFile(String text) throws IOException
    {
        File folder = new File(Environment.getExternalStorageDirectory(), "modcs_mcc");
        //File file = new File(Environment.getExternalStorageDirectory(), logName);
        File file = new File(folder, logName);

        if (!file.exists())
        {
            file.createNewFile();
        }

        try
        {
            FileOutputStream out = new FileOutputStream(file, true);
            out.write("\n".getBytes());
            out.write(text.getBytes());
            out.flush();
            out.close();

            return true;
        }
        catch (Exception e)
        {
            Log.e(TAG, e.toString());

            return false;
        }
    }

    public boolean clearFile(String filename)
    {
        File folder = new File(Environment.getExternalStorageDirectory(), "modcs_mcc");

        File file = new File(folder, filename);

        try
        {
            RandomAccessFile r = new RandomAccessFile(file, "rws");
            r.setLength(0);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Faz a leitura do arquivo
     * @return O texto lido.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String ReadFile() throws FileNotFoundException, IOException
    {
        File textfile = new File(context.getExternalFilesDir(null), "romar.txt");

        FileInputStream input = new FileInputStream(textfile);
        byte[] buffer = new byte[(int)textfile.length()];

        input.read(buffer);

        return new String(buffer);
    }

    public void getStateSDcard()
    {
        // Obtêm o status do cartão SD
        String status = Environment.getExternalStorageState();

        if (Environment.MEDIA_BAD_REMOVAL.equals(status))
        {
            // Midia foi removida antes de ser montada
            sdCardAvailable = false;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;

            Log.d(TAG, "Midia removida.");
        }
        else
        if (Environment.MEDIA_CHECKING.equals(status))
        {
            // Midia está presente e está sendo feita a verificação
            sdCardAvailable = true;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;

            Log.d(TAG, "Midia sendo verificada.");
        }
        else
        if (Environment.MEDIA_MOUNTED.equals(status))
        {
            // A midia está presente e montada neste momento com
            // permissão de escrita e leitura
            sdCardAvailable = true;
            sdCardWritableReadable = true;
            sdCardReadableOnly = false;

            Log.d(TAG, "Midia com permissão de escrita e leitura.");
        }
        else
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(status))
        {
            // A midia está presente e montada neste momento com
            // permissão somente de leitura
            sdCardAvailable = true;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;

            Log.d(TAG, "Midia com permissão somente leitura.");
        }
        else
        if (Environment.MEDIA_NOFS.equals(status))
        {
            // A midia está presente, mas está vazia ou utilizando um
            // sistema de arquivos não suportado
            sdCardAvailable = false;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;

            Log.d(TAG, "Midia com sistema de arquivos não compatível.");
        }
        else
        if (Environment.MEDIA_REMOVED.equals(status))
        {
            // A midia não está presente
            sdCardAvailable = false;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;

            Log.d(TAG, "Midia não presente.");
        }
        else
        if (Environment.MEDIA_SHARED.equals(status))
        {
            // A midia está presente, não montada e compartilhada
            // via USB
            sdCardAvailable = false;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;

            Log.d(TAG, "Midia compartilhada via USB.");
        }
        else
        if (Environment.MEDIA_UNMOUNTABLE.equals(status))
        {
            // A midia está presente mas não pode ser montada
            sdCardAvailable = false;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;

            Log.d(TAG, "Midia não pode ser montada");
        }
        else
        if (Environment.MEDIA_UNMOUNTED.equals(status))
        {
            // A midia está presente mas não montada
            sdCardAvailable = false;
            sdCardWritableReadable = false;
            sdCardReadableOnly = false;

            Log.d(TAG, "Midia não montada.");
        }
    }

    public boolean isSdCardAvailable()
    {
        return sdCardAvailable;
    }

    public void setSdCardAvailable(boolean sdCardAvailable)
    {
        this.sdCardAvailable = sdCardAvailable;
    }

    public boolean isSdCardWritableReadable()
    {
        return sdCardWritableReadable;
    }

    public void setSdCardWritableReadable(boolean sdCardWritableReadable)
    {
        this.sdCardWritableReadable = sdCardWritableReadable;
    }

    public boolean isSdCardReadableOnly()
    {
        return sdCardReadableOnly;
    }

    public void setSdCardReadableOnly(boolean sdCardReadableOnly)
    {
        this.sdCardReadableOnly = sdCardReadableOnly;
    }

}
