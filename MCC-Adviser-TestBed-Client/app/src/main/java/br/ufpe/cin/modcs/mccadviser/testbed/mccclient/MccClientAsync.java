package br.ufpe.cin.modcs.mccadviser.testbed.mccclient;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.configuration.Server;

public class MccClientAsync extends AsyncTask<Void, Void, Void>
{
    MccClientAsync(Context context, Server server, String methodName, TextView textResponse)
    {
    }

    @Override
    protected Void doInBackground(Void... arg0)
    {
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
    }
}
