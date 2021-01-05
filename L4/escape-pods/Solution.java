import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Solution {
    public static void main(String[] args) {

//        Random rand = new Random();


//        int[][] x = new int[6][6];
//        int size = rand.nextInt(7-3) + 3;
////        int size = 1;
//        int[][] x = new int[size][size];
//
//        int up = 10;
//        int low = -2;
//        for(int i = 0; i< x.length; i++){
//            for(int j = 0; j< x.length; j++){
//                if(i==j){
//                    x[i][j] = 0;
//                }else{
//                    x[i][j] = rand.nextInt(up-low) + low;
//                }
//            }
//        }

        //Test case 1, answer 16
        int[] x = {0,1};
        int[] y = {4,5};

        int[][] z = {{0, 0, 4, 6, 0, 0}, {0, 0, 5, 2, 0, 0}, {0, 0, 0, 0, 4, 4}, {0, 0, 0, 0, 6, 6}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}};

        //Test case 2, answer 6
//        int[] x = {0};
//        int[] y = {4};
//
//        int[][] z = {{0, 7, 0, 0}, {0, 0, 6, 0}, {0, 0, 0, 8}, {9, 0, 0, 0}};

        System.out.println("Sources: " + Arrays.toString(x));
        System.out.println("Sinks: " + Arrays.toString(y));
        System.out.println("Paths:\n");

        // Prints the array in a nice format
        for (int i = 0; i < z.length; i++) {
            for (int j = 0; j < z.length; j++) {
                System.out.print(z[i][j] + "\t");
            }
            System.out.println();
        }


        System.out.println("---------------\nSolution: " + solution(x, y, z));
    }

    public static int solution(int[] entrances, int[] exits, int[][] path) {
        int[][] newGraph = singleSourceSink(entrances, exits, path);



        return 0;
    }

    public static int[][] singleSourceSink(int[] entrances, int[] exits, int[][] path){
        //Create a new graph with a single source/sink linking to all entrances and exits
        //For multiple sources/sinks on the ford fulkerson algorithm

        int[][] modifiedGraph = new int[path.length+2][path.length+2];

        //Add original graph to modified graph
        for(int i = 0; i<path.length; i++){
            for(int j = 0; j<path.length; j++){
                modifiedGraph[i+1][j+1] = path[i][j];
            }
        }

        int MAX = Integer.MAX_VALUE;

        //Link supersource (0) to all the original sources
        for(int x: entrances){
            modifiedGraph[0][x] = MAX;
        }

        //Link supersink (modifiedGraph.length - 1) to all the original sources
        for(int y: exits){
            modifiedGraph[y][modifiedGraph.length - 1] = MAX;
        }

        return modifiedGraph;
    }

    public static int[] bfs(int[][] graph){
        return null;
    }
}