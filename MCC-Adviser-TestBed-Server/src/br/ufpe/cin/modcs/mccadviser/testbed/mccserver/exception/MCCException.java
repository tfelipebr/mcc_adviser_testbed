package br.ufpe.cin.modcs.mccadviser.testbed.mccserver.exception;

public class MCCException extends RuntimeException
{
	private static final long serialVersionUID = 5220088422804166599L;

	public MCCException(String message)
    {
        super(message);
    }
}