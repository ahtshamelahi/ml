public class MatrixThread implements Runnable
{

	private int row;
	private int[][] sourceM;
	private int[][] destM;

	public MatrixThread(int[][] sourceM, int[][] destM, int row )
	{
		this.sourceM = sourceM;
		this.destM = destM;	
		this.row = row;
	}
	
	public void run()
	{
		for ( int col = 0; col < sourceM.length; ++col)   //Its multiplyinh a single row
			for ( int temp1 = 0; temp1 < sourceM.length; ++temp1)
					destM[row][col] += sourceM[row][temp1]*sourceM[temp1][col];
	}
}
