import java.util.List;
import java.util.ArrayList;

public class FinalSolution {

    public static int solution(int[] entrances, int[] exits, int[][] path) {
        int[][] residualGraph = singleSourceSink(entrances, exits, path);
        int flow = 0;

        List<Integer> currentPath = pathFinder(residualGraph, new boolean[residualGraph.length], 0);

        while(currentPath != null){

            //get remaining capacity of path
            int capacity = 20000;
            int x = 0;

            for (int i = 1; i<currentPath.size(); i++) {
                int y = currentPath.get(i);
                capacity = Math.min(capacity, residualGraph[x][y]);
                x = y;
            }

            //Update flow
            flow += capacity;

            // update residual network
            x = 0;
            for (int y : currentPath) {
                residualGraph[x][y] -= capacity;
//                residualGraph[y][x] += capacity;
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
            List<Integer> output = new ArrayList<>();
            output.add(current);
            return output;
        }

        boolean[] newVisited = new boolean[visited.length];
        for(int i=0; i<visited.length;i++){
            newVisited[i] = visited[i];
        }
        newVisited[current] = true;

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