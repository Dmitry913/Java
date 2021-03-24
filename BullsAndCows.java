package bullscows;

import java.util.Scanner;
import java.util.Random;

public class BullsAndCows {
    final private char[] secretCode;
    final private int lenCode;

    public Main(int lenCode, int sizeCode) {
        this.lenCode = lenCode;
        secretCode = generateCode(lenCode, sizeCode);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Main game;
        System.out.println("Input the length of the secret code:");
        String input = "";
        try {
            input = scanner.next();
            int lenCode = Integer.parseInt(input);
            if (lenCode > 36 || lenCode < 1) {
                System.out.printf("Error: can't generate a secret number with a " +
                        "length of %d because there aren't enough unique digits.%n", lenCode);
            } else {
                System.out.println("Input the number of possible symbols in the code:");
                input = scanner.next();
                int sizeCode = Integer.parseInt(input);
                if (sizeCode > 36) {
                    System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
                } else if (sizeCode < lenCode) {
                    System.out.printf("Error: it's not possible to generate a code with a length of %d with %d unique symbols.%n", lenCode, sizeCode);
                } else {
                    game = new Main(lenCode, sizeCode);
                    System.out.println("Okay, let's start a game!");
                    int i = 1;
                    do {
                        System.out.printf("Turn %d:%n", i);
                        i++;
                    } while (game.checkCode(scanner.next().toCharArray()));
                    System.out.println("Congratulations! You guessed the secret code.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.printf("Error: \"%s\" isn't a valid number.%n", input);
        }
    }

    private char[] generateCode(int lenCode, int sizeCode) {
        Random random = new Random();
        StringBuilder randString = new StringBuilder();
        while (randString.length() < lenCode) {
            randString.append(random.nextLong());
        }
        randString.reverse();
        StringBuilder symbols = new StringBuilder("0123456789abcdefghijklmnopqrstuvwxyz".substring(0, sizeCode));
        char[] result = new char[lenCode];
        System.out.println(symbols);
        for (int i = 0; i < lenCode; i++) {
            result[i] = (char) ((randString.charAt(i) + i - '0') % 10 + '0');
            int index = (randString.charAt(i) + i - '0') % symbols.length();
            result[i] = symbols.charAt(index);
            symbols.deleteCharAt(index);
        }

        System.out.printf("The secret is prepared: %s (0-%s).%n", "*".repeat(lenCode),
                sizeCode > 10 ? String.format("9, a-%c", 'a' + sizeCode % 26) : Integer.toString(sizeCode - 1));
        return result;
    }


    public boolean checkCode(char[] attempt) {
        int countBull = 0;
        int countCow = 0;


        for (int i = 0; i < secretCode.length; i++) {
            if (secretCode[i] == attempt[i]) {
                countBull++;
            } else {
                for (int j = 1; j < secretCode.length; j++) {
                    if (attempt[i] == secretCode[(i + j) % secretCode.length])
                        countCow++;
                }
            }
        }
        System.out.println("Grade: "
                + (countBull + countCow == 0 ? "None" : String.format("%d bull%s and %d cow%s.",
                countBull, countBull > 2 ? "s" : "", countCow, countCow > 2 ? "s" : "")));
        return countBull != lenCode;
    }
}
