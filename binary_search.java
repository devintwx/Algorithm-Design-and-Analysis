import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class binary_search {

    // --- Start of DataEntry Inner Class Definition ---
    private static class DataEntry {
        private int value;
        private String text;

        public DataEntry(int value, String text) {
            this.value = value;
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }
    // --- End of DataEntry Inner Class Definition ---

    /**
     * Reads data from a CSV file into a list of DataEntry objects.
     * Assumes the CSV is sorted by the integer value.
     *
     * @param filename The path to the CSV file.
     * @return A list of DataEntry objects.
     * @throws IOException If an I/O error occurs.
     */
    private static List<DataEntry> readDataFromFile(String filename) throws IOException {
        List<DataEntry> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    try {
                        int value = Integer.parseInt(parts[0].trim());
                        String text = parts[1].trim();
                        data.add(new DataEntry(value, text));
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
     * Performs a standard binary search to find a target value.
     * This version is optimized for speed, not for step-by-step logging.
     *
     * @param data   The sorted list of DataEntry objects.
     * @param target The integer target value to search for.
     * @return The index of the target if found, otherwise -1.
     */
    private static int performBinarySearch(List<DataEntry> data, int target) {
        int low = 0;
        int high = data.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int currentValue = data.get(mid).getValue();

            if (currentValue == target) {
                return mid; // Target found
            } else if (currentValue < target) {
                low = mid + 1;
            } else { // currentValue > target
                high = mid - 1;
            }
        }
        return -1; // Target not found
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java BinarySearch <dataset_filename.csv>");
            return;
        }

        String datasetFilename = args[0];
        String outputFilename = ""; // To be set after determining n

        try {
            List<DataEntry> data = readDataFromFile(datasetFilename);

            if (data.isEmpty()) {
                System.out.println("Dataset is empty or could not be read. Cannot perform binary search.");
                return;
            }

            int n = data.size(); // Dataset size
            outputFilename = "binary_search_" + n + ".txt"; // Name output file based on n

            // Best Case Targets (n searches):
            long totalBestCaseTime = 0;
            if (n > 0) {
                int midValue = data.get(n / 2).getValue();
                for (int i = 0; i < n; i++) {
                    long startTime = System.nanoTime();
                    performBinarySearch(data, midValue);
                    long endTime = System.nanoTime();
                    totalBestCaseTime += (endTime - startTime);
                }
            }
            double avgBestCaseTimeNanos = (double) totalBestCaseTime / n;

            // Worst Case Targets (n searches):
            long totalWorstCaseTime = 0;
            int notFoundTarget = 0; // Initialize
            if (n > 0) {
                int maxDatasetValue = data.get(n - 1).getValue(); // Assumes sorted data
                // Standard worst-case for an unsuccessful search: a value guaranteed not to be
                // in data
                notFoundTarget = maxDatasetValue + 1;
            }

            for (int i = 0; i < n; i++) {
                long startTime = System.nanoTime();
                performBinarySearch(data, notFoundTarget);
                long endTime = System.nanoTime();
                totalWorstCaseTime += (endTime - startTime);
            }
            double avgWorstCaseTimeNanos = (double) totalWorstCaseTime / n;

            // Average Case Targets (n searches):
            long totalAverageCaseTime = 0;
            Random random = new Random();

            // --- EXCLUDING MIDDLE LOGIC START ---
            // Define a 'middle zone' to exclude from random picks
            // Example: Exclude the central 20% of the dataset
            int exclusionZoneSize = n / 5; // 20% of n
            int exclusionZoneStart = Math.max(0, (n / 2) - (exclusionZoneSize / 2));
            int exclusionZoneEnd = Math.min(n - 1, (n / 2) + (exclusionZoneSize / 2));

            for (int i = 0; i < n; i++) {
                int randomIndex;
                do {
                    randomIndex = random.nextInt(n);
                } while (randomIndex >= exclusionZoneStart && randomIndex <= exclusionZoneEnd);

                int targetValue = data.get(randomIndex).getValue();
                long startTime = System.nanoTime();
                performBinarySearch(data, targetValue);
                long endTime = System.nanoTime();
                totalAverageCaseTime += (endTime - startTime);
            }
            // --- EXCLUDING MIDDLE LOGIC END ---
            double avgAverageCaseTimeNanos = (double) totalAverageCaseTime / n;

            // Write results to file
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilename))) {
                writer.println("Binary Search Performance for dataset size: " + n);
                writer.println(
                        "Best Case Average Running Time: " + String.format("%.6f", avgBestCaseTimeNanos) + " ns");
                writer.println("Average Case Average Running Time: "
                        + String.format("%.6f", avgAverageCaseTimeNanos) // Updated label
                        + " ns");
                writer.println(
                        "Worst Case Average Running Time (Unsuccessful Search): "
                                + String.format("%.6f", avgWorstCaseTimeNanos) + " ns");
                System.out.println("Running times saved to " + outputFilename);
            }

        } catch (IOException e) {
            System.err.println("An I/O error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}