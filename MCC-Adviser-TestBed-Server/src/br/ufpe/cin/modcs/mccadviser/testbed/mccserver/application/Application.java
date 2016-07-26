package br.ufpe.cin.modcs.mccadviser.testbed.mccserver.application;

public class Application 
{
	public Application() 
	{
	}
	
	public static int m1() 
	{
		return NQueens2.localSolveNQueens(8);
	}
	
	public static int m2()
	{
		return NQueens2.localSolveNQueens(9);
	}	
	
	public static int m3()
	{
		return NQueens2.localSolveNQueens(8);
	}		
}