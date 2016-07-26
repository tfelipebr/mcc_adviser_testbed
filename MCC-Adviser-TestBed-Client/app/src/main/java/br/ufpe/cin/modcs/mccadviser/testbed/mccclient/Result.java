package br.ufpe.cin.modcs.mccadviser.testbed.mccclient;

import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.configuration.Target;

public class Result
{
    private int iteration;
    private int methodIndex;

    private String deviceId;
    private String deviceModel;
    private String timestamp;
    private Target target;
    private String methodName;
    private Object methodReturn;
    private double executionTime;
    private String unitTime = "ms";
    private boolean error;
    private String errorMessage;

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(getTimestamp());
        sb.append(";");
        sb.append(getDeviceId());
        sb.append(";");
        sb.append(getDeviceModel());
        sb.append(";");
        sb.append(getIteration());
        sb.append(";");
        sb.append(getMethodIndex());
        sb.append(";");
        sb.append(getMethodName());
        sb.append("();");
        sb.append(getTarget());
        sb.append(";");
        sb.append(getExecutionTime());
        sb.append(";");
        sb.append(getUnitTime());
        sb.append(";");
        sb.append(getMethodReturn());
        sb.append(";");
        sb.append(isError());
        sb.append(";");
        sb.append(getErrorMessage());

        return sb.toString();
    }

    public String getResultFormatted()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(getIteration());
        sb.append("-");
        sb.append(getMethodIndex());
        sb.append("     ");
        sb.append(getMethodName());
        sb.append("()   ");
        sb.append(getTarget());
        sb.append("  ");
        sb.append(getExecutionTime());
        sb.append("");
        sb.append(getUnitTime());
        sb.append("  ");
        sb.append(getMethodReturn());
        sb.append("  ");
        sb.append(isError());
        sb.append("  ");
        sb.append(getErrorMessage() == null ? "" : getErrorMessage());

        return sb.toString();
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public Target getTarget()
    {
        return target;
    }

    public void setTarget(Target target)
    {
        this.target = target;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }

    public Object getMethodReturn()
    {
        return methodReturn;
    }

    public void setMethodReturn(Object methodReturn)
    {
        this.methodReturn = methodReturn;
    }

    public double getExecutionTime()
    {
        return executionTime;
    }

    public void setExecutionTime(double executionTime)
    {
        this.executionTime = executionTime;
    }

    public String getUnitTime()
    {
        return unitTime;
    }

    public void setUnitTime(String unitTime)
    {
        this.unitTime = unitTime;
    }

    public String getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getDeviceModel()
    {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel)
    {
        this.deviceModel = deviceModel;
    }
    public boolean isError()
    {
        return error;
    }

    public void setError(boolean error)
    {
        this.error = error;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public int getIteration()
    {
        return iteration;
    }

    public void setIteration(int iteration)
    {
        this.iteration = iteration;
    }

    public int getMethodIndex()
    {
        return methodIndex;
    }

    public void setMethodIndex(int methodIndex)
    {
        this.methodIndex = methodIndex;
    }
}