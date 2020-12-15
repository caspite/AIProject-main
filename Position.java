import java.util.Arrays;

public class Position {
	
	int locationInTree;
	int[][] state;
	int cost; // the known cost - g()
	int hCost;// the heuristic cost
	int operators[];
	int previousPosition;

	
	// ----------------------------- Constructors and initialize Methods ----------------------------- // 

	public Position(int locationInTree, int[][] state, int cost, int[] operators,int previousPosition) {
		super();
		this.locationInTree = locationInTree;
		this.state = state;
		this.cost = cost; 
		this.operators = operators;
		setHcost(state);
		this.previousPosition=previousPosition;
	}
	
	// ------------------------------------------------------------------- // 

	// ----------------------------- Getters ----------------------------- // 
	
	public int[][] getState() {
		return state;
	}
	
	public int getLocationInTree() {
		return locationInTree;
	}
	
	public int[] getOperators() {
		
		return operators;
		
	}
	
	// ------------------------------------------------------------------- // 

	// ----------------------------- Setters ----------------------------- // 
	
	public void setLocationInTree(int locationInTree) {
		this.locationInTree = locationInTree;
	}
		
	public void setState(int[][] state) {
		this.state = state;
	}

	private void setHcost(int[][]state){
		hCost=Heuristic.distanceOneNum(state,0);


	}

	
	// ------------------------------------------------------------------- // 

	public String toString() {

			String s= "";
			for (int[] row : state)

				// converting each row as string
				// and then printing in a separate line
				s+=(Arrays.toString(row))+"\n";

			return s;



		//return "g cost: "+cost+" h cost: "+hCost +" location in tree: "+locationInTree;
	}
	
}
