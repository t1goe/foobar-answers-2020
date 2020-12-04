import java.util.Arrays;

public class Solution2 {
    public static void main(String[] args){

//        int[][] x = {{0, 2, 1, 0, 0}, {0, 0, 0, 3, 4}, {0, 0, 0, 0, 0}, {0, 0, 0, 0,0}, {0, 0, 0, 0, 0}};
        int[][] x = {{0, 1, 0, 0, 0, 1}, {4, 0, 0, 3, 2, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}};
//        int[][] x = {{0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}};



        for(int i = 0; i< x.length; i++){
            for(int j = 0; j< x.length; j++){
                System.out.print(x[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("---------------\nSolution: " + Arrays.toString(solution(x)));


    }

    public static int[] solution(int[][] m) {
        if(rowTotal(m[0]) == 0)
            return new int[]{1,1};
        int size = m.length;
        if(size == 0)
            return new int[]{};

        boolean[][] visited = new boolean[size][size];

        int[] answer = recursiveHelper(m, visited, 0);

        // Get number of terminals and which ones are terminal
        boolean[] isTerminal = new boolean[size];
        int numberOfTerminals = 0;
        for(int i = 0; i < size; i++){
            if(rowTotal(m[i]) == 0){
                numberOfTerminals++;
                isTerminal[i] = true;
            }
        }

        // Create output array for all terminals and denominator
        int[] output = new int[numberOfTerminals + 1];
        int currentPosition = 0;
        for(int i = 0; i < size; i++){
            if(isTerminal[i]){
                output[currentPosition++] = answer[i];
            }
        }

        output[output.length - 1] = answer[answer.length - 1];
        return output;
    }

    private static int[] recursiveHelper(int[][] input, boolean[][] visited, int currentState){
        System.out.println(currentState);
        int rowTotal = rowTotal(input[currentState]);

        //Have to make a copy of visited because java passes by "reference"
        boolean[][] tempVisited = new boolean[input.length][input.length];
        for(int i = 0; i< visited.length; i++){
            for(int j = 0; j< visited.length;j++){
                tempVisited[i][j] = visited[i][j];
            }
        }

        // Check if terminal state
        if(rowTotal == 0){
            int output[] = new int[input.length + 1];

            output[output.length-1] = -1;

            return output;
        }

        int[] output = new int[input.length + 1];
        output[output.length-1] = 1;

        for(int i = 0; i<input.length; i++){
//            System.out.println("CurrentState: " + Integer.toString(currentState) + ", " + Integer.toString(i));
//            System.out.println("tempVisited: " + Boolean.toString(tempVisited[currentState][i]) + ", " + Integer.toString(input[currentState][i]) + "\n");
            if(!tempVisited[currentState][i] && input[currentState][i] != 0){
                tempVisited[currentState][i] = true;
                int[] currentValues = recursiveHelper(input, tempVisited, i);

                // If terminal
                if(currentValues[currentValues.length - 1] == -1){
                    currentValues[i] = input[currentState][i];
                    currentValues[currentValues.length - 1] = rowTotal;
                }else{ // Not terminal so * current fraction
                    currentValues = multiplyScalar(currentValues, input[currentState][i], rowTotal);
                }

                output = addOutputs(currentValues, output);
            }
        }

        return output;
    }

    private static int[] multiplyScalar(int[] input, int num, int dom){
        for(int i = 0; i<input.length - 1; i++){
            input[i] *= num;
        }
        input[input.length - 1] *= dom;
        return simplifyFractions(input);
    }

    private static int[] addOutputs(int[] input1, int[] input2){
        System.out.println("Input1: \t\t" + Arrays.toString(input1));
        System.out.println("Input2: \t\t" + Arrays.toString(input2));
        System.out.println();

        int denominator = input1[input1.length-1] * input2[input2.length-1];

        int[] output = new int[input1.length];
        output[output.length - 1] = denominator;

        for(int i = 0; i < (input1.length - 1); i++){
            if(input1[i] != 0 && input2[i] != 0){
                output[i] = (input1[i] * input2[input2.length - 1]) + (input2[i] * input1[input1.length - 1]);
            }else if(input1[i] != 0){
                output[i] = input1[i] + input2[i];
                output[i] *= input2[input2.length - 1];
            }else if(input2[i] != 0){
                output[i] = input1[i] + input2[i];
                output[i] *= input1[input1.length - 1];
            }
        }

        System.out.println("Output: \t\t" + Arrays.toString(output));

        output = simplifyFractions(output);

        System.out.println("Reduced Output: " + Arrays.toString(output) + "\n\n\n");

        return output;
    }

    private static int[] simplifyFractions(int[] input){
        int GCD = input[input.length - 1];
        for(int i = 0; i < input.length; i++){
            while(input[i] % GCD != 0){
                GCD--;
                i = 0;
            }

            if(GCD <= 1){
                break;
            }
        }

        for(int i = 0; i < input.length; i++){
            input[i] /= GCD;
        }

//        System.out.println("GCD: " + Integer.toString(GCD));
        return input;
    }

    private static int rowTotal(int[] input){
        int count = 0;
        for(int i = 0; i<input.length; i++){
            count += input[i];
        }
        return count;
    }
}