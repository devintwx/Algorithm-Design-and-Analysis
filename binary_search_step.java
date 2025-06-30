import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class binary_search_step {

    /**
     * Reads data from a CSV file into a list of DataPair objects.
     * Assumes the CSV is sorted by the integer value for binary search.
     *
     * @param filename The path to the CSV file.
     * @return A list of DataPair objects.
     * @throws IOException If an I/O error occurs.
     */
    private static List<DataPair> readDataFromFile(String filename) throws IOException {
        List<DataPair> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    try {
                        int value = Integer.parseInt(parts[0].trim());
                        String text = parts[1].trim();
                        data.add(new DataPair(value, text));
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping malformed line (non-integer value): " + line);
                    }
                } else {
                    System.err.println("Skipping malformed line (incorrect number of parts): " + line);
                }
            }
        }
        return data;
    }

    /**
     * Performs a binary search on the list of DataPair objects and records each
     * step.
     *
     * @param data   The sorted list of DataPair objects.
     * @param target The integer target value to search for.
     * @param writer The PrintWriter to write the search steps to.
     * @return The index of the target if found, otherwise -1.
     * @throws IOException If an I/O error occurs while writing steps.
     */
    public static int binarySearchStep(List<DataPair> data, int target, PrintWriter writer) throws IOException {
        int low = 0;
        int high = data.size() - 1;
        int targetIndex = -1;

        writer.println("Searching for target: " + target);
        writer.print("[");
        for (int i = 0; i < data.size(); i++) {
            writer.print(data.get(i).toString());
            if (i < data.size() - 1) {
                writer.print(", ");
            }
        }
        writer.println("]");

        while (low <= high) {
            int mid = low + (high - low) / 2;
            DataPair current = data.get(mid);

            writer.println((mid + 1) + ": " + current.toString());

            if (current.getValue() == target) {
                targetIndex = mid;
                break;
            } else if (current.getValue() < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        if (targetIndex == -1) {
            writer.println("-1"); // Target not found
        }
        return targetIndex;
    }

    public static void main(String[] args) {
        String datasetFilename = "src/quick_sort_1000_java.csv"; // Default path to dataset
        int target;

        if (args.length != 1) {
            System.out.println("Please enter the target number to search:");
            Scanner sc = new Scanner(System.in);
            try {
                target = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.err.println("Error: Target must be an integer.");
                sc.close();
                return;
            }
            sc.close();
        } else {
            try {
                target = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Error: Target must be an integer.");
                return;
            }
        }

        try {
            List<DataPair> data = readDataFromFile(datasetFilename);
            if (data.isEmpty()) {
                System.out.println("Dataset is empty or could not be read. Cannot perform binary search.");
                return;
            }

            String outputFilename = "src/binary_search_step_" + target + ".txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilename))) {
                long startTime = System.nanoTime();
                binarySearchStep(data, target, writer);
                long endTime = System.nanoTime();
                System.out.println("Search steps saved to " + outputFilename);
            }
        } catch (IOException e) {
            System.err.println("An I/O error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

/**
 * Inner class to represent a data pair of integer and string.
 */
class DataPair {
    private final int value;
    private final String label;

    public DataPair(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "(" + value + ", " + label + ")";
    }
}
