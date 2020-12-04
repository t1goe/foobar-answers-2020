import java.util.Arrays;
import java.util.Random;

public class Solution {
    public static void main(String[] args){

//        int[][] x = {{2, 2, 1, 33, 3}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0,0}, {0, 0, 0, 0, 0}};
//        int[][] x = {{0, 1, 0, 0, 0, 1}, {4, 0, 0, 3, 2, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}};
//        int[][] x = {{1,1,0,1},{1,1,0,0},{0,0,0,0},{0,0,0,0}};
//        int[][] x = {{1,1,1,1},{1,1,1,1},{0,0,0,0},{0,0,0,0}};
        int[][] x = {{0, 0, 12, 0, 15, 0, 0, 0, 1, 8},
                {0, 0, 60, 0, 0, 7, 13, 0, 0, 0},
                {0, 15, 0, 8, 7, 0, 0, 1, 9, 0},
                {23, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {37, 35, 0, 0, 0, 0, 3, 21, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
//        Random rand = new Random();
//        int size = rand.nextInt(10-1) + 1;;
//        int[][] x = new int[size][size];
//
//        int up = 10;
//        int low = 0;
//        for(int i = 0; i< x.length; i++){
//            boolean term = rand.nextInt(1000)%2 == 0;
//            for(int j = 0; j< x.length; j++){
//                if(!term || i==0)
//                    x[i][j] = rand.nextInt(up-low) + low;
//            }
//        }

        for(int i = 0; i< x.length; i++){
            for(int j = 0; j< x.length; j++){
                System.out.print(x[i][j] + "\t");
            }
            System.out.println();
        }

        System.out.println("---------------\nSolution: " + Arrays.toString(solution(x)));
    }

    public static int[] solution(int[][] m) {
        if(rowTotal(m[0]) == 0)
            return new int[]{1,1};

        int size = m.length;

        // Get number of terminals and which ones are terminal
        boolean[] isTerminal = new boolean[size];
        int numberOfTerminals = 0;
        for(int i = 0; i < size; i++){
            if(rowTotal(m[i]) == 0){
                numberOfTerminals++;
                isTerminal[i] = true;
            }
        }

        int[][] markovStepOne = new int[size][size];
        int[] orderList = new int[size];
        int currentPosition = 0;

        // Put all terminals at top of matrix
        for(int i = 0; i<size; i++){
            if(isTerminal[i]){
                orderList[currentPosition] = i;
                markovStepOne[currentPosition++] = m[i];
            }
        }

        // Put all non-terminals at end of matrix
        for(int i = 0; i<size; i++){
            if(!isTerminal[i]){
                orderList[currentPosition] = i;
                markovStepOne[currentPosition++] = m[i];
            }
        }

        // Rearrange the columns
        int[][] markovStdForm = new int[size][size];
        for(int i = 0; i<size; i++){
            for(int j = 0; j<size; j++){
                markovStdForm[j][i] = markovStepOne[j][orderList[i]];
            }

        }

        // Add ID matrix to top left
        for(int i = 0; i<numberOfTerminals; i++){
            markovStdForm[i][i] = 1;
        }

        // Calculate F = (I - Q) ^ -1

        // First (I-Q)
        int numOfNonTerm = size - numberOfTerminals;
        double[][] Q = new double[numOfNonTerm][numOfNonTerm];
        for(int i = 0; i< numOfNonTerm; i++){
            for(int j = 0; j < numOfNonTerm; j++){
                Q[i][j] += markovStdForm[i + numberOfTerminals][j + numberOfTerminals];
                Q[i][j] /= rowTotal(markovStdForm[i + numberOfTerminals]);
                Q[i][j] *= -1;
            }
            Q[i][i] += 1;
        }

        // Get F
        double F[][] = invert(Q);

        // Get R
        double[][] R = new double[numOfNonTerm][numberOfTerminals];
        for(int i = 0; i< numOfNonTerm; i++){
            for(int j = 0; j < numberOfTerminals; j++){
                R[i][j] += markovStdForm[i + numberOfTerminals][j];
                R[i][j] /= rowTotal(markovStdForm[i + numberOfTerminals]);
            }
            Q[i][i] += 1;
        }

        double[][] FR = multiplyMatrices(F, R);

        int[][] probabilities = new int[FR[0].length][2];

        for(int i = 0; i < FR[0].length; i++){
            probabilities[i] = rationalize(FR[0][i]);
        }

        int[] answer = simplifyAndFormat(probabilities);

        return answer;
    }

    private static int[] simplifyAndFormat(int[][] a){
        int[] output = new int[a.length + 1];
        int commonDenominator = 1;

        for(int i = 0; i<a.length; i++){
            if(a[i][0] != 0){
                commonDenominator = lcm(commonDenominator, a[i][1]);
            }
        }

        for(int i = 0; i<a.length; i++){
            if(a[i][0] != 0){
                output[i] = (commonDenominator/a[i][1]) * a[i][0];
            }
        }

        output[output.length - 1] = commonDenominator;

        output = simplifyFractions(output);

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

        return input;
    }

    private static int lcm(int a, int b){
        int a1 = a;
        int b1 = b;

        while(a1 != b1){
            if(a1 > b1){
                b1 += b;
            }else if(b1 > a1){
                a1 += a;
            }
        }

        return a1;
    }

    private static int[] rationalize(double x){
        // Convert double into fraction (stored as {numerator, denominator})

        // Gonna be honest, this is not a great implementation for rationalizing a number like this
        // It's very hacky, and uses string comparison to override java's weird interactions with long decimals
        // But it works, so good until I think of something better

        if(x == 0)
            return new int[]{0, 0};

        double[] output = new double[]{1,1};

        double currentValue = output[0]/output[1];
        String cvString = Double.toString((double)Math.round(currentValue * 100000d) / 100000d);
//        cvString = cvString.substring(0, (int) Math.min(10, cvString.length()));

        String xString = Double.toString((double)Math.round(x * 100000d) / 100000d);
//        System.out.println(x);
//        System.out.println();

//        xString = xString.substring(0, (int) Math.min(10, xString.length()));

        int count = 0;
        while(!cvString.equals(xString)){
            System.out.println(Double.toString(output[0]) + " / " + Double.toString(output[1]));
            System.out.println(cvString + " " + xString + "\n");
            if(count++ == 200){
                break;
            }

            if(currentValue > x){
                output[1]++;
            }else if(currentValue < x){
                output[0]++;
            }else{
                break;
            }
            currentValue = output[0]/output[1];
            cvString = Double.toString((double)Math.round(currentValue * 100000d) / 100000d);
//            cvString = cvString.substring(0, (int) Math.min(10, cvString.length()));
        }
        System.out.println(Double.toString(output[0]) + " / " + Double.toString(output[1]));
        System.out.println(cvString + " " + xString + "\n");
        System.out.println("\n\n\n");

        return new int[]{(int) output[0], (int) output[1]};
    }

    private static double[][] multiplyMatrices(double[][] m1, double[][] m2){
        int m1Height = m1.length;
        int m1Length = m1[0].length;
        int m2Height = m2.length;
        int m2Length = m2[0].length;

        double[][] output = new double[m1Height][m2Length];

        for(int i = 0; i<m1Height; i++){
            for(int j = 0; j<m2Length; j++){
                double currentTotal = 0;
                for(int k = 0; k<m1Length; k++){
                    currentTotal += m1[i][k] * m2[k][j];
                }
                output[i][j] = currentTotal;
            }

        }
        return output;
    }

    private static int rowTotal(int[] input){
        int count = 0;
        for(int i = 0; i<input.length; i++){
            count += input[i];
        }
        return count;
    }

    private static double[][] invert(double a[][])
    {
        int size = a.length;
        double x[][] = new double[size][size];
        double b[][] = new double[size][size];
        int index[] = new int[size];
        for (int i=0; i<size; ++i)
            b[i][i] = 1;

        // Transform the matrix into an upper triangle
        gaussian(a, index);

        // Update the matrix b[i][j] with the ratios stored
        for (int i=0; i<size-1; ++i)
            for (int j=i+1; j<size; ++j)
                for (int k=0; k<size; ++k)
                    b[index[j]][k]
                            -= a[index[j]][i]*b[index[i]][k];

        // Perform backward substitutions
        for (int i=0; i<size; ++i)
        {
            x[size-1][i] = b[index[size-1]][i]/a[index[size-1]][size-1];
            for (int j=size-2; j>=0; --j)
            {
                x[j][i] = b[index[j]][i];
                for (int k=j+1; k<size; ++k)
                {
                    x[j][i] -= a[index[j]][k]*x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }

    private static void gaussian(double a[][], int index[])
    {
        int n = index.length;
        double c[] = new double[n];

        // Initialize the index
        for (int i=0; i<n; ++i)
            index[i] = i;

        // Find the rescaling factors, one from each row
        for (int i=0; i<n; ++i)
        {
            double c1 = 0;
            for (int j=0; j<n; ++j)
            {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }

        // Search the pivoting element from each column
        int k = 0;
        for (int j=0; j<n-1; ++j)
        {
            double pi1 = 0;
            for (int i=j; i<n; ++i)
            {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1)
                {
                    pi1 = pi0;
                    k = i;
                }
            }

            // Interchange rows according to the pivoting order
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i=j+1; i<n; ++i)
            {
                double pj = a[index[i]][j]/a[index[j]][j];

                // Record pivoting ratios below the diagonal
                a[index[i]][j] = pj;

                // Modify other elements accordingly
                for (int l=j+1; l<n; ++l)
                    a[index[i]][l] -= pj*a[index[j]][l];
            }
        }
    }
}