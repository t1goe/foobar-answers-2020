import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Solution {
    static List<int[]> permutations;

    public static int[] solution(int[][] times, int times_limit) {

        //Check for bad array sizes
        if (times.length <= 2 || times.length != times[0].length) {
            return new int[]{};
        }

        // If there is a negative cycle, just return all bunnies
        if (negativeCyclePresence(times)) {
            int[] answer = new int[times.length - 2];
            for (int i = 0; i < answer.length; i++) {
                answer[i] = i;
            }
            return answer;
        }

        //Init perm list
        permutations = new ArrayList<>();

        //Calculates floyd distances (shortest all source all path algorithm)
        int[][] floydDistances = floyd(times);

        //Autofill permutations which is static
        generateAllPermutations(times.length - 2);

        //Sorts the permutations by custom criteria which ensure that the biggest combinations are first, then sort
        //by the lower ID bunnies
        permutations.sort((s1, s2) -> {
            int l1 = s1.length;
            int l2 = s2.length;

            //If different length prioritize bigger perm
            if (l1 != l2) {
                return l2 - l1;
            }

            //Check for the first digit different. Lower one wins
            for (int i = 0; i < l1; i++) {
                if (s1[i] != s2[i]) {
                    return s1[i] - s2[i];
                }
            }

            return 0;

        });


        //Search all permutations for the first one which is within time
        //Because its sorted by more desirable ones first, simply return when a valid one is found
        for (int[] x : permutations) {
            if (getPathLength(x, floydDistances) <= times_limit) {
                Arrays.sort(x);

                //Convert the positions to the bunnies' ID
                for (int i = 0; i < x.length; i++)
                    x[i]--;
                return x;
            }
        }

        return null;
    }

    private static int getPathLength(int[] path, int[][] floydDistances) {
        //Path length SHOULD never be 0 but better safe than sorry
        if (path.length == 0) {
            return 0;
        }

        int pathCost = 0;

        //Go from start to 1st space
        pathCost += floydDistances[0][path[0]];

        //Add cost for each subsequent move
        for (int i = 0; i < path.length - 1; i++) {
            pathCost += floydDistances[path[i]][path[i + 1]];
        }

        //Go from last bunny to blast doors
        pathCost += floydDistances[path[path.length - 1]][floydDistances.length - 1];

        return pathCost;
    }

    // Generate all permutations and subset permutations up to the given size
    // This generates all possible orders to rescue the bunnies in
    private static void generateAllPermutations(int size) {
        if (size == 1) {
            permutations.add(new int[]{1});
            return;
        }

        //Create an array {1,2,3...} to act as our baseline to generate subsets/permutations from
        int[] input = new int[size];
        for (int i = 0; i < input.length; i++) {
            input[i] = i + 1;
        }

        int[][] subsets = getSubsets(input);

        //Run heap's algorithm on all subsets generated to get all permutations of those subsets
        for (int[] x : subsets) {
            heaps(x, x.length);
        }
    }

    private static int[][] getSubsets(int[] set) {
        //Basically generates all subsets by using binary numbers as a sequence of true/false bits to create all possible
        //subsets
        //After all, each item in the set can only have one of 2 possibilities, present or not
        //By counting a binary number up you cover all possible cases

        int size = set.length;
        List<int[]> output = new ArrayList<>();

        //Loop through all of the 2^size possibilities
        for (int i = 0; i < (1 << size); i++) {
            List<Integer> list = new ArrayList<Integer>();

            for (int j = 0; j < size; j++) {

                //Bitwise AND our number with the Jth bit to find if it is present or not
                if ((i & (1 << j)) > 0) {
                    list.add(set[j]);
                }
            }

            //Manually convert the arraylist to an int array and add to the super list
            int[] currentArray = new int[list.size()];
            int k = 0;
            for (Integer x : list)
                currentArray[k++] = x;
            output.add(currentArray);

        }

        //manually convert all int arrays to a 2d array to output
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

    //Swaps ints based on their indexes
    private static int[] swap(int[] a, int indexA, int[] b, int indexB) {
        int temp = a[indexA];
        a[indexA] = b[indexB];
        b[indexB] = temp;
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