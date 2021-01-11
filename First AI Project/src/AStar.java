import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

public class AStar {
    TreeMap<Double, Position> sortedOpenList=new TreeMap<>();
    HashMap<Integer,Double> outPutAll=new HashMap<>();
    Vector<Position> closeList;
    Tile tile;
    int iteration =0;


    //***constructor***//
    public AStar(Tile tile){
        this.tile=tile;
        addNewPositions(tile.getKNextPositions());

    }


//*** solve methods***//


    private void addNewPositions(Collection<Position> positions) {
        for (Position p : positions) {
            double cost = p.getFCost();
            while (sortedOpenList.containsKey(cost)) {
                cost += 0.000001;
            }
            sortedOpenList.put(cost, p);
        }
    }

    public Position solve() {
        while (!tile.identifyGoalState(tile.getCurrentState().state)) {

            iteration++;
            Position pos = null;
            pos = sortedOpenList.pollFirstEntry().getValue();

            outPutAll.put(iteration,pos.getgCost());

            if (!tile.checkIfWeKnowThisMatrix(pos.state)) {
                tile.expand(pos);
                addNewPositions(tile.getKNextPositions());
                System.out.println("expend");
            }
            System.out.println(iteration);
            tile.addKnownPositions(pos);

        }
        WriteToFile.writeOutput("output.csv",outPutAll,"Astar",0);


        return tile.getCurrentState();
    }
}
