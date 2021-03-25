import java.util.NoSuchElementException;
import java.util.Scanner;

public class Converter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int sourceRadix;
        String sourceNum;
        int targetRadix;
        try {
            sourceRadix = scanner.nextInt();
            sourceNum = scanner.next();
            targetRadix = scanner.nextInt();
        } catch (NoSuchElementException e) {
            System.out.println("error. you're idiot.");
            return;
        }
        if (sourceRadix > 36 || targetRadix > 36 || sourceRadix < 1 || targetRadix < 1) {
            System.out.println("error. you're idiot.");
            return;
        }
        if (sourceNum.contains(".")) {
            System.out.println(processInteger(sourceRadix, sourceNum.substring(0, sourceNum.indexOf(".")), targetRadix)
                    + processFraction(sourceRadix, sourceNum.substring(sourceNum.indexOf(".") + 1), targetRadix));
        } else {
            System.out.println(processInteger(sourceRadix, sourceNum, targetRadix));
        }
    }

    public static String processInteger(int sourceRadix, String sourceNum, int targetRadix) {
        int decimalNum;
        String result;
        if (sourceRadix == 1) {
            decimalNum = sourceNum.length();
        } else {
            decimalNum = Integer.parseInt(sourceNum, sourceRadix);
        }
        if (targetRadix == 1) {
            result = "1".repeat(decimalNum);
        } else {
            result = Integer.toString(decimalNum, targetRadix);
        }
        return result;
    }


    public static String processFraction(int sourceRadix, String sourceNum, int targetRadix) {
        double decimalNum = 0;
        StringBuilder result = new StringBuilder(".");
        if (sourceRadix != 10) {
            for (int i = 0; i < sourceNum.length(); i++) {
                if (sourceNum.charAt(i) >= 'a') {
                    decimalNum += (sourceNum.charAt(i) - 'a' + 10) / Math.pow(sourceRadix, i + 1);
                } else {
                    decimalNum += (sourceNum.charAt(i) - '0') / Math.pow(sourceRadix, i + 1);
                }
            }
        } else {
            decimalNum = Double.parseDouble("0." + sourceNum);
        }
        if (targetRadix != 10) {
            for (int i = 0; i < 5; i++) {
                decimalNum *= targetRadix;
                if ((int) decimalNum > 9) {
                    result.append((char) ('a' + (int) decimalNum - 10));
                } else {
                    result.append((int) decimalNum);
                }
                decimalNum -= (int) decimalNum;
            }
        }
        return result.toString();
    }

}
