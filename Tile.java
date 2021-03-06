//import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Arrays;
import java.util.HashMap;
//import java.util.List;
import java.util.Random;
import java.util.Vector;

enum operatorss{

	UP,
	DOWN,
	LEFT,
	RIGHT

}

public class Tile {



	int tileMatrix[][];//game board
	int sizeOfTile;//board size
	Random rand;
	protected HashMap<Integer, Position> searchTree; // open state list
	protected Vector<Position> knownPositions;//close state list
	public static int counter;

	// ----------------------------- Constructors and initialize Methods ----------------------------- //

	public Tile() {

		this.sizeOfTile = 3; //This size of the tile matrix.
		Tile.counter = 0;  //Advanced each time that a new position was created.
		rand = new Random();
		searchTree = new HashMap<Integer, Position>();
		knownPositions = new Vector<Position>();
		initializeFirstPosition();

	}

	//This will initialize a random start position of the search tree.
	public void initializeFirstPosition() {

		int[][] tileFirstOrder = new int[this.sizeOfTile][this.sizeOfTile];
		tileFirstOrder = initializeTile();
		int[] operators = identifyOperators(tileFirstOrder);
		Position position = new Position(Tile.counter, tileFirstOrder, 0, operators,0);
		this.searchTree.put(position.getLocationInTree(), position);
		this.knownPositions.add(position);
		this.tileMatrix=tileFirstOrder;

	}

	//Create a tile, shuffle it and will transition into 2D matrix.
	protected int[][] initializeTile() {

		int[] tileOreder = {0,1,2,3,4,5,6,7,8};
		tileOreder = shuffleTileOrder(tileOreder);
		int[][] tileRandomOrder = turnArrayToMatrix(tileOreder);
		return tileRandomOrder;
		//TODO check if this is a solved position - code below
	}

	//Shuffle a tile.
	public int[] shuffleTileOrder(int[] array) {

		for (int i = 0; i < array.length; i++) {
			int randomIndexToSwap = rand.nextInt(array.length);
			int temp = array[randomIndexToSwap];
			array[randomIndexToSwap] = array[i];
			array[i] = temp;
		}
		return array;
	}

	//Turn a 1D to 2D matrix.
	public int[][] turnArrayToMatrix(int[] tile){

		int[][] tileRandomOrder = new int[this.sizeOfTile][this.sizeOfTile];
		int k = 0;

		for(int i = 0 ; i < this.sizeOfTile ; i++) {

			for(int j = 0 ; j < this.sizeOfTile ; j++) {

				tileRandomOrder[i][j] = tile[k];
				k++;

			}

		}

		return tileRandomOrder;
	}

	// -------------------------------------------------------------------------------------------------------------- //

	// ----------------------------- Methods for potential operator in current position ----------------------------- //

	// ------------------------------------------------------------------------------------------------------------- //

	// ----------------------------- Main expand method ------------------------------------------------------------ //

	protected void expand(Position previousPosition) {

		int [] operators = previousPosition.getOperators(); // Get the operators array.
		int [][] matrix = new int[previousPosition.getState().length][];
		for(int i = 0; i < previousPosition.getState().length; i++)
			matrix[i] = previousPosition.getState()[i].clone();
		int[] zeroLocation = identifyZeroLocation(matrix);

		for(int i = 0 ; i < operators.length ; i++) { //Start loop over the operators.

			int operator = i;

			if(operators[i] != 0) { // If the operator is available;

				int [][] swapedMatrix;
				int [][] temp;
				temp=swapTileInMatrix(matrix, operator, zeroLocation);
				swapedMatrix = temp.clone(); //Perform the swap and get the new matrix.
				//boolean knownMatrix = checkIfWeKnowThisMatrix(swapedMatrix);
				//if(knownMatrix == false) { //We dont know this matrix

				Tile.counter++;
				int[] newOperators = identifyOperators(swapedMatrix);
				Position position = new Position(Tile.counter, swapedMatrix, 0, newOperators,previousPosition.getLocationInTree());
				this.searchTree.put(Tile.counter, position); // A potential Bug !!! TODO - Fix this bug.
				this.knownPositions.add(previousPosition);


			}

		}

	}

	// ---------------------------------------------------------------------------------------------------------------- //

	// ----------------------------- Matrix methods ------------------------------------------------------------------- //

