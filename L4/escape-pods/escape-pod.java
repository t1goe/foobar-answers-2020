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

        System.out.println("---------------\nSolution: " + solution(x, y, z));
    }

    public static int solution(int[] entrances, int[] exits, int[][] path) {
        // Your code here
        return 0;
    }
}