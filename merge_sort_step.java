import java.io.*;
import java.util.*;

public class merge_sort_step {
    static List<List<DataPair>> mergeSteps = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: java Ass.merge_sort_step <csv_file> <start_row> <end_row>");
            return;
        }

        String filename = args[0];
        int startRow = Integer.parseInt(args[1]) - 1; // Convert to 0-based index
        int endRow = Integer.parseInt(args[2]) - 1;

        List<DataPair> data = readCSV(filename, startRow, endRow);

        recordStep(data); // ✅ Log the initial unsorted array

        mergeSort(data, 0, data.size() - 1);

        String outputName = "merge_sort_step_" + (startRow + 1) + "_" + (endRow + 1) + ".txt";
        writeStepsToFile(outputName);
    }

    static List<DataPair> readCSV(String filename, int start, int end) throws IOException {
        List<DataPair> list = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        int row = 0;
        while ((line = reader.readLine()) != null) {
            if (row >= start && row <= end) {
                String[] parts = line.split(",");
                list.add(new DataPair(Integer.parseInt(parts[0]), parts[1]));
            }
            row++;
        }
        reader.close();
        return list;
    }

    static void mergeSort(List<DataPair> list, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right);
            recordStep(list); // ✅ Log after each merge
        }
    }

    static void merge(List<DataPair> list, int left, int mid, int right) {
        LinkedList<DataPair> L = new LinkedList<>(list.subList(left, mid + 1));
        LinkedList<DataPair> R = new LinkedList<>(list.subList(mid + 1, right + 1));

        int k = left;
        while (!L.isEmpty() && !R.isEmpty()) {
            if (L.peek().number <= R.peek().number) {
                list.set(k++, L.poll());
            } else {
                list.set(k++, R.poll());
            }
        }
        while (!L.isEmpty()) list.set(k++, L.poll());
        while (!R.isEmpty()) list.set(k++, R.poll());
    }

    static void recordStep(List<DataPair> currentList) {
        List<DataPair> snapshot = new ArrayList<>();
        for (DataPair dp : currentList) {
            snapshot.add(new DataPair(dp.number, dp.text)); // Make a copy
        }
        mergeSteps.add(snapshot);
    }

    static void writeStepsToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (List<DataPair> step : mergeSteps) {
            writer.write("[");
            for (int i = 0; i < step.size(); i++) {
                DataPair dp = step.get(i);
                writer.write(dp.number + "/" + dp.text);
                if (i < step.size() - 1) writer.write(", ");
            }
            writer.write("]\n");
        }
        writer.close();
        System.out.println("Written to: " + filename);
    }

    static class DataPair {
        int number;
        String text;

        public DataPair(int number, String text) {
            this.number = number;
            this.text = text;
        }
    }
}