package br.ufpe.cin.modcs.mccadviser.testbed.mccclient.instrumenter;

import android.content.Context;

import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.Result;
import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.configuration.Target;
import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.utils.DateUtils;
import br.ufpe.cin.modcs.mccadviser.testbed.mccclient.utils.DeviceUtils;

public class TimeLogger
{
    private double initTime = 0;
    private double endTime = 0;
    private double executionTime = 0;

    private Result result;

    public TimeLogger()
    {
        result = new Result();
    }

    public void registerStart()
    {
        initTime = System.currentTimeMillis();
    }

    public Result registerFinishing(int iteration, int methodIndex, Target target, String methodCallName, Object methodReturn, Context context)
    {
        endTime = System.currentTimeMillis();

        executionTime = endTime - initTime;

        result.setIteration(iteration);
        result.setMethodIndex(methodIndex);
        result.setDeviceId(DeviceUtils.getDeviceId(context));
        result.setDeviceModel(DeviceUtils.getDeviceName());
        result.setTimestamp(DateUtils.getTimestamp());
        result.setTarget(target);
        result.setMethodName(methodCallName);
        result.setMethodReturn(methodReturn);
        result.setExecutionTime(executionTime);

        return result;
    }
}
