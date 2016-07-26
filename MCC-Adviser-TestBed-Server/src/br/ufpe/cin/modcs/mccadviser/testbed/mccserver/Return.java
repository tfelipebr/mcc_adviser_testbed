package br.ufpe.cin.modcs.mccadviser.testbed.mccserver;

import java.io.Serializable;

public class Return implements Serializable
{
	private static final long serialVersionUID = 3682859355868122233L;
	
	private boolean error;
	private String errorMessage;
	private Object methodReturn;
	
	public Return() 
	{
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

	public Object getMethodReturn() 
	{
		return methodReturn;
	}

	public void setMethodReturn(Object methodReturn) 
	{
		this.methodReturn = methodReturn;
	}
}
