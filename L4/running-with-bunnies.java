import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Solution {
    public static void main(String[] args) {

//        Random rand = new Random();

//        int[][] x = {{0, 1, -1, 12, 0}, {1, 0, 1, 1, 1}, {1, 1, 0, 1, 1}, {23, 23, 23, 23, 23}, {1, 1, 1, 1, 0}};
//        int[][] x = {{0, 1, 1, 1, 1}, {1, 0, 1, 1, 1}, {1, 1, 0, 1, 1}, {1, 1, 1, 0, 1}, {1, 1, 1, 1, 0}}; // Given test case T = 3
        int[][] x = {{0, 2, 2, 2, -1}, {9, 0, 2, 2, -1}, {9, 3, 0, 2, -1}, {9, 3, 2, 0, -1}, {9, 3, 2, 2, 0}}; // Given test case T = 1
//        int[][] x ={{0, 2, 2, 2, -1}, {9, 0, 2, 0, -1}, {9, 3, 0, 2, -1}, {9, 0, 2, 0, -1}, {9, 3, 2, 2, 0}};
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

        // Prints the array in a nice format
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x.length; j++) {
                System.out.print(x[i][j] + "\t");
            }
            System.out.println();
        }
        int max = 20;
        int min = 1;
//        int y = rand.nextInt(max - min) + min;
        int y = 1;
        System.out.println("Time: " + y);

        System.out.println("---------------\nSolution: " + Arrays.toString(solution(x, y)));
    }

    static int maxNegative = 0;
    static List<int[]> permutations;

    public static int[] solution(int[][] times, int times_limit) {

        if (times.length <= 2 || times.length != times[0].length) {
            System.out.println("Bad array size");
            return new int[]{};
        }

        // If there is a negative cycle, just return all bunnies
        if (negativeCyclePresence(times)) {
            System.out.println("Negative cycle");
            int[] answer = new int[times.length - 2];
            for (int i = 0; i < answer.length; i++) {
                answer[i] = i;
            }
            return answer;
        }

        permutations = new ArrayList<int[]>();

        int[][] floydDistances = floyd(times);

        // Prints the array in a nice format
        for (int i = 0; i < floydDistances.length; i++) {
            for (int j = 0; j < floydDistances.length; j++) {
                System.out.print(floydDistances[i][j] + "\t");
            }
            System.out.println();
        }

        generateAllPermutations(times.length - 2);

        System.out.println();

//        for(int i = 0; i<times.length-2;i++) {
        for (int i = times.length - 2; i >= 0; i--) {
            int finalI = i;
            permutations.sort((s1, s2) -> {
                if (s1.length > finalI && s2.length > finalI && s1.length == s2.length)
                    return s1[finalI] - s2[finalI];
                else
                    return 0;
            });
        }

//        permutations.sort((s1, s2) -> s2.length - s1.length);

        permutations.sort((s1, s2) -> {
            int l1 = s1.length;
            int l2 = s2.length;

            if (l1 != l2) {
                return l2 - l1;
            }

            for (int i = 0; i < l1; i++) {
                if (s1[i] == s2[i]) {
                    continue;
                } else {
                    return s1[i] - s2[i];
                }
            }

            return 0;

        });


        for (int[] x : permutations) {
//            System.out.println(Arrays.toString(x));

//            System.out.println(Arrays.toString(x) + "\t" +  getPathLength(x, floydDistances));
            System.out.println("\n\n" + Arrays.toString(x) + "\t");
            if (getPathLength(x, floydDistances) <= times_limit) {
                System.out.println("RETURNING " + Arrays.toString(x) + "\t" + getPathLength(x, floydDistances));

                Arrays.sort(x);
                for (int i = 0; i < x.length; i++)
                    x[i]--;
                return x;
            }
        }

        return null;
    }

    private static int getPathLength(int[] path, int[][] floydDistances) {
        if (path.length == 0) {
            return 0;
        }
//        System.out.println("\n");

        int pathCost = 0;
//        System.out.println("PaTh: " + pathCost);
        //Go from start to 1st space
        pathCost += floydDistances[0][path[0]];
        System.out.println("Path: 0, " + path[0] + "\tcost: " + pathCost);

        //Add cost
        for (int i = 0; i < path.length - 1; i++) {

            pathCost += floydDistances[path[i]][path[i + 1]];
//            System.out.println("PaTh: " + pathCost);
            System.out.println("Path: " + path[i] + ", " + path[i + 1] + "\tcost: " + pathCost);


        }

        //Go from end to doors


        pathCost += floydDistances[path[path.length - 1]][floydDistances.length - 1];
//        System.out.println("PaTh: " + pathCost);
        System.out.println("Path: " + path[path.length - 1] + ", " + Integer.toString(floydDistances.length - 1) + "\tcost: " + pathCost);

        return pathCost;
    }

    private static void generateAllPermutations(int size) {
        if (size == 1) {
            permutations.add(new int[]{1});
            return;
        }

        int[] input = new int[size];
        for (int i = 0; i < input.length; i++) {
            input[i] = i + 1;
        }

        int[][] subsets = getSubsets(input);
        System.out.println(Arrays.deepToString(subsets));

        for (int[] x : subsets) {
            heaps(x, x.length);
        }
    }

    // Print all subsets of a given set
    private static int[][] getSubsets(int set[]) {
        int size = set.length;
        List<int[]> output = new ArrayList<>();

        // Run a loop for printing all 2^n
        // subsets one by one
        for (int i = 0; i < (1 << size); i++) {
            System.out.print("{ ");
            List<Integer> list = new ArrayList<Integer>();

            // Print current subset
            for (int j = 0; j < size; j++)

                // (1<<j) is a number with jth bit 1
                // so when we 'and' them with the
                // subset number we get which numbers
                // are present in the subset and which
                // are not
                if ((i & (1 << j)) > 0) {
                    System.out.print(set[j] + " ");
                    list.add(set[j]);
                }
            System.out.println("}");

            int[] currentArray = new int[list.size()];
            int k = 0;
            for (Integer x : list)
                currentArray[k++] = x;
            output.add(currentArray);

        }
//        return output.toArray();

        int[][] outputArray = new int[output.size()][output.size()];
        int k = 0;
        for (int[] x : output) {
            outputArray[k++] = x;
        }

        return outputArray;
    }

    // Generating permutation using Heap's Algorithm
    private static void heaps(int a[], int size) {
        if (size == 1) {
            int[] aReversed = Arrays.copyOf(a, a.length);

            permutations.add(aReversed);
            return;
        }

        for (int i = 0; i < size; i++) {
            heaps(a, size - 1);

            if (size % 2 == 1) {
                a = swap(a, 0, a, size - 1);
            } else {
                a = swap(a, i, a, size - 1);
            }
        }
    }

    private static int[] swap(int[] a, int indexA, int[] b, int indexB) {
        int temp = a[indexA];
        a[indexA] = a[indexB];
        a[indexB] = temp;
        return a;
    }


    // Checks for negative cycle in entire matrix using floyd Warshal
    private static boolean negativeCyclePresence(int[][] x) {
        int[][] floydDistance = floyd(x);

        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x.length; j++) {
                if (floydDistance[i][j] + floydDistance[j][i] < 0) {
                    return true;
                }
            }
        }

        return false;
    }

    // Does floyd warshall to find all shortest distances
    private static int[][] floyd(int[][] x) {

        // Create and fill output to avoid changing x
        int[][] output = new int[x.length][x.length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x.length; j++) {
                output[i][j] = x[i][j];
            }
        }

        // Do Floyd
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x.length; j++) {
                for (int k = 0; k < x.length; k++) {
                    int newDistance = output[j][i] + output[i][k];
                    if (newDistance < output[j][k])
                        output[j][k] = newDistance;
                }
            }
        }

        return output;

    }
}