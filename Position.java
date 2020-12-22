import java.util.Arrays;

public class Position {
	
	int locationInTree;
	int[][] state;
	int cost; // the known cost - g()
	int hCost;// the heuristic cost
	int operators[];
	int previousPosition;
	boolean close; //if we visit in this position
	Position previousPosition1;//the previous position

	
	// ----------------------------- Constructors and initialize Methods ----------------------------- // 

	public Position(int locationInTree, int[][] state, int cost, int[] operators,int previousPosition) {
		super();
		this.locationInTree = locationInTree;
		this.state = state;
		this.cost = cost; 
		this.operators = operators;
		setHcost(state);
		this.previousPosition=previousPosition;
		close=false;
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
	public double getCost(){
		return (cost+hCost);
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
		hCost=Heuristic.penalty(state);


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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Position position = (Position) o;


			for (int i = 0; i < state.length; i++) {
				for (int j = 0; j < state[i].length; j++) {
					int b1 = state[i][j];
					int b2 = position.state[i][j];
					if (b1 != b2) return false;
				}
			}
			return true;
		}


	@Override
	public int hashCode() {
		return Arrays.hashCode(state);
	}
}
