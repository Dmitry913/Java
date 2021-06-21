package numbers;

import java.util.*;

public class AmazingNumberMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Amazing Numbers!\n");
        printInstruction();
        AmazingNumber amazingNumber = new AmazingNumber();
        do {
            System.out.println("Enter a request: ");
        } while (amazingNumber.processInput(scanner.nextLine()));
        System.out.println("Goodbye!");
    }

    private static void printInstruction() {
        System.out.println("Supported requests:\n" +
                "- enter a natural number to know its properties;\n" +
                "- enter two natural numbers to obtain the properties of the list:\n" +
                "  * the first parameter represents a starting number;\n" +
                "  * the second parameter shows how many consecutive numbers are to be printed;\n" +
                "- two natural numbers and properties to search for;\n" +
                "- a property preceded by minus must not be present in numbers;\n" +
                "- separate the parameters with one space;\n" +
                "- enter 0 to exit.");
    }
}

class AmazingNumber {
    enum State {
        ONENUMBER, LISTNUMBER, PROPERITIESNUMBER, EXIT
    }

    private State state;


    public boolean processInput(String input) {
        setState(input);
        if (isValidInput(input)) {
            switch (state) {
                case EXIT:
                    return false;
                case ONENUMBER:
                    printSoloProperties(new Number(Long.parseLong(input)));
                    break;
                case LISTNUMBER:
                    long startNumber = Long.parseLong(input.substring(0, input.indexOf(" ")));
                    long range = Long.parseLong(input.substring(input.indexOf(" ") + 1));
                    for (long i = 0; i < range; i++) {
                        printProperties(new Number(startNumber + i));
                    }
                    break;
                case PROPERITIESNUMBER:
                    String[] array = input.toLowerCase().split(" ");
                    startNumber = Long.parseLong(array[0]);
                    range = Long.parseLong(array[1]);
                    Set<String> includeProperties = new HashSet<>();
                    Set<String> excludeProperties = new HashSet<>();
                    for (int i = 2; i < array.length; i++) {
                        if (array[i].contains("-")) {
                            excludeProperties.add(array[i].substring(1));
                        } else {
                            includeProperties.add(array[i]);
                        }
                    }
                    int i = 0;
                    while (range != 0) {
                        Number number = new Number(startNumber + i++);
                        if (number.getProperties().containsAll(includeProperties) && (excludeProperties.isEmpty() || !number.getProperties().containsAll(excludeProperties))) {
                            printProperties(number);
                            range--;
                        }
                    }
                    break;
            }
        }
        System.out.println();
        return true;
    }

    private boolean isValidInput(String input) {
        String[] array = input.split(" ");
        //check first number
        if (Long.parseLong(array[0]) < 0) {
            System.out.println("The first parameter should be a natural number or zero.");
            return false;
        }
        //check second number
        if (array.length > 1 && Long.parseLong(array[1]) <= 0) {
            System.out.println("The second parameter should be a natural number or zero.");
            return false;
        }
        //below both checking can be done with "set"
        //check valid all properties
        if (array.length > 2) {
            StringBuilder wrongProperty = new StringBuilder();
            for (int i = 2; i < array.length; i++) {
                String property;
                if (array[i].contains("-")) {
                    property = array[i].substring(1);
                } else {
                    property = array[i];
                }
                if (!Number.ALLPROPERTIES.contains(property.toLowerCase())) {
                    wrongProperty.append(array[i]).append(" ");
                }
            }
            if (!"".contentEquals(wrongProperty)) {
                String stringForInput = wrongProperty.toString().trim().replace(" ", ", ");
                System.out.printf("The propert%s [%s] %s wrong.\n" +
                                "Available properties:\n" +
                                "[EVEN, ODD, BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, JUMPING, HAPPY, SAD]",
                        stringForInput.contains(" ") ? "ies" : "y", stringForInput, stringForInput.contains(" ") ? "are" : "is");
                return false;
            }
        }
        //check exclusive properties
        String exclusiveProperties;
        Set<String> includeProperties = new HashSet<>();
        Set<String> excludeProperties = new HashSet<>();
        for (int i = 2; i < array.length; i++) {
            if (array[i].contains("-")) {
                excludeProperties.add(array[i].substring(1));
            } else {
                includeProperties.add(array[i]);
            }
        }
        Set<String> intersection = new HashSet<>(includeProperties);
        intersection.retainAll(excludeProperties);
        if (!intersection.isEmpty()) {
            exclusiveProperties = includeProperties.toString();
            exclusiveProperties = exclusiveProperties.substring(1, exclusiveProperties.length() - 1);
            exclusiveProperties = exclusiveProperties + ", -" + exclusiveProperties;
        } else {
            exclusiveProperties = checkExclusiveProperties(includeProperties);
            if (exclusiveProperties.isEmpty()) {
                exclusiveProperties = checkExclusiveProperties(excludeProperties);
            }
        }
        if (!exclusiveProperties.isEmpty()) {
            System.out.printf("The request contains mutually exclusive properties: [%s]\n" +
                    "There are no numbers with these properties.", exclusiveProperties);
            return false;
        }
        return true;
    }

