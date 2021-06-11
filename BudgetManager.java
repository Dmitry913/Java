package budget;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

//public class Main {
//    public static void main(String[] args) {
//        Manager budgetManager = new BudgetManager();
//        budgetManager.startWork();
//    }
//}

abstract class Manager {
    protected Scanner scanner;
    protected Map<String, List<String>> data;

    abstract void printActions();

    abstract void startWork();
}

public class BudgetManager extends Manager {
    
    public static void main(String[] args) {
        Manager budgetManager = new BudgetManager();
        budgetManager.startWork();
    }
    
    private double balance = 0;
    public static final Map<Integer, String> CATEGORIES = Map.of(1, "Food", 2, "Clothes", 3, "Entertainment", 4, "Other");
    private static final File FILE = new File("purchases.txt");

    @Override
    void printActions() {
        System.out.println("Choose your action:\n" +
                "1) Add income\n" +
                "2) Add purchase\n" +
                "3) Show list of purchases\n" +
                "4) Balance\n" +
                "5) Save\n" +
                "6) Load\n" +
                "7) Analyze (Sort)\n" +
                "0) Exit");
    }

    @Override
    public void startWork() {
        data = new HashMap<>();
        scanner = new Scanner(System.in);
        int action;
        do {
            printActions();
            action = Integer.parseInt(scanner.nextLine());
            switch (action) {
                case 1:
                    addIncome();
                    break;
                case 2:
                    addPurchase();
                    break;
                case 3:
                    printPurchases();
                    break;
                case 4:
                    printBalance();
                    break;
                case 5:
                    saveToFile();
                    break;
                case 6:
                    loadFromFile();
                    break;
                case 7:
                    analyze();
                    break;
                case 0:
                    System.out.println("\nBye!");
                    break;
                default:
                    System.out.println("ERROR: Choose correct action");
            }
        } while (action != 0);
    }

    private void analyze() {
        System.out.println("\nHow do you want to sort?\n" +
                "1) Sort all purchases\n" +
                "2) Sort by type\n" +
                "3) Sort certain type\n" +
                "4) Back");
        int action = Integer.parseInt(scanner.nextLine());
        List<String> sortedList;
        if (action == 1 && data.isEmpty()) {
            System.out.println("\nThe purchase list is empty!");
        } else {
            BubbleSort bubbleSort = createBubbleSort(action);
            if (bubbleSort == null) {
                System.out.println();
                return;
            } else {
                sortedList = bubbleSort.sort(data);
                if (sortedList == null) {
                    System.out.println("\nThe purchase list is empty!");
                } else {
                    System.out.println("Total sum: $" + String.format("%.2f", calculateCountMoney(sortedList)));
                }
            }
        }
        analyze();
    }

    //factory method
    private BubbleSort createBubbleSort(Integer typeSort) {
        switch (typeSort) {
            case 1:
                System.out.println("\nAll:");
                return new AllBubbleSort();
            case 2:
                System.out.println("\nTypes:");
                return new TypesBubbleSort();
            case 3:
                return new ByTypeBubbleSort();
            case 4:
                return null;
            default:
                //плохо делать обработку некорректного ввода на стеке
                System.out.println("Please choose correct action");
                analyze();
                return null;
        }
    }

    private void addIncome() {
        System.out.println("\nEnter income:");
        balance += Double.parseDouble(scanner.nextLine());
        System.out.println("Income as added!\n");
    }

    private void saveToFile() {
        try {
            FileWriter fileWriter = new FileWriter(FILE);
            for (String type : CATEGORIES.values()) {
                List<String> purchases = data.getOrDefault(type, List.of());
                fileWriter.write(type + " " + purchases.size() + '\n');
                for (String purchase : purchases) {
                    fileWriter.write(purchase + '\n');
                }
            }
            fileWriter.write("balance=" + balance);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error: writing in file failed");
        }
        System.out.println("\nPurchases were saved!\n");
    }

    private void loadFromFile() {
        try {
            Scanner readingScanner = new Scanner(FILE);
            while (readingScanner.hasNext()) {
                String typeAndCount = readingScanner.nextLine();
                if (typeAndCount.contains("balance")) {
                    balance = Double.parseDouble(typeAndCount.substring(typeAndCount.indexOf("=") + 1));
                    continue;
                }
                List<String> purchases = new LinkedList<>();
                for (int i = 0; i < Integer.parseInt(typeAndCount.substring(typeAndCount.indexOf(' ') + 1)); i++) {
                    purchases.add(readingScanner.nextLine());
                }
                data.put(typeAndCount.substring(0, typeAndCount.indexOf(' ')), purchases);
            }
            readingScanner.close();
        } catch (IOException e) {
            System.out.println("Error: reading info from file failed");
        }
        System.out.println("\nPurchases were loaded!\n");
    }

    private void printBalance() {
        System.out.println("\nBalance: $" + balance + "\n");
    }

