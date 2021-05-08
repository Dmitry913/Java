package encryptdecrypt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CodeAndDecode {
    public static void main(String[] args) {
        Coder coder = new Coder(args);
        coder.doOutput(coder.doTransformation());
    }
}

class Coder {
    int key;
    String mode;
    String data;
    String fileIn;
    String fileOut;
    Algorithm algorithm;

    {
        key = 0;
        mode = "enc";
        data = "";
        fileIn = "";
        fileOut = "";
    }

    public Coder(String[] args) {
        String fileIn = "";
        String alg = "";
        for (int i = 0; i < args.length / 2; i++) {
            if ("-mode".equals(args[i * 2])) {
                this.mode = args[i * 2 + 1];
            } else if ("-key".equals(args[i * 2])) {
                this.key = Integer.parseInt(args[i * 2 + 1]);
            } else if ("-data".equals(args[i * 2])) {
                this.data = args[i * 2 + 1];
            } else if ("-in".equals(args[i * 2])) {
                fileIn = args[i * 2 + 1];
            } else if ("-out".equals(args[i * 2])) {
                this.fileOut = args[i * 2 + 1];
            } else if ("-alg".equals(args[i * 2])) {
                alg = args[i * 2 + 1];
            }
        }
        algorithmFactory(alg);
        try (Scanner scanner = new Scanner(new File(fileIn))) {
            this.data = scanner.nextLine();
        } catch (IOException e) {
            this.data = "";
        }
    }

    /**
     * factory method
     */
    private void algorithmFactory(String alg) {
        this.algorithm = new Algorithm();
        if ("unicode".equals(alg)) {
            algorithm.setCoding(new UnicodeCoding());
        } else {
            algorithm.setCoding(new ShiftCoding());
        }
    }

    public String doTransformation() {
        if ("dec".equals(mode)) {
            return algorithm.decode(data, key);
        } else {
            return algorithm.encode(data, key);
        }
    }

    public void doOutput(String outData) {
        try (FileWriter writer = new FileWriter(fileOut)) {
            writer.write(outData);
        } catch (IOException e) {
            System.out.println(outData);
        }
    }
}

/**
 * pattern Strategy
 */
class Algorithm {
    Coding coding;

    public void setCoding(Coding coding) {
        this.coding = coding;
    }

    public String encode(String data, int key) {
        return this.coding.encode(data, key);
    }

    public String decode(String data, int key) {
        return this.coding.decode(data, key);
    }
}

//Basic interface for coding
interface Coding {

    String encode(String data, int key);

    String decode(String data, int key);
}

class ShiftCoding implements Coding {
    @Override
    public String encode(String data, int key) {
        StringBuilder result = new StringBuilder();
        for (char character : data.toCharArray()) {
            if (character >= 'a' && character <= 'z') {
                result.append((char) ('a' + (character - 'a' + key) % 26));
            } else if (character >= 'A' && character <= 'Z') {
                result.append((char) ('A' + (character - 'A' + key) % 26));
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    @Override
    public String decode(String data, int key) {
        StringBuilder result = new StringBuilder();
        for (char character : data.toCharArray()) {
            if (character >= 'a' && character <= 'z') {
                int decodeSymbol = character - key;
                if (decodeSymbol < 'a') {
                    decodeSymbol += 26;
                }
                result.append((char) decodeSymbol);
            } else if (character >= 'A' && character <= 'Z') {
                int decodeSymbol = character - key;
                if (decodeSymbol < 'A') {
                    decodeSymbol += 26;
                }
                result.append((char) decodeSymbol);
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

}

class UnicodeCoding implements Coding {

    @Override
    public String encode(String data, int key) {
        StringBuilder result = new StringBuilder();
        for (char character : data.toCharArray()) {
            result.append((char) (character + key));
        }
        return result.toString();
    }

    @Override
    public String decode(String data, int key) {
        StringBuilder result = new StringBuilder();
        for (char character : data.toCharArray()) {
            result.append((char) (character - key));
        }
        return result.toString();
    }
}
