import java.util.Arrays;
import java.util.LinkedList;
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
//        int[] x = {0,1};
//        int[] y = {4,5};
//
//        int[][] z = {{0, 0, 4, 6, 0, 0}, {0, 0, 5, 2, 0, 0}, {0, 0, 0, 0, 4, 4}, {0, 0, 0, 0, 6, 6}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}};

        //Test case 2, answer 6
        int[] x = {0};
        int[] y = {3};

        int[][] z = {{0, 7, 0, 0}, {0, 0, 6, 0}, {0, 0, 0, 8}, {9, 0, 0, 0}};

        System.out.println("Sources: " + Arrays.toString(x));
        System.out.println("Sinks: " + Arrays.toString(y));
        System.out.println("Paths:");

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
        int[][] residualGraph = singleSourceSink(entrances, exits, path);
        int flow = 0;

        List<Integer> currentPath = pathFinder(residualGraph, new boolean[residualGraph.length], 0);

        System.out.println(currentPath.toString());

        while(currentPath != null){

            //Bug testing
            System.out.println("\n\n"+currentPath.toString());

            // Prints the array in a nice format
            System.out.print(".\t\t");
            for (int i = 0; i < residualGraph.length; i++) {
                System.out.print(i + ".\t\t");
            }
            System.out.println();


            for (int i = 0; i < residualGraph.length; i++) {
                System.out.print(i + ".\t\t");
                for (int j = 0; j < residualGraph.length; j++) {
                    System.out.print(residualGraph[i][j] + "\t\t");
                }
                System.out.println();
            }
            //Bug testing

            //get remaining capacity of path
            int capacity = 20000;
            int x = 0;
//            for (int y : currentPath) {
            for (int i = 1; i<currentPath.size(); i++) {
                int y = currentPath.get(i);
                System.out.println("resG: " + residualGraph[x][y]);
                System.out.println("[x,y]: " + Integer.toString(x) + ", " + Integer.toString(y) + "\n");
                capacity = Math.min(capacity, residualGraph[x][y]);
                x = y;
            }
            System.out.println("Flow: " + flow);
            System.out.println("Capacity: " + capacity);


            //Update flow
            flow += capacity;

            // update residual network
            x = 0;
            for (int y : currentPath) {
                residualGraph[x][y] -= capacity;
                residualGraph[y][x] += capacity;
                x = y;
            }

            currentPath = pathFinder(residualGraph, new boolean[residualGraph.length], 0);
        }

        return flow;
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
            modifiedGraph[0][x+1] = MAX;
        }

        //Link supersink (modifiedGraph.length - 1) to all the original sources
        for(int y: exits){
            modifiedGraph[y+1][modifiedGraph.length - 1] = MAX;
        }

        return modifiedGraph;
    }

    public static List<Integer> pathFinder(int[][] graph, boolean[] visited, int current){
        //Recursive pathfinding algorithm, finds a path from source to sink that has some residual capacity

        if(current == graph.length - 1){
//            List<Integer> output = new List<>();
            List<Integer> output = new ArrayList<>();
            output.add(current);
            return output;
        }

        boolean[] newVisited = new boolean[visited.length];
        for(int i=0; i<visited.length;i++){
            newVisited[i] = visited[i];
        }
        newVisited[current] = true;

//        System.out.println(Arrays.toString(newVisited)+"\n");

        for(int i=0; i<graph.length; i++){
            if(!newVisited[i] && graph[current][i] > 0){
                List<Integer> path = pathFinder(graph, newVisited, i);
                if(path!=null){
                    path.add(0, current);
                    return path;
                }
            }
        }

        return null;
    }

}