    private void printPurchases() {
        System.out.println("\nChoose the type of purchases\n" +
                "1) Food\n" +
                "2) Clothes\n" +
                "3) Entertainment\n" +
                "4) Other\n" +
                "5) All\n" +
                "6) Back");
        int typePurchase = Integer.parseInt(scanner.nextLine());
        System.out.println();
        if (typePurchase == 6) {
            return;
        } else if (typePurchase == 5) {
            System.out.println("All:");
            if (data.isEmpty()) {
                System.out.println("The purchase list is empty");
            } else {
                double countMoney = 0;
                for (String type : CATEGORIES.values()) {
                    countMoney += calculateCountMoney(data.getOrDefault(type, List.of()));
                }
                System.out.println("Total sum:" + countMoney + "\n");
            }
        } else if (CATEGORIES.containsKey(typePurchase)) {
            System.out.println(CATEGORIES.get(typePurchase));
            List<String> purchase = data.getOrDefault(CATEGORIES.get(typePurchase), List.of());
            if (purchase.isEmpty()) {
                System.out.println("The purchase list is empty");
            } else {
                double countMoney = calculateCountMoney(purchase);
                System.out.println("Total sum: " + countMoney + "\n");
            }
        }
        printPurchases();
    }

    private double calculateCountMoney(List<String> purchase) {
        double countMoney = 0;
        for (String line : purchase) {
            double money = Double.parseDouble(line.substring(line.lastIndexOf('$') + 1));
            countMoney += money;
            System.out.println(line.substring(0, line.lastIndexOf("$") + 1) + String.format("%.2f", money));
        }
        return countMoney;
    }

    private void addPurchase() {
        System.out.println("\nChoose the type of purchase\n" +
                "1) Food\n" +
                "2) Clothes\n" +
                "3) Entertainment\n" +
                "4) Other\n" +
                "5) Back");
        Integer typeCategories = Integer.parseInt(scanner.nextLine());
        System.out.println();
        if (CATEGORIES.containsKey(typeCategories)) {
            String purchase = getNamePurchase();
            addPurchaseInList(CATEGORIES.get(typeCategories), purchase);
            System.out.println("Purchase was added!");
            addPurchase();
        }
    }

    private String getNamePurchase() {
        StringBuilder addedString = new StringBuilder();
        System.out.println("Enter purchase name");
        addedString.append(scanner.nextLine());
        System.out.println("Enter its price:");
        double costPurchase = Double.parseDouble(scanner.nextLine());
        balance -= costPurchase;
        addedString.append(" $").append(costPurchase);
        return addedString.toString();
    }

    private void addPurchaseInList(String nameCategory, String purchase) {
        data.putIfAbsent(nameCategory, new LinkedList<>());
        data.get(nameCategory).add(purchase);
    }
}

interface BubbleSort {

    default Map<String, Double> createMapPurchasePrice(List<String> data) {
        Map<String, Double> purchasePrice = new HashMap<>();
        for (String elem : data) {
            purchasePrice.put(elem, Double.parseDouble(elem.substring(elem.lastIndexOf("$") + 1)));
        }
        return purchasePrice;
    }

    default void sortSpecialList(List<String> data) {
        Map<String, Double> mapForSorting = createMapPurchasePrice(data);
        int countSwap = 1;
        while (countSwap != 0) {
            countSwap = 0;
            for (int i = 0; i < data.size() - 1; i++) {
                if (mapForSorting.get(data.get(i)) < mapForSorting.get(data.get(i + 1))) {
                    countSwap++;
                    String temp = data.get(i);
                    data.set(i, data.get(i + 1));
                    data.set(i + 1, temp);
                }
            }
        }
    }

    List<String> sort(Map<String, List<String>> data);
}

class AllBubbleSort implements BubbleSort {
    @Override
    public List<String> sort(Map<String, List<String>> data) {
        List<String> listForSort = new LinkedList<>();
        for (String namePurchase : data.keySet()) {
            listForSort.addAll(data.get(namePurchase));
        }
        sortSpecialList(listForSort);
        return listForSort;
    }
}

class TypesBubbleSort implements BubbleSort {

    @Override
    public List<String> sort(Map<String, List<String>> data) {
        List<String> listForSort = new ArrayList<>();
        double countPriceType = 0;
        for (int i = 1; i < 5; i++) {
            String types = BudgetManager.CATEGORIES.get(i);
            for (Double price : createMapPurchasePrice(data.getOrDefault(types, new LinkedList<>())).values()) {
                countPriceType += price;
            }
            listForSort.add(types + " - $" + String.format("%.2f", countPriceType));
            countPriceType = 0;
        }
        sortSpecialList(listForSort);
        return listForSort;
    }
}

class ByTypeBubbleSort implements BubbleSort {

    @Override
    public List<String> sort(Map<String, List<String>> data) {
        System.out.println("\nChoose the type of purchase\n" +
                "1) Food\n" +
                "2) Clothes\n" +
                "3) Entertainment\n" +
                "4) Other");
        Scanner scanner = new Scanner(System.in);
        String type = BudgetManager.CATEGORIES.get(Integer.parseInt(scanner.nextLine()));
        List<String> listForSort = data.get(type);
        if (listForSort == null) {
            return null;
        }
        sortSpecialList(listForSort);
        System.out.println("\n" + type + ":");
        return listForSort;
    }
}
