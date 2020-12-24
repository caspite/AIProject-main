public class Main {

    public static void main(String[] args){

        int algorithm = 2; // 1 is for A star, 2 is for BnB.
        int numberOfRuns = 1;
        int j = 0;

        do {

            Tile tile = new Tile();

            //System.out.println("The initial position that was randomly selected:");
            System.out.println(tile);
            tile.expand(tile.searchTree.get(0));

            int matrix[][] = tile.getCurrentState().getState();

            boolean isSolvable = solver.checkIfTheTileIsSolvable(matrix);

            if (isSolvable) {

                j++;
                //System.out.println("The tile chosen for the run(" + j + "):" + isSolvable + ".\n");

                if (algorithm == 1) {

                    //System.out.println("start A star"); //Will print that the A* was algorithm will run.

                    Position goal = solver.Astar(tile); //Will initialize the algorithm.

                    Position p = goal; //This is the solution of the final position.

                    int i = 0;

                    while (p.locationInTree > 0) {

                        i++;

                        System.out.println("i: "+ i +" location in tree:"+p.locationInTree + ".");

                        System.out.println(p);

                        if (p.previousPosition == 0) {

                            break;
                        }

                        p = p.previousPosition1;

                    }

                }

                if (algorithm == 2) {

                    System.out.println("start bNb");
                    Position bNb = solver.branchNbaund(tile, j);
                    Position p = bNb; //This is the solution of the final position.

                    int i = 0;

                    while (p.locationInTree > 0) {

                        i++;

                        System.out.println("i: " + i + " location in tree:" + p.locationInTree + ".");

                        System.out.println(p);

                        if (p.previousPosition == 0) {

                            break;
                        }

                        p = p.previousPosition1;

                    }

                }

            }

        } while (j < numberOfRuns) ;

        System.out.println("The run was ended");

    }



}