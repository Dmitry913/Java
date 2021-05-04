import java.util.Scanner;

public class Matrix {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("1. Add matrices\n" +
                    "2. Multiply matrix by a constant\n" +
                    "3. Multiply matrices\n" +
                    "4. Transpose matrix\n" +
                    "5. Calculate a determinant\n" +
                    "6. Inverse matrix\n" +
                    "0. Exit");
            choice = scanner.nextInt();
            switch (choice) {
                case 1: {
                    System.out.println("Enter size of first matrix: ");
                    double[][] matrix1 = initialiseMatrix();
                    System.out.println("Enter size of second matrix: ");
                    double[][] matrix2 = initialiseMatrix();
                    System.out.println("The result is:");
                    try {
                        printMatrix(addMatrix(matrix1, matrix2));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 2: {
                    System.out.println("Enter size of matrix");
                    double[][] matrix = initialiseMatrix();
                    printMatrix(multiplyConst(matrix, scanner.nextDouble()));
                    break;
                }
                case 3: {
                    System.out.println("Enter size of first matrix: ");
                    double[][] matrix1 = initialiseMatrix();
                    System.out.println("Enter size of second matrix: ");
                    double[][] matrix2 = initialiseMatrix();
                    System.out.println("The result is:");
                    try {
                        printMatrix(multiplyMatrix(matrix1, matrix2));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 4: {
                    System.out.println("1. Main diagonal\n" +
                            "2. Side diagonal\n" +
                            "3. Vertical line\n" +
                            "4. Horizontal line");
                    choice = scanner.nextInt();
                    System.out.println("Enter matrix size: ");
                    double[][] matrix = initialiseMatrix();
                    System.out.println("The result is:");
                    switch (choice) {
                        case 1:
                            printMatrix(transposeMainDiag(matrix));
                            break;
                        case 2:
                            printMatrix(transposeSideDiag(matrix));
                            break;
                        case 3:
                            printMatrix(transposeVerticalLine(matrix));
                            break;
                        case 4:
                            printMatrix(transposeHorizontalLine(matrix));
                            break;
                        default:
                            break;
                    }
                    break;
                }
                case 5: {
                    System.out.println("Enter matrix size: ");
                    double[][] matrix = initialiseMatrix();
                    System.out.println("The result is:");
                    System.out.println(calculateDeterminate(matrix));
                    break;
                }
                case 6: {
                    System.out.println("Enter matrix size: ");
                    double[][] matrix = initialiseMatrix();
                    System.out.println("The result is:");
                    printMatrix(findReverse(matrix));
                }
                default:
                    break;
            }
            System.out.println();
        } while (choice != 0);
    }

    private static double[][] initialiseMatrix() {
        Scanner scanner = new Scanner(System.in);
        double[][] matrix = new double[scanner.nextInt()][scanner.nextInt()];
        System.out.println("Enter matrix:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = scanner.nextDouble();
            }
        }
        return matrix;
    }

    private static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            System.out.print(row[0]);
            for (int j = 1; j < matrix[0].length; j++) {
                System.out.print(" " + row[j]);
            }
            System.out.println();
        }
    }

    private static double[][] addMatrix(double[][] matrix1, double[][] matrix2) {
//        check matrix size
        if (matrix1.length != matrix2.length || matrix1[0].length != matrix2[0].length) {
            System.out.println("The operation cannot be performed.");
            throw new IndexOutOfBoundsException("Size first doesn't equal size second");
        }

        double[][] result = new double[matrix1.length][matrix1[0].length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return result;
    }

    private static double[][] multiplyConst(double[][] matrix, double constant) {
        double[][] result = matrix.clone();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] *= constant;
            }
        }
        return result;
    }

    private static double[][] multiplyMatrix(double[][] matrix1, double[][] matrix2) {
        if (matrix1[0].length != matrix2.length) {
            System.out.println("The operation cannot be performed.");
            throw new IndexOutOfBoundsException("Number columns of first matrix doesn't equal number rows of second matrix");
        }

        double[][] result = new double[matrix1.length][matrix2[0].length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                for (int m = 0; m < matrix2.length; m++) {
                    result[i][j] += matrix1[i][m] * matrix2[m][j];
                }
            }
        }
        return result;
    }

    private static double[][] transposeMainDiag(double[][] matrix) {
        double[][] result = matrix.clone();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix[0].length; j++) {
                double temp = result[i][j];
                result[i][j] = result[j][i];
                result[j][i] = temp;
            }
        }
        return result;
    }

    private static double[][] transposeSideDiag(double[][] matrix) {
        double[][] result = matrix.clone();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length - i; j++) {
                double temp = result[i][j];
                result[i][j] = result[matrix.length - 1 - j][matrix.length - 1 - i];
                result[matrix.length - 1 - j][matrix.length - 1 - i] = temp;
            }
        }
        return result;
    }

    private static double[][] transposeVerticalLine(double[][] matrix) {
        double[][] result = matrix.clone();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length / 2; j++) {
                double temp = result[i][j];
                result[i][j] = result[i][matrix[0].length - j - 1];
                result[i][matrix[0].length - j - 1] = temp;
            }
        }
        return result;
    }

    private static double[][] transposeHorizontalLine(double[][] matrix) {
        double[][] result = matrix.clone();
        for (int i = 0; i < matrix.length / 2; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                double temp = result[i][j];
                result[i][j] = result[matrix.length - i - 1][j];
                result[matrix.length - i - 1][j] = temp;
            }
        }
        return result;
    }

    private static double calculateDeterminate(double[][] matrix) {
        double result = 0;
        if (matrix.length == 1) {
            return matrix[0][0];
        }
        for (int i = 0; i < matrix.length; i++) {
            double[][] newMatrix = new double[matrix.length - 1][matrix.length - 1];
            for (int j = 0; j < matrix.length - 1; j++) {
                for (int k = 0; k < matrix.length - 1; k++) {
                    if (k >= i) {
                        newMatrix[j][k] = matrix[j + 1][k + 1];
                    } else {
                        newMatrix[j][k] = matrix[j + 1][k];
                    }
                }
            }
            result += matrix[0][i] * Math.pow(-1, i) * calculateDeterminate(newMatrix);
        }
        return result;
    }

    private static double[][] findReverse(double[][] matrix) {
        double[][] matrixCofactors = new double[matrix.length][matrix.length];
        for (int i = 0; i < matrixCofactors.length; i++) {
            for (int j = 0; j < matrixCofactors.length; j++) {
                matrixCofactors[i][j] = calculateDeterminate(getMinorMatrix(matrix, i, j)) * Math.pow(-1, i + j);
            }
        }
        return multiplyConst(transposeMainDiag(matrixCofactors), 1 / calculateDeterminate(matrix));
    }

    private static double[][] getMinorMatrix(double[][] matrix, int i, int j) {
        double[][] newMatrix = new double[matrix.length - 1][matrix.length - 1];
        for (int m = 0; m < matrix.length - 1; m++) {
            for (int k = 0; k < matrix.length - 1; k++) {
                if (k >= j) {
                    if (m >= i) {
                        newMatrix[m][k] = matrix[m + 1][k + 1];
                    } else {
                        newMatrix[m][k] = matrix[m][k + 1];
                    }
                } else {
                    if (m >= i) {
                        newMatrix[m][k] = matrix[m + 1][k];
                    } else {
                        newMatrix[m][k] = matrix[m][k];
                    }
                }
            }
        }
        return newMatrix;
    }
}
