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
