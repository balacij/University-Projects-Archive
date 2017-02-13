// iterative
/* My program uses elementary row operations (iteratively) to find the inverse matrix.
Name: Jason Balaci
MacID: balacij
Description: Multiply input matrices and output the inverse of the multiplied input matrices.
*/
public class HWK2_balacij {

    public static void main(String[] args) {
        // what do we do if the user provides 0 as the number of matrices?
        int matrices = Integer.parseInt(args[0]); // parse the first argument provided; the number of matrices to parse

        if (matrices == 0) // if there aren't any matrices to multiply, don't do anything
            return;

        boolean stop = false; // stopper used to check if we should quit the program on account of the provided matrices being incompatible for matrix multiplication
        for (int i = 1; i < matrices; i++) { // iterate over the matrice sizes...
            int mN = Integer.parseInt(args[1 + i * 2]); // parsing m_N
            int nN_1 = Integer.parseInt(args[i * 2]); // parsing n_N-1
            if (mN != nN_1) { // if they arent equal, then we tell the user the provided matrices are incompatible for matrix multiplication.
                System.out.println("Multiplication error."); // tell the user that the matrices cant be multiplied
                stop = true; // set stop to true
                break; // stop the loop from iterating further
            }
        }
        if (stop) // if an error was found, then we dont do anything past this point
            return; // stop the rest of the code from executing

        double[][] accumulatorMatrix = null; // create a matrix which will become the final product of all the provided input matrices
        int elementIndex = 1 + (2 * matrices); // create a counter to see where the first element of the first matrix starts at

        for (int i = 0; i < matrices; i++) { // iterate over the matrices...
            int curRows = Integer.parseInt(args[2 * i + 1]); // parse the expected row count for the i-th matrix
            int curColumns = Integer.parseInt(args[2 * i + 2]); // parse the expected column count for the i-th matrix
            double[][] cur = new double[curRows][curColumns]; // create a matrix of size curRows by curColumns

            for (int y = 0; y < curRows; y++) { // iterate over the rows
                for (int x = 0; x < curColumns; x++) { // iterate over the columns
                    cur[y][x] = Double.parseDouble(args[elementIndex]); // parse the next argIndex
                    elementIndex++; // increment elementIndex so that it moves the index to point towards the next index
                }
            }


            // if the accumulatorMatrix is null, then the matrix we just parsed is all we have thus far, so we set
            // accumulatorMatrix to cur, otherwise, we multiply accumulatorMatrix and cur, respectively.
            accumulatorMatrix = accumulatorMatrix == null ? cur : matrix_multiply(accumulatorMatrix, cur); // ^
        }

        double[][] inverse = invert(accumulatorMatrix); // create an inverse matrix variable, assign the returned value of invert(accumulatorMatrix) to it
        if (inverse == null) { // if we got back null, then...
            System.out.println("Matrix not invertible"); // tell the user that the fully multiplied matrix is not invertible.
        } else { // otherwise...
            print(inverse); // print the inverse matrix!
        }
    }

    private static double[][] invert(double[][] matrix) { // create a new method... intended to invert a provided matrix through Guass-Jordan Elimination
        /*
        How the algorithm (Guass-Jordan elimination - rref) works:
            i. Creates two parallel matrices, one to carry a copy of the input matrix, and one to carry the identity which will become the resultant inverse.
            ii. Zero out all the values below the main diagonal on the copy matrix, applying the same operation onto the resultant matrix, using elementary row operations, retaining 1s across the main diagonal
                iia. Swap rows of the copy matrix and resultant matrix where needed, respectively.
                iib. If a row of zeroes is left, then the input matrix is not invertible.
                iib.  Force the values on the main diagonal of the copy matrix through scaling, applying the same operations onto the resultant matrix.
            iii.  Zero out all the values above the main diagonal on the copy matrix, applying the same operation onto the resultant matrix, using elementary row operations
        */
        if (matrix.length != matrix[0].length) // if the matrix isn't square,..
            return null; // return null so as to tell the user that the input matrix is not invertible

        double[][] result = eye(matrix.length); // create a resultant matrix which is defined as I_n
        double[][] copy = copy(matrix); // create a deep copy of the input matrix so that we dont break the users input matrix if they feel like reusing it later

        boolean broken = false; // create a boolean variable intended to be used as an invertibility check
        int n = matrix.length; // create an integer variable intended to hold the length of the matrix
        for (int y = 0; y < n; y++) { // iterate over the rows of the matrix...
            int yi = 1; // create a 'push' variable
            while (copy[y][y] == 0 && y + yi < n) { // while the main diagonal has a 0 in it's way and we haven't yet swapped with all possible rows below the current y-th row
                swaprows(copy, y, y + yi); // swap the y-th row with the (y+yi)-th pushed row on the copy of the matrix
                swaprows(result, y, y + yi); // ^ but on the resultant matrix, as per the algorithm
                yi++; // increment the 'push' variable
            }

            if (copy[y][y] == 0) { // if we have pushed all the way but have yet to find a suitable row, then we can infer that the matrix is not invertible
                broken = true; // set broken to true :(
                break; // get out of the for loop
            }

            double scalar = 1 / copy[y][y]; // create a scalar variable intended to be used to scale down the current copied row so that the value on the main diagonal that touches this row is 1.
            multrow(copy, y, scalar); // scale the row on the copied matrix
            multrow(result, y, scalar); // scale the row on the resultant matrix, as per the algorithm

            for (int next = y + 1; next < n; next++) { // iterate the rest of the rows, ...
                scalar = -copy[next][y]; // setting scalar to the next value required to zero out the rest of the values below the y-th value on the main diagonal
                addrows(copy, next, y, scalar); // zero them out!
                addrows(result, next, y, scalar); // apply to the resultant matrix as well, as per the algorithm
            }
        }

        if (broken) // if broken was set to true from the for loop, then...
            return null; // return null so as to tell the user that the input matrix is not invertible

        for (int y = n - 1; y >= 0; y--) { // iterate over all of the rows again! (starting from the bottom this time)
            for (int next = y - 1; next >= 0; next--) { // iterate over all the rows above the current^ row
                double scalar = -copy[next][y]; // create a double variable which will be used to zero out all of the values over the y-th value on the main diagonal
                addrows(copy, next, y, scalar); // zero out all the values above the y-th value on the main diagonal
                addrows(result, next, y, scalar);  // as per the algorith, we must apply the same operation to the resultant matrix
            }
        }

        return result; // return the resultant inverse matrix!!! :)
    }

