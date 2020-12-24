import org.w3c.dom.ls.LSOutput;

import java.util.*;

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
        int it=0;
        HashMap<Integer,Double> outPutAll=new HashMap<>();
        TreeMap<Double, Position> sortOpen = new TreeMap<>();
        sortOpen = solver.addNewPositions(sortOpen, tile.getKNextPositions());
        while (!tile.identifyGoalState(tile.getCurrentState().state)) {
            it++;
            // System.out.println(pos);

            Position pos = null;
            pos = sortOpen.pollFirstEntry().getValue();
            outPutAll.put(it,pos.getCost());

            if (!tile.checkIfWeKnowThisMatrix(pos.state)) {
                tile.expand(pos);
                sortOpen = solver.addNewPositions(sortOpen, tile.getKNextPositions());

                // System.out.println(pos.locationInTree);
            }

        }
        System.out.println(tile.getCurrentState());
        int i=0;
        WriteToFile.writeOutput("output.csv",outPutAll,"Astar",i);


        return tile.getCurrentState();
    }

    public static Position branchNbaund(Tile tile,int i) {
        double UB =Double.POSITIVE_INFINITY;
        double LB = 0;
        int it = 0;
        Position last = tile.getCurrentState();
        LB = last.cost;
        Vector<Position> knowStates = new Vector<Position>();
        Vector<Position> pathToGoal = new Vector<Position>();
        HashMap<Integer,Double> outPut=new HashMap<>();
        HashMap<Integer,Double> outPutAll=new HashMap<>();
        int LB1=0;



        //while search tree have open positions
        while (LB1<6) {
            if(LB==1){
                LB1++;
            }
            tile.getCurrentState().close=true;
            System.out.println("iteration: "+it+ " LB: "+LB);
            it++;//update iteration

            if(UB==Double.POSITIVE_INFINITY)
                outPut.put(it,LB);
            else
                outPut.put(it,LB);


            knowStates.add(tile.getCurrentState());//add current state to known positions

            //find the lowest next position by heuristic
            Position pos = null;
            double cost = Double.POSITIVE_INFINITY;

            //loop all next position and get the lower cost if not been there
            for (Position p : tile.getKNextPositions()) {
                boolean flag=false;
                if(pathToGoal.contains(p)) {
                   int index= pathToGoal.indexOf(p);
                   Position p1=pathToGoal.get(index);
                        if(p1.cost<p.cost){
                            flag = true;
                            p.close = true;
                            continue;
                        }

                }
                boolean visitInstate =solver.visitInState(p, knowStates);
                if (p.hCost < cost&& !visitInstate) {
//
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
                if (LB+pos.hCost< UB) {
                    //(LB+pos.hCost < UB)
                    tile.expand(pos);
                    //    System.out.println("expand: " + pos.locationInTree);
                }
                //LB>UB
                else if (LB+pos.hCost>= UB) {
                    if (pos.locationInTree == 1) {
                        //      System.out.println("search tree head");
                        break;
                    } else {
                        //back to previous position
                        tile.getPrevious();//update tile to previous position
                        LB = tile.getCurrentState().cost;//update lb to current state on tile- g dost
                        knowStates.clear();
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
                knowStates.clear();
                //   System.out.println("up to previous " + tile.getCurrentState().locationInTree + "search tree size: " + tile.searchTree.size());

            }

            if (tile.identifyGoalState(tile.getCurrentState().state)) {
                UB = tile.getCurrentState().cost;
              //  System.out.println(tile.getCurrentState());
                System.out.println("UB: " + UB );

                last = tile.getCurrentState();
              //  tile.setCurrentState(tile.searchTree.get(0));
              //  tile.setNextPosition(tile.searchTree.get(0));
                tile.getPrevious();
                LB = tile.getCurrentState().cost;//update lb to current state on tile- g dost
                knowStates.clear();
                outPutAll.putAll(outPut);
                solver.updatepathToGoal(last,pathToGoal);

            }
        }

        System.out.println("iterations: " + it);
        WriteToFile.writeOutput("output.csv",outPutAll,"B&B",i);
        return last;
    }

    private static boolean visitInState(Position position, Vector<Position> knowState) {
        if(knowState.contains(position))
            return true;
//        for (Position p : knowState) {
//
//            if (p.equals(position)) {
//                return true;
//            }
//        }
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



//    public static Position branchNbaundBFS(Tile tile) {
//
//        double UB = Double.POSITIVE_INFINITY;
//        double LB = 0;
//        int it = 0;
//        Position last = tile.getCurrentState();
//        LB = last.cost;
//        Vector<Position> knowStates = new Vector<Position>();
//
//        while (!tile.finished()) {
//            it++;
//
//            //find the lowest next by heuristic
//            Position pos = null;
//            double cost = Double.POSITIVE_INFINITY;
//
////            //loop all  open positions and get the lower cost if not been there
//            for (Position p : tile.searchTree.values()) {
//                if (p.hCost < cost && !solver.visitInState(p, knowStates) && !p.close) {
//                    pos = p;
//                    cost = p.getCost();
//                }
//                if (solver.visitInState(p, knowStates)) {
//                    p.close = true;
//                }
//
//            }
//
//            //check if LB < UB
//            if (pos != null) {
//                //LB<UB: update LB. expend position - LB+the heuristic cost (admissible h)
//                LB = pos.getCost();
//                if (LB < UB) {
//                    tile.expand(pos);
//                    System.out.println("expand: " + pos.locationInTree);
//                    System.out.println(tile.getCurrentState());
//
//                }
//                //LB>UB
//                else if (LB >= UB) {
//                    pos.close = true;
//                    if (pos.locationInTree == 1) {
//                        System.out.println("search tree head");
//                        break;
//                    } else {
//                        //back to previous position
//                        tile.getPrevious();//update tile to previous position
//                        LB = tile.getCurrentState().cost;//update lb to current state on tile- g dost
//                    }
//
//
//                }
//            } else if (pos == null) {
//                if (tile.getCurrentState().locationInTree == 1) {
//                    System.out.println("search tree head");
//                    break;
//                }
//                //back to previous position
//                tile.getPrevious();//update tile to previous position
//                LB = tile.getCurrentState().cost;//update lb to current state on tile- g dost
//                System.out.println("up to previous " + tile.getCurrentState().locationInTree + "search tree size: " + tile.searchTree.size());
//
//            }
//
//            if (tile.identifyGoalState(tile.getCurrentState().state)) {
//                UB = tile.getCurrentState().cost;
//                System.out.println(tile.getCurrentState());
//                System.out.println("UB: " + UB + "\n LB: " + LB);
//                last = tile.getCurrentState();
////                tile.setCurrentState(tile.searchTree.get(0));
////                tile.setNextPosition(tile.searchTree.get(0));
//                tile.getPrevious();
//                LB = tile.getCurrentState().cost;//update lb to current state on tile- g dost
//                knowStates.clear();
//            }
//        }
//
//        return last;
//    }

    // ----------------------------- Check is Solvable method ---------------------------------------- //

    public static boolean checkIfTheTileIsSolvable(int state[][]) {

        int[] stateOneD = trun2Dinto1D(state);
        swithZeroPosition(stateOneD);

        int inversionsCount = 0;

        for(int i = 0 ; i < stateOneD.length ; i++) {

            for (int j = i + 1 ; j < stateOneD.length ; j++) {

                if(stateOneD[i] > 0 && stateOneD[j] > 0) {

                    if(stateOneD[i] > stateOneD[j]) {

                        inversionsCount++;

                    }

                }

            }

        }

        if(inversionsCount % 2 == 0) {

            return true;

        }

        return false;

    }

    private static int[] trun2Dinto1D(int matrix[][]) {

        int size = matrix.length;

        int[] oneArray = new int[size*size-1];

        int k = 0;

        for(int i = 0; i < size ; i++) {

            for(int j = 0 ; j < size ; j++) {

                if(matrix[i][j] > 0) {

                    oneArray[k] = matrix[i][j];
                    k++;

                }
            }

        }

        return oneArray;

    }

    private static void swithZeroPosition(int[] array) {

        for(int i = 0 ; i < array.length ; i++) {

            if (array[i] == 0) {

                array[i] = array[array.length-1];
                array[array.length-1] = 0;

            }

        }

    }

    //----------------------------------------------------------------------------------------//
}
