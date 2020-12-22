public class Main {

    public static void main(String[] args){

        int algorithm = 2; // 1 is for A star, 2 is for BnB.

        Tile tile = new Tile();

        System.out.println(tile.searchTree.get(0));
        tile.expand(tile.searchTree.get(0));
        System.out.println(tile.searchTree.get(1));


        if(algorithm ==1) {

            System.out.println("start A star");

            Position goal = solver.Astar(tile);
            Position p=goal;
            int i=0;
            while (p.locationInTree>0){
                i++;
                System.out.println("i: "+ i +" location in tree: "+p.locationInTree);
                System.out.println(p);
                if(p.previousPosition==0){
                    break;
                }

                p=tile.getPrevious();
            }

        }

        if(algorithm ==2) {

            System.out.println("start bNb");
            Position bNb = solver.branchNbaund(tile);

        }
        if(algorithm ==3) {

            System.out.println("start bNbBFS");
            Position bNb = solver.branchNbaundBFS(tile);

        }

    }
}