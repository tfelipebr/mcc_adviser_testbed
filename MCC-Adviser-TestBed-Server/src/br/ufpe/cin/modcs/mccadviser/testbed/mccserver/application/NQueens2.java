package br.ufpe.cin.modcs.mccadviser.testbed.mccserver.application;

import java.lang.reflect.Method;

public class NQueens2 
{

	private static final long	serialVersionUID = 5687713591581731140L;
	private static final String TAG = "NQueens";
	private static int 				N = 8;
	private int					nrClones;
	//private transient ExecutionController controller;

	/**
	 * @param controller	The execution controller taking care of the execution
	 * @param nrClones		In case of remote execution specify the number of clones needed
	 */
	/*
	public NQueens(ExecutionController controller, int nrClones) {
		this.controller = controller;
		this.nrClones	= nrClones;
	}
	
	*/
	
	/**
	 * @param controller	The execution controller taking care of the execution
	 * @param nrClones		In case of remote execution specify the number of clones needed
	 */
	/*
	
	public NQueens(ExecutionController controller) {
		this.controller = controller;
	}
	
	*/
	/**
	 * Solve the N-queens problem
	 * @param N	The number of queens
	 * @return The number of solutions found
	 */
	/*
	public int solveNQueens(int N)
	{
		this.N = N;
		Method toExecute;
		Class<?>[] paramTypes = {int.class};
		Object[] paramValues = {N};

		int result = 0;
		try {
			toExecute = this.getClass().getDeclaredMethod("localSolveNQueens", paramTypes);
			result = (Integer) controller.execute(toExecute, paramValues, this);
		} catch (SecurityException e) {
			// Should never get here
			e.printStackTrace();
			throw e;
		} catch (NoSuchMethodException e) {
			// Should never get here
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	 */

	//@Remote
	public static int localSolveNQueens(int N) {

		byte[][] board = new byte[N][N];
		int countSolutions = 0;

		// cloneId == -1 if this is the main clone
		// or [0, nrClones-2] otherwise
		//int cloneId 		= ControlMessages.readCloneId();
		int howManyRows 	= N;				// Integer division, we may loose some rows. 
		int start 			= 0; 		// Add 1 since cloneId == -1 if this is the main clone 
		int end 			= start + howManyRows;
		
		// If this is the clone with the highest id let him take care
		// of the rows not considered due to the integer division. 
		
		System.out.println("Finding solutions for " + N + "-queens puzzle.");
		System.out.println("Analyzing rows: " + start + "-" +(end-1));
		
		for (int i = start; i < end; i++) {
			for (int j = 0; j < N; j++) {
				for (int k = 0; k < N; k++) {
					for (int l = 0; l < N; l++) {
						if (N == 4) {
							countSolutions += setAndCheckBoard(board, new int[]{i, j, k, l});
							continue;
						}
						for (int m = 0; m < N; m++) {
							if (N == 5) {
								countSolutions += setAndCheckBoard(board, new int[]{i, j, k, l, m});
								continue;
							}
							for (int n = 0; n < N; n++) {
								if (N == 6) {
									countSolutions += setAndCheckBoard(board, new int[]{i, j, k, l, m, n});
									continue;
								}
								for (int o = 0; o < N; o++) {
									if (N == 7) {
										countSolutions += setAndCheckBoard(board, new int[]{i, j, k, l, m, n, o});
										continue;
									}
									for (int p = 0; p < N; p++) {
											countSolutions += setAndCheckBoard(board, new int[]{i, j, k, l, m, n, o, p});
									}
								}
							}
						}
					}
				}
			}
		}

		System.out.println("Found " + countSolutions + " solutions.");
		return countSolutions;
	}
	
	/**
	 * When having more than one clone running the method there will be partial results
	 * which should be combined to get the total result.
	 * This will be done automatically by the main clone by calling
	 * this method.
	 * @param params Array of partial results.
	 * @return The total result.
	 */
	public int localSolveNQueensReduce(int[] params) {
		int solutions = 0;
		for (int i = 0; i < params.length; i++) {
			System.out.println("Adding " + params[i] + " partial solutions.");
			solutions += params[i];
		}
		return solutions;
	}
	
	private static int setAndCheckBoard(byte[][] board, int... cols) {
		
		clearBoard(board);
		
		for (int i = 0; i < N; i++)
			board[i][cols[i]] = 1;
		
		if (isSolution(board)) return 1;
		
		return 0;
	}

	private static void clearBoard(byte[][] board) {
		for (int i = 0; i < N; i ++) {
			for (int j = 0; j < N; j++) {
				board[i][j] = 0;
			}
		}
	}

	private static boolean isSolution(byte[][] board) {

		int rowSum = 0;
		int colSum = 0;

		for (int i = 0; i < N; i++) {
			for (int j = 0;  j < N; j++) {
				rowSum += board[i][j];
				colSum += board[j][i];

				if (i == 0 || j == 0)
					if ( !checkDiagonal1(board, i, j) ) return false;

				if (i == 0 || j == N-1)
					if ( !checkDiagonal2(board, i, j) ) return false;

			}
			if (rowSum > 1 || colSum > 1) return false;
			rowSum = 0;
			colSum = 0;
		}

		return true;
	}

	private static boolean checkDiagonal1(byte[][] board, int row, int col) {
		int sum = 0;
		int i = row;
		int j = col;
		while (i < N && j < N) {
			sum += board[i][j];
			i++;
			j++;
		}
		return sum <= 1;
	}

	private static boolean checkDiagonal2(byte[][] board, int row, int col) {
		int sum = 0;
		int i = row;
		int j = col;
		while (i < N && j >=0) {
			sum += board[i][j];
			i++;
			j--;
		}
		return sum <= 1;
	}


	private static void printBoard(byte[][] board) 
	{
		for (int i = 0; i < N; i++) 
		{
			StringBuilder row = new StringBuilder();
			
			for (int j = 0;  j < N; j++) 
			{
				row.append(board[i][j]);
				if (j < N - 1)
					row.append(" ");
			}
			
			System.out.println(row.toString());
		}
		
		System.out.print("\n");
	}

	public void setNumberOfClones(int nrClones) 
	{
		this.nrClones = nrClones;
	}
	
	public static void main(String[] args)
	{
		System.out.println("Executando o NQueens2");
		
		byte[][] board = new byte[N][N];
		
		long iniTime = System.currentTimeMillis();
		
		localSolveNQueens(10);
		
		System.out.println("Tempo total = " + (System.currentTimeMillis() - iniTime));
	}
	
	/*
	@Override
	public void copyState(Remoteable state) {

	}
	
	*/
}
