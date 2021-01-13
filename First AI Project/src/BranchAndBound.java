import java.util.HashMap;
import java.util.Vector;

public class BranchAndBound{

    Tile tile;
    double ub =Double.POSITIVE_INFINITY;
    double lb = 0;
    int iteration = 0;
    Position last;
    Vector<Position> knowStates = new Vector<Position>();
    Vector<Position> pathToGoal = new Vector<Position>();
    HashMap<Integer,Double> outPut=new HashMap<>();
    HashMap<Integer,Double> outPutAll=new HashMap<>();
    int lb1=0;
    int version;

    //***output Variables***//
    int atomicCount;
    int atomicCounterFactor=1000;

    //***constructor***//

    public BranchAndBound(Tile tile){
        this.tile=tile;
        this.last=tile.getCurrentState();
        lb=last.gCost;

    }

    //*** solve methods***//

    public Position solve(int version) {
        this.version=version;//0-non improvements, 1- loop improvement, 2- loop + path to goal improvements


        //while search tree have open positions
        while (!(tile.finished()||lb1>6)) {
            lb=tile.getCurrentState().gCost;
            updateLb1();
            printCurrentPosition();
            outPut.put(iteration,lb);

            tile.getCurrentState().close=true;
            iteration++;//update iteration
            knowStates.add(tile.getCurrentState());//add current state to known positions

            //find the lowest next position by heuristic & version
            Position nextPosition=FinedNextPositionToExpand();

            boolean finished = goToNextPosition(nextPosition);
            if (finished)
                break;
            identifyGoalState();
        }

        System.out.println("iterations: " + iteration);
        WriteToFile.writeOutput("output.csv",outPutAll,"B&B",version);
        return last;
    }

    private boolean visitInState(Position position) {
        increaseAtomicCounter();
        if(knowStates.contains(position))
            return true;
        return false;
    }

    private void  updatePathToGoal(Position p){
        //loop on search tree to find all solution path

        while (p.previousPosition!=null){
            //search the state in the path
            increaseAtomicCounter();

            for(int i =0;i<pathToGoal.size();i++) {//if find- remove position
                increaseAtomicCounter();

                if (p.equals(pathToGoal.get(i))) {
                    pathToGoal.remove(i);
                }
            }
            pathToGoal.add(p); //add to path

            p=p.previousPosition;
        }
    }

    private void updateLb1(){
        increaseAtomicCounter();
        if(lb==1){
            lb1++;
        }
    }


    private Position FinedNextPositionToExpand(){
        Position nextPosition = null;
        double cost = Double.POSITIVE_INFINITY;
        boolean flag=false;

        switch (version){
            case 0:
                //loop all next position and get the lower cost
                for (Position p : tile.getKNextPositions()) {
                    increaseAtomicCounter();
                    if (p.hCost <= cost) {
                        nextPosition = p;
                        cost = p.hCost;
                    }
                }
                break;
            case 1:
                //loop all next position and get the lower cost if not been there
                for (Position p : tile.getKNextPositions()) {

                    boolean visitInstate =visitInState(p);
                    increaseAtomicCounter();
                    if (p.hCost <= cost&&!visitInstate) {
                        nextPosition = p;
                        cost = p.hCost;
                    }
                    if (visitInstate) {
                        p.close = true;
                    }
                }
                break;


            case 2:
                //loop all next position and get the lower cost if not been there + not better in previous
                for (Position p : tile.getKNextPositions()) {
                    flag = lowerInPathToGoal(p);
                    if(flag)
                        continue;
                    boolean visitInstate =visitInState(p);
                    increaseAtomicCounter();
                    if (p.hCost <= cost && !visitInstate) {
                        nextPosition = p;
                        cost = p.hCost;
                    }

                    if (visitInstate||flag) {
                        p.close = true;
                    }

                }
                break;

        }
        return nextPosition;
    }

    private boolean lowerInPathToGoal(Position newPosition) {
        increaseAtomicCounter();
        if (pathToGoal.contains(newPosition)) {
            int index = pathToGoal.indexOf(newPosition);
            Position knownPosition = pathToGoal.get(index);
            increaseAtomicCounter();
            if (knownPosition.getFCost() < newPosition.getFCost()) {
                newPosition.close = true;
                return true;
            }
        }
        return false;
    }


    private boolean goToNextPosition(Position nextPosition){
        increaseAtomicCounter();
        if (nextPosition != null) {
            lb = nextPosition.getFCost();//update lb to f cost
            //check if LB < UB
            //LB<UB: update LB. expend position - LB+the heuristic cost (admissible h)
            increaseAtomicCounter();
            if (lb<ub) {
                tile.expand(nextPosition);
            }
            //LB>UB
            else if (lb>=ub) {
                if (nextPosition.previousPosition == null) {
                    System.out.println("Finished!!");
                    return true;
                } else {
                    //back to previous position
                    tile.updateToPreviousPosition();//update tile to previous position
                    knowStates.clear();//clear loop save list
                    nextPosition.close = true;
                }

            }
        } else if (nextPosition == null) {
            increaseAtomicCounter();

            if (tile.getCurrentState().previousPosition == null) {
                increaseAtomicCounter();

                System.out.println("Finished!!");
                return true;
            } else {

                //back to previous position
                tile.updateToPreviousPosition();//update tile to previous position
                knowStates.clear();
            }
        }
        return false;
    }

   private void identifyGoalState(){

       if (tile.identifyGoalState(tile.getCurrentState().state)) {
           ub = tile.getCurrentState().gCost;
           System.out.println("UB: " + ub );
           last = tile.getCurrentState();
           tile.updateToPreviousPosition();
           knowStates.clear();
           outPutAll.putAll(outPut);
           if(version==2){
               updatePathToGoal(last);
           }

       }

   }

   //*** output methods ***//
   public void increaseAtomicCounter(){
       atomicCount+=1;
       if(atomicCount%atomicCounterFactor==0){
           outPut.put(atomicCount/atomicCounterFactor,lb);
           System.out.println("NCLO: "+atomicCount/atomicCounterFactor+ "Cost: "+lb);

       }

   }

   //*** print & dGub***//

   private void  printCurrentPosition(){
       if(lb==1){
           System.out.println("iteration: "+iteration+ " CurrentState "+tile.getCurrentState().getLocationInTree());
           System.out.println(tile.getCurrentState());
       }
   }





}