    private static void multrow(double[][] matrix, int row, double scalar) { // create a new method... intended to emulate a single elementary row operation; multiplying a row by a constant
        for (int i = 0; i < matrix[0].length; i++) { // iterate over the specified row....
            matrix[row][i] *= scalar; // multiplying each value in the row by the provided scalar
        }
    }

    private static void addrows(double[][] matrix, int a, int b, double scalar) { // create a new method... intended to emulate a single elementary row operation; adding a multiple of one row to another
        for (int i = 0; i < matrix[0].length; i++) { // iterate over the a-th row of matrix
            matrix[a][i] += matrix[b][i] * scalar; // respectively add a scaled version of the b-th rows i-th element to the i-th element of the a-th row
        }
    }

    private static void swaprows(double[][] matrix, int a, int b) { // create a new method... intended to emulate a single elementary row operation; swapping two rows
        for (int i = 0; i < matrix[0].length; i++) { // iterate over the rows...
            double temp = matrix[a][i]; // create a storage variable intended to hold the value of matrix[a][i] when it gets replaced
            matrix[a][i] = matrix[b][i]; // replace the value of matrix[a][i] with matrix[b][i]
            matrix[b][i] = temp; // replace the value of matrix[b][i] with the previously stored variable so as to emulate swapping two values
        }
    }

    private static double[][] matrix_multiply(double[][] a, double[][] b) { // create a new method... intended to multiply two matrices
        if (a[0].length != b.length) // if a is m by n, then b is expected to be n by k, if not, return null so as to show that the input matrices are incompatible
            return null;

        int rows = a.length, columns = b[0].length, iters = a[0].length; // create three variables; one to get the rows of the resultant matrix, one to get the columns of the resultant matrix, and one to count how many times the method will have to iterate to get the dot product of the respective row and column vectors
        double[][] result = new double[rows][columns]; // define a new matrix called result

        for (int y = 0; y < rows; y++) { // iterate over the rows
            for (int x = 0; x < columns; x++) { // iterate over the columns
                for (int i = 0; i < iters; i++) { // find the dot product of the y-th row vector of a and the x-th column vector of b
                    result[y][x] += a[y][i] * b[i][x]; // ^
                }
            }
        }

        return result; // return the resultant matrix
    }

    private static double[][] copy(double[][] matrix) { // create a deep copy of a matrix
        if (matrix.length == 0) // if the matrix is empty, give them another empty matrix
            return new double[0][]; // ^

        double[][] result = new double[matrix.length][matrix[0].length]; // create a resultant matrix
        for (int y = 0; y < matrix.length; y++) { // iterate over the rows
            System.arraycopy(matrix[y], 0, result[y], 0, matrix[0].length); // copy the columns over to the resultant matrix
        }
        return result; // return the deep copied resultant matrix
    }

    private static double[][] eye(int n) { // create a square identity matrix of size n
        double[][] result = new double[n][n]; // define a resultant matrix
        for (int i = 0; i < n; i++) { // iterate over the main diagonal of the matrix, ...
            result[i][i] = 1; // setting the value to 1 on the main diagonal
        }
        return result;
    }

    private static void print(double[][] matrix) { // create a new method... intended to print out a matrix to the screen in a pretty way
        for (double[] doubles : matrix) { // iterate over the rows
            for (double d : doubles) { // iterate over the values in the rows
                if (d > -0.01 && d <= 0) { // if the number is between -0.01, exclusively, and 0, inclusively, then we should set it to zero so as to avoid outputting negative zeroes into console.
                    d = 0; // set the value to 0
                }
                System.out.printf("%.2f ", d); // as per what Wenqiang Chen and Micheal Liut said to do (on piazza), we should print out only the first two decimal places, where the hundredth place is rounded through formatting
            }
        }
        System.out.println(); // go to the next line so that the command line look of running this program is slightly better.
    }

}