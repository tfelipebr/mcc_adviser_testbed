package br.ufpe.cin.modcs.mccadviser.testbed.mccclient.configuration;

public enum Target
{
    CLOUD(1), DEVICE(2);

    private int value;

    Target(int value)
    {
        this.value = value;
    }

    public int getValue()
{
    return value;
}
}
