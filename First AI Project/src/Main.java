import java.util.Collections;
import java.util.Vector;

public class Main {

    public static void main(String[] args){

        int algorithm = 2; // 1 is for A star, 2 is for BnB.
        int numberOfRuns = 1;
        int j = 0;
        int bNbVersion=0;//0-non improvements, 1- loop improvement, 2- loop + path to goal improvements

        do {

            Tile tile = new Tile();
            tile.expand(tile.getCurrentState());

            int matrix[][] = tile.getCurrentState().getState();

            boolean isSolvable = checkIfTheTileIsSolvable(matrix);

            if (isSolvable) {

                j++;

                if (algorithm == 1) {

                    System.out.println("start A star"); //Will print that the A* was algorithm will run.
                    AStar aStar=new AStar(tile);
                    Position AGoal = aStar.solve(); //Will initialize the algorithm.
                    printSolution(AGoal);

                }

                if (algorithm == 2) {

                    System.out.println("start bNb");
                    BranchAndBound bNb =new BranchAndBound(tile);

                    Position bNbGoal = bNb.solve(bNbVersion);
                    printSolution(bNbGoal);

                }

            }

        } while (j < numberOfRuns) ;

        System.out.println("The run was ended");

    }


    static private void printSolution(Position p){
        int i = 0;
        Vector<Position>path=new Vector<>();

        while (p.locationInTree > 0) {
            path.add(p);

            if (p.previousPosition == null) {

                break;
            }

            p = p.previousPosition;

        }
        Collections.reverse(path);

        for(Position state:path){

            i++;
            System.out.println("State: "+ i);
            //+" location in tree:"+state.locationInTree + "."
            System.out.println(state);
        }

    }

    //*** if tile is solvable methods***//

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



}