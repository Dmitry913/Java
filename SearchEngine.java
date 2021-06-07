package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SearchEngine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Searcher searcher = new Searcher();
        searcher.setData(args[1]);
        String input;
        do {
            searcher.printMenu();
            input = scanner.nextLine();
            switch (input) {
                case "1":
                    searcher.doSearch();
                    break;
                case "2":
                    searcher.printData();
                    break;
                case "0":
                    System.out.println("Bye!");
                    break;
                default:
                    System.out.println("Incorrect option! Try again.");
            }
        } while (!input.equals("0"));
    }
}

interface SearchingMethod {
    Set<Integer> search(String words, Map<String, Set<Integer>> data);
}

class AllSearchingMethod implements SearchingMethod {
    @Override
    public Set<Integer> search(String words, Map<String, Set<Integer>> data) {
        String[] separateWords = words.split(" ");
        Set<Integer> result = data.getOrDefault(separateWords[0], new HashSet<>());
        for (int i = 1; i < separateWords.length; i++) {
            result.retainAll(data.getOrDefault(separateWords[i], Set.of()));
        }
        return result;
    }
}

class AnySearchingMethod implements SearchingMethod {
    @Override
    public Set<Integer> search(String words, Map<String, Set<Integer>> data) {
        Set<Integer> result = new HashSet<>();
        for (String elem : words.split(" ")) {
            result.addAll(data.get(elem));
        }
        return result;
    }
}

class NoneSearchingMethod implements SearchingMethod {
    @Override
    public Set<Integer> search(String words, Map<String, Set<Integer>> data) {
        Set<Integer> result = new HashSet<>();
        for (Set<Integer> lines : data.values()) {
            result.addAll(lines);
        }
        for (String word : words.split(" ")) {
            result.removeAll(data.get(word));
        }
        return result;
    }
}

class Searcher {
    String[] data;
    private final Scanner scanner;
    private final Map<String, Set<Integer>> correspond;
    private SearchingMethod method;

    public Searcher() {
        scanner = new Scanner(System.in);
        correspond = new HashMap<>();
    }

    public void setData(String fileName) {
        try (Scanner scannerFile = new Scanner(new File(fileName))) {
            StringBuilder readAllFile = new StringBuilder();
            while (scannerFile.hasNext()) {
                readAllFile.append(scannerFile.nextLine()).append("\n");
            }
            data = readAllFile.toString().split("\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setCorrespond();
    }

    private void setCorrespond() {
        for (int i = 0; i < data.length; i++) {
            for (String word : data[i].toLowerCase().split(" ")) {
                correspond.putIfAbsent(word, new HashSet<>());
                correspond.get(word).add(i);
            }
        }
    }

    public void printMenu() {
        System.out.println("=== Menu ===\n" +
                "1. Find a person\n" +
                "2. Print all people\n" +
                "0. Exit");
    }

    public void doSearch() {
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        this.method = methodFactory(scanner.nextLine());
        System.out.println("Enter data to search people:");
        String words = scanner.nextLine().toLowerCase();
        Set<Integer> concurrence = method.search(words, correspond);
        if (concurrence.size() == 0) {
            System.out.println("No matching people found");
        } else {
            System.out.printf("%d person%s found:%n", concurrence.size(), (concurrence.size() == 1) ? "" : "s");
            for (Integer index : concurrence) {
                System.out.println(data[index]);
            }
        }
    }

    private SearchingMethod methodFactory(String type) {
        switch (type) {
            case "ANY":
                return new AnySearchingMethod();
            case "ALL":
                return new AllSearchingMethod();
            case "NONE":
                return new NoneSearchingMethod();
            default:
                return null;
        }
    }

    public void printData() {
        System.out.println("=== List of people ===");
        for (String elem : data) {
            System.out.println(elem + "\n");
        }
    }
}



