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
    double currentCost=0;

    //***output Variables***//
    int atomicCount;
    int atomicCounterFactor=1000;


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
                increaseAtomicCounter();
                cost += 0.000001;
            }
            sortedOpenList.put(cost, p);
        }
    }

    public Position solve() {
        while (!tile.identifyGoalState(tile.getCurrentState().state)) {
            currentCost=tile.getCurrentState().getgCost();
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

    //*** output methods ***//
    public void increaseAtomicCounter(){
        atomicCount+=1;
        if(atomicCount%atomicCounterFactor==0){
            outPutAll.put(atomicCount/atomicCounterFactor,currentCost);
            System.out.println("NCLO: "+atomicCount/atomicCounterFactor+ "Cost: "+currentCost);
        }

    }
}
