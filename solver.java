import org.w3c.dom.ls.LSOutput;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class solver {

    private static TreeMap<Double, Position> addNewPositions(TreeMap<Double, Position> open, Collection<Position> positions) {
        for (Position p : positions) {
            double cost = p.getCost();
            while (open.containsKey(cost)) {
                cost += 0.000001;
            }
            open.put(cost, p);
        }
        return open;
    }

    public static Position Astar(Tile tile) {
        TreeMap<Double, Position> sortOpen = new TreeMap<>();
        sortOpen = solver.addNewPositions(sortOpen, tile.searchTree.values());
        while (!tile.identifyGoalState(tile.getCurrentState().state)) {
            Position pos = null;
            pos = sortOpen.pollFirstEntry().getValue();
            // System.out.println(pos);

            if (!tile.checkIfWeKnowThisMatrix(pos.state)) {
                tile.expand(pos);
                sortOpen = solver.addNewPositions(sortOpen, tile.getKNextPositions());

                // System.out.println(pos.locationInTree);
            }

        }
        System.out.println(tile.getCurrentState());

        return tile.getCurrentState();
    }

    public static Position branchNbaund(Tile tile) {
        double UB =Double.POSITIVE_INFINITY;
        double LB = 0;
        int it = 0;
        Position last = tile.getCurrentState();
        LB = last.cost;
        Vector<Position> knowStates = new Vector<Position>();
        Vector<Position> pathToGoal = new Vector<Position>();
        HashMap<Integer[][],Integer> knownPath=new HashMap<>();//the know state to solution + their costs


        //while search tree have open positions
        while (!tile.finished()) {
            knowStates.add(tile.getCurrentState());//add current state to known positions
            it++;//update iteration

            //find the lowest next position by heuristic
            Position pos = null;
            double cost = Double.POSITIVE_INFINITY;

            //loop all next position and get the lower cost if not been there
            for (Position p : tile.getKNextPositions()) {
                boolean flag=false;
                if(pathToGoal.contains(p)) {
                    for(Position p1:pathToGoal){
                        if(p1.equals(p)&&p1.cost<p.cost){
                            flag = true;
                            break;
                        }
                    }

                }
                boolean visitInstate =solver.visitInState(p, knowStates);
                    if (p.hCost <= cost && !visitInstate&&!flag) {
                        pos = p;
                        cost = p.hCost;
                    }
                    if (visitInstate||flag) {
                        p.close = true;
                    }


            }

            //check if LB < UB
            if (pos != null) {
                LB = pos.cost;//update lb to current state on tile- g cost

                //LB<UB: update LB. expend position - LB+the heuristic cost (admissible h)
                if (LB+pos.hCost < UB) {
                    tile.expand(pos);
                //    System.out.println("expand: " + pos.locationInTree);

                }
                //LB>UB
                else if (LB+pos.hCost >= UB) {
                    if (pos.locationInTree == 1) {
                  //      System.out.println("search tree head");
                        break;
                    } else {
                        //back to previous position
                        tile.getPrevious();//update tile to previous position
                        LB = tile.getCurrentState().cost;//update lb to current state on tile- g dost
                        pos.close = true;
                    }


                }
            } else if (pos == null) {
                if (tile.getCurrentState().locationInTree == 1) {
                    System.out.println("search tree head");
                    break;
                }
                //back to previous position
                tile.getPrevious();//update tile to previous position
                LB = tile.getCurrentState().cost;//update lb to current state on tile- g dost
             //   System.out.println("up to previous " + tile.getCurrentState().locationInTree + "search tree size: " + tile.searchTree.size());

            }

            if (tile.identifyGoalState(tile.getCurrentState().state)) {
                UB = tile.getCurrentState().cost;
                System.out.println(tile.getCurrentState());
                System.out.println("UB: " + UB + "\n LB: " + LB);
                last = tile.getCurrentState();
//                tile.setCurrentState(tile.searchTree.get(0));
//                tile.setNextPosition(tile.searchTree.get(0));
                tile.getPrevious();
                LB = tile.getCurrentState().cost;//update lb to current state on tile- g dost
                knowStates.clear();
               // solver.updatepathToGoal(last,pathToGoal);
                solver.updatepathToGoalHash(last,knownPath);



            }
        }

        System.out.println("iterations: " + it);
        return last;
    }

    private static boolean visitInState(Position position, Vector<Position> knowState) {
        for (Position p : knowState) {

            if (p.equals(position)) {
                return true;
            }
        }
        return false;
    }
    private static Vector<Position> updatepathToGoal(Position p,Vector<Position> path){
       //loop on search tree to find all solution path
            while (p.locationInTree>0){
                //search the state in the path
                for(int i =0;i<path.size();i++) {      //if find- remove position
                    if (p.equals(path.get(i))) {
                        path.remove(i);
                    }
                }
                    path.add(p); //add to path

                if(p.previousPosition==0){
                    break;
                }
                    p=p.previousPosition1;
            }

        //return path
        return path;
    }


    private static HashMap<Integer[][],Integer> updatepathToGoalHash(Position p,HashMap<Integer[][],Integer> path){
        //loop on search tree to find all solution path
        while (p.locationInTree>0){
            //search the state in the path
           if(path.get(p.state)!=null){
               path.remove(p.state);
           }
            int[][] twoDimenArray = p.state;
            Integer[][] result = Stream.of(twoDimenArray)
                    .map(array -> IntStream.of(array).boxed().toArray(Integer[]::new))
                    .toArray(Integer[][]::new);
            path.put(result,p.cost); //add to path

            if(p.previousPosition==0){
                break;
            }
            p=p.previousPosition1;
        }

        //return path
        return path;
    }

    public static Position branchNbaundBFS(Tile tile) {

        double UB = Double.POSITIVE_INFINITY;
        double LB = 0;
        int it = 0;
        Position last = tile.getCurrentState();
        LB = last.cost;
        Vector<Position> knowStates = new Vector<Position>();

        while (!tile.finished()) {
            it++;

            //find the lowest next by heuristic
            Position pos = null;
            double cost = Double.POSITIVE_INFINITY;

//            //loop all  open positions and get the lower cost if not been there
            for (Position p : tile.searchTree.values()) {
                if (p.hCost < cost && !solver.visitInState(p, knowStates) && !p.close) {
                    pos = p;
                    cost = p.getCost();
                }
                if (solver.visitInState(p, knowStates)) {
                    p.close = true;
                }

            }

            //check if LB < UB
            if (pos != null) {
                //LB<UB: update LB. expend position - LB+the heuristic cost (admissible h)
                LB = pos.getCost();
                if (LB < UB) {
                    tile.expand(pos);
                    System.out.println("expand: " + pos.locationInTree);
                    System.out.println(tile.getCurrentState());

                }
                //LB>UB
                else if (LB >= UB) {
                    pos.close = true;
                    if (pos.locationInTree == 1) {
                        System.out.println("search tree head");
                        break;
                    } else {
                        //back to previous position
                        tile.getPrevious();//update tile to previous position
                        LB = tile.getCurrentState().cost;//update lb to current state on tile- g dost
                    }


                }
            } else if (pos == null) {
                if (tile.getCurrentState().locationInTree == 1) {
                    System.out.println("search tree head");
                    break;
                }
                //back to previous position
                tile.getPrevious();//update tile to previous position
                LB = tile.getCurrentState().cost;//update lb to current state on tile- g dost
                System.out.println("up to previous " + tile.getCurrentState().locationInTree + "search tree size: " + tile.searchTree.size());

            }

            if (tile.identifyGoalState(tile.getCurrentState().state)) {
                UB = tile.getCurrentState().cost;
                System.out.println(tile.getCurrentState());
                System.out.println("UB: " + UB + "\n LB: " + LB);
                last = tile.getCurrentState();
//                tile.setCurrentState(tile.searchTree.get(0));
//                tile.setNextPosition(tile.searchTree.get(0));
                tile.getPrevious();
                LB = tile.getCurrentState().cost;//update lb to current state on tile- g dost
                knowStates.clear();
            }
        }

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

