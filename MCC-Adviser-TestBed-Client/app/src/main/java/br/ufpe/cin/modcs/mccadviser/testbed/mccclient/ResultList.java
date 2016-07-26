package br.ufpe.cin.modcs.mccadviser.testbed.mccclient;

import java.util.ArrayList;
import java.util.List;

public class ResultList
{
    private List<Result> results;

    public ResultList()
    {
        results = new ArrayList<Result>();
    }

    public void addResult(Result result)
    {
        results.add(result);
    }

    public String getResults()
    {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < results.size(); i++)
        {
            sb.append(i);
            sb.append(";");

            sb.append(results.get(i));
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public String toString()
    {
        return getResults();
    }
}
