import org.w3c.dom.ls.LSOutput;

import java.util.*;

public class solver {

    private static TreeMap<Double,Position> addNewPositions(TreeMap<Double,Position> open, Collection<Position> positions ){
    for(Position p:positions){
        double cost=p.getCost();
        while(open.containsKey(cost)){
            cost+=0.000001;
        }
        open.put(cost,p);
    }
    return open;
    }

    public static Position Astar(Tile tile){
        TreeMap<Double,Position> sortOpen=new TreeMap<>();
        sortOpen= solver.addNewPositions(sortOpen,tile.searchTree.values());
        while(!tile.identifyGoalState(tile.getCurrentState().state)){
            Position pos = null;
           pos = sortOpen.pollFirstEntry().getValue();
           // System.out.println(pos);

            if(!tile.checkIfWeKnowThisMatrix(pos.state)){
               tile.expand(pos);
                sortOpen= solver.addNewPositions(sortOpen,tile.getKNextPositions());

              // System.out.println(pos.locationInTree);
           }

        }
        System.out.println(tile.getCurrentState());

        return tile.getCurrentState();
    }
    public static Position branchNbaund(Tile tile){
        double UB=Double.POSITIVE_INFINITY;
        double LB=0;
        int it=0;
        Position last=tile.getCurrentState();
        LB=last.cost;

        //while current position != goal
        while(!tile.finished()) {
            if (UB!=Double.POSITIVE_INFINITY)
                System.out.println("location in tree "+tile.getCurrentState().locationInTree);
//            System.out.println("tree search size: "+tile.searchTree.size()+ " close positions: "+tile.numClose());
            it++;
            //find the lowest next by heuristic
            Position pos = null;
            double cost = Double.POSITIVE_INFINITY;


//            //loop all next position and get the lower cost if not been there
            for (Position p : tile.getKNextPositions()) {
                if (p.hCost < cost &&!tile.checkIfWeKnowThisMatrix(p.state)) {
                    pos = p;
                    cost = p.hCost;
                }
                else if (tile.checkIfWeKnowThisMatrix(p.state))
                    p.close=true;
            }

            //check if LB < UB
            if (pos != null) {
                //LB<UB: update LB. expend position - LB+the heuristic cost (admissible h)
                if (LB + pos.hCost < UB) {
                    tile.expand(pos);
                    LB=tile.getCurrentState().cost;
                }
                //LB>UB
                else if (LB + pos.hCost >= UB) {
                    pos.close=true;
                    if(pos.locationInTree==1){
                        System.out.println("search tree head");
                        break;
                    }
                    else{
                        //back to previous position
                        tile.getPrevious();//update tile to previous position
                        LB=tile.getCurrentState().cost;//update lb to current state on tile- g dost
                        tile.setNextPosition(tile.getPrevious());//update next positions
                        //   System.out.println("up to previous "+tile.getPrevious().locationInTree);
                    }


                }
            } else if (pos == null) {
                if(tile.getCurrentState().locationInTree==1){
                    System.out.println("search tree head");
                    break;
                }
                //back to previous position
                tile.getPrevious();//update tile to previous position
                LB=tile.getCurrentState().cost;//update lb to current state on tile- g dost
                //   System.out.println("up to previous "+tile.getPrevious().locationInTree);


            }

            if (tile.identifyGoalState(tile.getCurrentState().state)) {
                UB = tile.getCurrentState().cost;
                System.out.println(tile.getCurrentState());
                System.out.println("UB: " + UB+"\n LB: "+LB);
                last=tile.getCurrentState();
            }
        }

        System.out.println("iterations: "+it);
        return last;
    }
}

//    public static Position branchNbaund(Tile tile){
//        double UB=Double.POSITIVE_INFINITY;
//        double LB=0;
//        double it=0;
//        Position last=tile.getCurrentState();
//
//        //we want that LB < UB
//        //LB will update by sum all costs of the new assignment if we do BT - we lower the LB
//        //UB update when we reach to goal
//
//        //while current position != goal
//        while(!tile.finished()) {
//            //tile.getCurrentState().locationInTree!=1
//            //    System.out.println(it);
//            it++;
//            Position pos = null;
//            double cost = Double.POSITIVE_INFINITY;
//
//
////            //loop all next position and get the lower cost if not been there
//            for (Position p : tile.getKNextPositions()) {
//
//                if (p.hCost < cost && !tile.checkIfWeKnowThisMatrix(p.state)) {
//                    pos = p;
//                    cost = p.getCost();
//                }
//            }
//            //check if LB <UB
//            if (pos != null) {
//                //LB<UB: update LB. expend position
//                if (LB + 1 < UB) {
//                    LB += 1;
//                    tile.expand(pos);
//
//                    // System.out.println("down from: "+pos.locationInTree+ " LB: "+LB+" UB: "+UB);
//                }
//                //LB>UB
//                else if (LB + (double) pos.getCost() >= UB) {
//                    //back to previous position
//                    tile.setCurrentState(tile.getPrevious());
//                    //update next positions
//                    tile.setNextPosition(tile.getPrevious());
//                    System.out.println("LB "+LB);
//
//                }
//            } else if (pos == null) {
//                //back to previous position
//                tile.setCurrentState(tile.getPrevious());
//                //update next positions
//                tile.setNextPosition(tile.getPrevious());
//                LB-=1;
//                System.out.println("up to previous "+tile.getPrevious().locationInTree);
//
//
//            }
//
//            if (tile.identifyGoalState(tile.getCurrentState().state)) {
//                UB = tile.getCurrentState().getCost();
//                System.out.println(tile.getCurrentState());
//                System.out.println("UB: " + UB);
//                last=tile.getCurrentState();
//
//            }
//        }
//
//
//        return last;
//    }
//}

