import java.util.HashMap;

public class Heuristic {

    public static int distanceOneNum(int[][]state,int num){
        int h=0;

        //find num place
        for(int i=0;i<state.length;i++){
            for(int j=0;j<state[i].length;j++){
                if(state[i][j]==num){
                    h=Heuristic.getDistance(num,i,j);
                    return h;
                }
            }
        }
        return h;
    }

    public static int penalty(int[][]state){
        int h=0;
        int h1=0;
        //find num place
        for(int i=0;i<state.length;i++){
            for(int j=0;j<state[i].length;j++){
                if(Heuristic.getDistance(state[i][j],i,j)>0){
                    if(Heuristic.getDistance(state[i][j],i,j)<h1)
                        h1=Heuristic.getDistance(state[i][j],i,j);
                    h+=1;
                }
            }
        }
        return h;

    }

    public static int calcManhattanDistanceCost(int[][] state) {

        int manhattanDistanceCost = 0;

        int[][] goalState = {{1,2,3}, {4,5,6}, {7,8,0}};

        for(int i = 1; i < 9 ; i++) { //From each number starting from 1 until 8.

            int[] locationOfNumberInCurrent = getLocationOfNumber(state, i); //Get location of the number in the goal state.
            int[] locationOfNumberInGoalState = getLocationOfNumber(goalState, i); //Get the location of of the number in the state.
            int rowDistance = Math.abs(locationOfNumberInCurrent[0] - locationOfNumberInGoalState[0]); //Calculate the distance to i -> absulute value of state[i] - goal[i]
            int colDistance = Math.abs(locationOfNumberInCurrent[1] - locationOfNumberInGoalState[1]); //Calculate the distance to j -> absulute value of state[j] - goal[j]
            manhattanDistanceCost = manhattanDistanceCost + rowDistance + colDistance; //add to total.

        }

        return manhattanDistanceCost;

    }

    public static int[] getLocationOfNumber(int[][] matrix, int numberToLook) {

        int[] numberToLookPosition = new int[2];

        for(int i = 0; i < matrix.length ; i++) {

            for(int j = 0; j < matrix.length ; j++) {

                if(matrix[i][j] == numberToLook) {

                    numberToLookPosition[0] = i;
                    numberToLookPosition[1] = j;

                }

            }

        }

        return numberToLookPosition;

    }

    private static int getDistance(int num,int row,int column){
        int distance=0;
        int[][] goal;
        goal= new int[][]{{0, 2, 2}, {1, 0, 0}, {2, 0, 1}, {3, 0, 2}, {4, 1, 0}, {5, 1, 1}, {6, 1, 2}, {7, 2, 0}, {8, 2, 1}};
        for(int i[]: goal){
            if (i[0]==num){
                distance=Math.abs((i[1]-row)+(i[2]-column));

            }
        }
        return distance;
    }
}
