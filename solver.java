import org.w3c.dom.ls.LSOutput;

import java.util.Random;

public class solver {

    public static void Astar(Tile tile){

    }
    public static Position branchNbaund(Tile tile){
        double UB=Double.POSITIVE_INFINITY;
        double LB=0;
        double it=0;

        //we want that LB < UB
        //LB will update by sum all costs of the new assignment if we do BT - we lower the LB
        //UB update when we reach to goal

        //while current position != goal
        while(tile.getCurrentState().locationInTree!=1) {
            //tile.getCurrentState().locationInTree!=1
            //    System.out.println(it);
            it++;
            Position pos = null;
            double cost = Double.POSITIVE_INFINITY;


//            //loop all next position and get the lower cost if not been there
            for (Position p : tile.getKNextPositions()) {

                if (p.hCost < cost && !tile.checkIfWeKnowThisMatrix(p.state)) {
                    pos = p;
                    cost = p.getCost();
                }
            }
            //check if LB <UB
            if (pos != null) {
                //LB<UB: update LB. expend position
                if (LB + 1 < UB) {
                    LB += 1;
                    tile.expand(pos);
                    // System.out.println("down from: "+pos.locationInTree+ " LB: "+LB+" UB: "+UB);
                }
                //LB>UB
                else if (LB + (double) pos.getCost() >= UB) {
                    //back to previous position
                    tile.setCurrentState(tile.getPrevious());
                    //update next positions
                    tile.setNextPosition(tile.getPrevious());
                     System.out.println("LB "+LB);

                }
            } else if (pos == null) {
                //back to previous position
                tile.setCurrentState(tile.getPrevious());
                //update next positions
                tile.setNextPosition(tile.getPrevious());
          //      System.out.println("up");

            }

            if (tile.identifyGoalState(tile.getCurrentState().state)) {
                UB = tile.getCurrentState().getCost();
                System.out.println(tile.getCurrentState());
                System.out.println("UB: " + UB);
                tile.setCurrentState(tile.searchTree.get(2));
            }
        }


        return tile.getCurrentState();
    }
}