	protected int[][] swapTileInMatrix(int[][] matrix, int swapType, int[] zeroLocation){
		//The order of the operators - operators[0] - Up, operators[1] - Down, operators[2] - Right, operators[3] - Left.
		int [][] swapedMatrix = new int[matrix.length][];
		for(int i = 0; i < matrix.length; i++)
			swapedMatrix[i] = matrix[i].clone();
		int i = zeroLocation[0];
		int j = zeroLocation[1];

		if(swapType == 0) { //The zero is located down and will transition up.

			swapedMatrix[i][j] = swapedMatrix[i-1][j]; //Shift the number up.
			swapedMatrix[i-1][j] = 0; //Update the zero location.

		}

		if(swapType == 1) { //The zero is located up and will transition down.

			swapedMatrix[i][j] = swapedMatrix[i+1][j]; //Shift the number up.
			swapedMatrix[i+1][j] = 0; //Update the zero location.

		}

		if(swapType == 2) { //The zero is located left and will transition right.

			swapedMatrix[i][j] = swapedMatrix[i][j+1]; //Shift the number up.
			swapedMatrix[i][j+1] = 0; //Update the zero location.

		}

		if(swapType == 3) { //The zero is located right and will transition left.

			swapedMatrix[i][j] = swapedMatrix[i][j-1]; //Shift the number up.
			swapedMatrix[i][j-1] = 0; //Update the zero location.

		}

		return swapedMatrix;

	}

	protected boolean checkIfWeKnowThisMatrix(int[][] matrixToCheck) {

		for(int i = 0 ; i < this.knownPositions.size() ; i++) {

			int[][] knownMatrix = knownPositions.get(i).getState();

			if(checkMatrixForMatrix(matrixToCheck, knownMatrix)) {

				return true;

			}

		}

		return false;

	}

	protected boolean checkMatrixForMatrix(int[][] matrix, int[][] matrixToCheck) {

		for(int i = 0; i < matrix.length ; i++) {

			for(int j = 0; j < matrix.length ; j++) {

				if(matrix[i][j] != matrixToCheck[i][j]) {

					return true;

				}

			}

		}

		return false;

	}



	// ----------------------------- Methods to identify if we arrived at the final state ----------------------------- //

	public boolean identifyEndState(int[][] matrixToCheck) {

		int[][] finalTile = {{0,1,2},{3,4,5},{6,7,8}};

		for(int i = 0 ; i < this.sizeOfTile ; i++) {

			for(int j = 0 ; j < this.sizeOfTile ; j++) {

				if(matrixToCheck[i][j] != finalTile[i][j]) {

					return false;

				}

			}

		}

		return true;

	}

	//The order of the operators - operators[0] - Up, operators[1] - Down, operators[2] - Right, operators[3] - Left.
	public int[] identifyOperators(int[][] tileMatrix) {
		//matrix:
		//  0 1 2
		//0
		//1
		//2

		int[] zeroPosition = identifyZeroLocation(tileMatrix);
		int zeroRowPosition = zeroPosition[0];
		int zeroColPosition = zeroPosition[1];
		int[] operators = new int[4];

		if(zeroRowPosition == 0) {
			operators[0] = 1;
		}

		if(zeroRowPosition > 0 && zeroRowPosition < 2) {

			operators[0] = 1;
			operators[1] = 1;

		}

		if(zeroRowPosition == 2) {
			operators[0] = 1;
		}

		if(zeroColPosition == 0) {
			operators[2] = 1;
		}

		if(zeroColPosition > 0 && zeroColPosition < 2) {

			operators[2] = 1;
			operators[3] = 1;

		}

		if(zeroColPosition == 2) {
			operators[3] = 1;
		}

		return operators;

	}

	public int[] identifyZeroLocation(int[][] tileMatrix){

		int[] position = {0,0};

		for(int i = 0; i < tileMatrix.length; i++) {

			for(int j = 0; j < tileMatrix.length; j++) {

				if (tileMatrix[i][j] == 0) {

					position[0] = i;
					position[1] = j;
					break;

				}
			}

		}

		return position;

	}

	public String toString(){
		String s= "";
		for (int[] row : tileMatrix)

			// converting each row as string
			// and then printing in a separate line
			s+=(Arrays.toString(row))+"\n";

		return s;
	}



}

// Java program to check if a given
// instance of 8 puzzle is solvable or not
//class GFG
//{
//
//	// A utility function to count
//// inversions in given array 'arr[]'
//	static int getInvCount(int[][] arr)
//	{
//		int inv_count = 0;
//		for (int i = 0; i < 3 - 1; i++)
//			for (int j = i + 1; j < 3; j++)
//
//				// Value 0 is used for empty space
//				if (arr[j][i] > 0 &&
//						arr[j][i] > arr[i][j])
//					inv_count++;
//		return inv_count;
//	}
//
//	// This function returns true
//// if given 8 puzzle is solvable.
//	static boolean isSolvable(int[][] puzzle)
//	{
//		// Count inversions in given 8 puzzle
//		int invCount = getInvCount(puzzle);
//
//		// return true if inversion count is even.
//		return (invCount % 2 == 0);
//	}
//
//	/* Driver code */
//	public static void main (String[] args)
//	{
//		int[][] puzzle = {{1, 8, 2},{0, 4, 3},{7, 6, 5}};
//		if(isSolvable(puzzle))
//			System.out.println("Solvable");
//		else
//			System.out.println("Not Solvable");
//	}
//}
//
//// This code is contributed by chandan_jnu
//
