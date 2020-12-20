

public class Main {
    public static void main(String[] args){

    Tile tile = new Tile();
        System.out.println(tile.searchTree.get(0));
        tile.expand(tile.searchTree.get(0));
        System.out.println(tile.searchTree.get(1));

        System.out.println("\nstart B&B");

        solver.branchNbaund(tile);




    }
}
