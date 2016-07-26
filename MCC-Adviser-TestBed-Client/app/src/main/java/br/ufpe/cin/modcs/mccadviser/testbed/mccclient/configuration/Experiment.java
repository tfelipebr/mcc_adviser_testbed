package br.ufpe.cin.modcs.mccadviser.testbed.mccclient.configuration;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Element
public class Experiment
{
    @Attribute
    private int iterationCount;

    @Attribute(required = false)
    private long delayBetweenIteration;

    public Experiment()
    {}

    public Experiment(int iterationCount, long delayBetweenIteration)
    {
        this.iterationCount = iterationCount;
        this.delayBetweenIteration = delayBetweenIteration;
    }

    public int getIterationCount()
    {
        return iterationCount;
    }

    public void setIterationCount(int iterationCount)
    {
        this.iterationCount = iterationCount;
    }

    public long getDelayBetweenIteration()
    {
        return delayBetweenIteration;
    }

    public void setDelayBetweenIteration(long delayBetweenIteration)
    {
        this.delayBetweenIteration = delayBetweenIteration;
    }

    @Override
    public String toString()
    {
        return "iteration=".concat(String.valueOf(getIterationCount())).concat(";delay=").concat(String.valueOf(getDelayBetweenIteration()));
    }
}