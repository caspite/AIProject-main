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
    public static int calcPenalty(int[][] state) {

        int penalty = 0;

        int[][] goalState = {{1,2,3}, {4,5,6}, {7,8,0}};

        for(int i = 0 ; i < state.length ; i++) {

            for(int j = 0 ; j < state.length; j++) {

                if(state[i][j] != goalState[i][j]) {

                    penalty+=1;

                }

            }

        }
//        if(penalty>3)
//            penalty*=3;


        return penalty;

    }

    public static int calManhattanInPhase(int[][]state){
        int h=0;

        //the heuristic will check which is lead to order first the metrix to:
        //1 x x
        //4 x x
        //7 8 x
        //while we in this position - the solution is very close, and will give penalty of 1 to each non position number
        int[][] goalState = {{1,2,3}, {4,5,6}, {7,8,0}};

        for(int i = 1; i < 9 ; i++) { //From each number starting from 1 until 8.
            if(i==1||i==4||i==7||i==8){
                int[] locationOfNumberInCurrent = getLocationOfNumber(state, i); //Get location of the number in the goal state.
                int[] locationOfNumberInGoalState = getLocationOfNumber(goalState, i); //Get the location of of the number in the state.
                int rowDistance = Math.abs(locationOfNumberInCurrent[0] - locationOfNumberInGoalState[0]); //Calculate the distance to i -> absulute value of state[i] - goal[i]
                int colDistance = Math.abs(locationOfNumberInCurrent[1] - locationOfNumberInGoalState[1]); //Calculate the distance to j -> absulute value of state[j] - goal[j]
                h = h + rowDistance + colDistance; //add to total.
            }
        }
        if(h==0){
            for(int i = 2; i < 6 ; i++) { //From each number starting from 1 until 8.
                if(i==2||i==3||i==5||i==6){
                    int[] locationOfNumberInCurrent = getLocationOfNumber(state, i); //Get location of the number in the goal state.
                    int[] locationOfNumberInGoalState = getLocationOfNumber(goalState, i); //Get the location of of the number in the state.
                    int rowDistance = Math.abs(locationOfNumberInCurrent[0] - locationOfNumberInGoalState[0]); //Calculate the distance to i -> absulute value of state[i] - goal[i]
                    int colDistance = Math.abs(locationOfNumberInCurrent[1] - locationOfNumberInGoalState[1]); //Calculate the distance to j -> absulute value of state[j] - goal[j]
                    h = h + rowDistance + colDistance; //add to total.
                }
            }
        }

        return h;
    }

    public static int penalty(int[][]state){
        int h=0;
        //find num place
        for(int i=0;i<state.length;i++){
            for(int j=0;j<state[i].length;j++){
                if(Heuristic.getDistance(state[i][j],i,j)>0){
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
                distance=Math.abs((i[2]-row)+(i[1]-column));
                break;
            }
        }
        return distance;
    }
}