    private String checkExclusiveProperties(Set<String> properties) {
        String result = "";
        if (properties.contains("even") && properties.contains("odd")) {
            result = "ODD, EVEN";
        } else if (properties.contains("duck") && properties.contains("spy")) {
            result = "DUCK, SPY";
        } else if (properties.contains("square") && properties.contains("sunny")) {
            result = "SQUARE, SUNNY";
        } else if (properties.contains("happy") && properties.contains("sad")) {
            result = "HAPPY, SAD";
        }
        return result;
    }

    private void setState(String input) {
        String[] array = input.split(" ");
        if (array.length >= 3) {
            state = State.PROPERITIESNUMBER;
        } else if (array.length == 2) {
            state = State.LISTNUMBER;
        } else {
            if (input.equals("0")) {
                state = State.EXIT;
            } else {
                state = State.ONENUMBER;
            }
        }
    }


    private void printSoloProperties(Number number) {
        System.out.println("Properties of " + number.getValue());
        Set<String> properties = number.getProperties();
        for (String property : Number.ALLPROPERTIES) {
            System.out.printf("\t\t%s: %b\n", property, properties.contains(property));
        }
        System.out.println();
    }

    private void printProperties(Number number) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String propertyNumber : number.getProperties()) {
            stringBuilder.append(propertyNumber).append(", ");
        }
        stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "");
        System.out.println("\t\t" + number.getValue() + " is " + stringBuilder.toString());
    }


}


class Number {
    final static Set<String> ALLPROPERTIES = Set.of("jumping", "even", "odd", "duck", "gapful",
            "buzz", "spy", "palindromic", "square", "sunny", "happy", "sad");

    private final long value;
    private final Map<String, Boolean> properties;

    public Number(long value) {
        properties = new HashMap<>();
        this.value = value;
        properties.put("even", value % 2 == 0);
        properties.put("odd", value % 2 != 0);
        properties.put("duck", isDuck());
        properties.put("gapful", isGapful());
        properties.put("buzz", isBuzz());
        properties.put("spy", isSpy());
        properties.put("palindromic", isPalindromic());
        properties.put("square", isSquare());
        properties.put("sunny", isSunny());
        properties.put("jumping", isJumping());
        properties.put("happy", isHappy());
        properties.put("sad", !properties.get("happy"));
    }

    // check properties
    private boolean isSquare() {
        return Math.sqrt(value) % 1 == 0;
    }

    private boolean isSunny() {
        return Math.sqrt(value + 1) % 1 == 0;
    }

    private boolean isSpy() {
        long sumDigit = 0;
        long productDigit = 1;
        String stringNumber = String.valueOf(value);
        for (char digit : stringNumber.toCharArray()) {
            sumDigit += digit - '0';
            productDigit *= digit - '0';
        }
        return sumDigit == productDigit;
    }

    private boolean isGapful() {
        String stringNumber = String.valueOf(value);
        return (stringNumber.length() > 2) && (value % ((stringNumber.charAt(0) - '0') * 10 + value % 10) == 0);
    }

    private boolean isPalindromic() {
        String stringNumber = String.valueOf(value);
        for (int i = 0; i < stringNumber.length() / 2; i++) {
            if (stringNumber.charAt(i) != stringNumber.charAt(stringNumber.length() - 1 - i)) {
                return false;
            }
        }
        return true;
    }

    private boolean isDuck() {
        return String.valueOf(value).contains("0");
    }

    private boolean isBuzz() {
        return (value / 10 - value % 10 * 2) % 7 == 0 || value % 10 == 7;
    }

    private boolean isJumping() {
        String number = String.valueOf(value);
        for (int i = 0; i < number.length() - 1; i++) {
            if (Math.abs(number.charAt(i) - number.charAt(i + 1)) != 1) {
                return false;
            }
        }
        return true;
    }

    private boolean isHappy() {
        Set<Long> preferValues = new HashSet<>();
        preferValues.add(value);
        long resultNumber = value;
        while (true) {
            String stringNumber = String.valueOf(resultNumber);
            resultNumber = 0;
            for (char digit : stringNumber.toCharArray()) {
                resultNumber += Math.pow(digit - '0', 2);
            }
            if (resultNumber == 1) {
                return true;
            } else if (preferValues.contains(resultNumber)) {
                return false;
            }
            preferValues.add(resultNumber);
        }
    }

    public Set<String> getProperties() {
        Set<String> result = new HashSet<>();
        for (String property : properties.keySet()) {
            if (properties.get(property)) {
                result.add(property);
            }
        }
        return result;
    }

    public long getValue() {
        return value;
    }
}
