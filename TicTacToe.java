import java.util.Arrays;
import java.util.Scanner;

public class TicTacToe {
    public static void main(String[] args) {
        char[] field = new char[9];
        Arrays.fill(field, '_');

        while (true) {
            printField(field);
            if (checkWin(field, 'X')) {
                System.out.println("X wins");
                break;
            } else if (checkWin(field, 'O')) {
                System.out.println("O wins");
                break;
            } else if (countSymbol(field, '_') == 0) {
                System.out.println("Draw");
                break;
            }
            getCoordinate(field);
        }
    }

    static boolean checkWin(char[] array, char elemChecking) {
        if (array[0] == elemChecking && array[4] == elemChecking && array[8] == elemChecking) {
            return true;
        } else {
            int numElemDig = 0;
            int numElemSideDig = 0;
            for (int i = 0; i < 3; i++) {
                int numElemRow = 0;
                int numElemColumn = 0;
                for (int j = 0; j < 3; j++) {
                    if (array[i * 3 + j] == elemChecking) {
                        numElemRow++;
                    }
                    if (array[i + 3 * j] == elemChecking) {
                        numElemColumn++;
                    }
                }
                if (array[4 * i] == elemChecking) {
                    numElemDig++;
                }
                if (array[2 * i] == elemChecking) {
                    numElemSideDig++;
                }
                if (numElemRow == 3 || numElemColumn == 3) {
                    return true;
                }
            }
            return (numElemDig == 3 || numElemSideDig == 3);
        }
    }

    static int countSymbol (char[] array, char symbol) {
        int counter = 0;
        for (char elem : array) {
            if (elem == symbol) {
                counter++;
            }
        }
        return counter;
    }

    static void printField (char[] array) {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(array[i * 3 + j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    static void getCoordinate(char[] array) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the coordinates: ");
        while (true) {
            String temp = scanner.next();
            if (!temp.matches("[-+]?\\d+")) {
                System.out.println("You should enter numbers!");
            } else {
                int first = Integer.parseInt(temp);
                int second = scanner.nextInt();
                if (!(second > 0 && second < 4 & first > 0 && first < 4)) {
                    System.out.println("Coordinates should be from 1 to 3!");
                } else if (array[(first - 1) * 3 + second - 1] != '_') {
                    System.out.println("This cell is occupied! Choose another one!");
                } else {
                    array[(first - 1) * 3 + second - 1] = 'X';
                    break;
                }
            }
        }
    }
}
