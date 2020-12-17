public class solver {
    int [][] goal;

    public static void Astar(Tile tile,int[][] goal){
    goal=goal;

    }
    public static void branchNbaund(Tile tile,int[][] goal){
        double UB=Double.POSITIVE_INFINITY;
        double LB=0;
        double currentCost=0;
        goal=goal;

        //while current position != goal
        //get tile open list
        //open the next position in tree- one bellow the current position
        //check if current cost + the new position's cost is more UB
        //if true - go to the next position -one bellow the current position
            //if their is no next position - chang the curr position to the first position in the open list
        //if false - current position = next // update bounds // current cost and expend the position

    }
}
