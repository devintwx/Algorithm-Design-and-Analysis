package ass;

import java.io.*;
import java.util.*;

public class merge_sort {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Ass.merge_sort <dataset_file>");
            return;
        }

        String filename = args[0];
        List<DataPair> data = readCSV(filename);

        long startTime = System.nanoTime();
        mergeSort(data, 0, data.size() - 1);
        long endTime = System.nanoTime();

        String outFile = "merge_sort_" + data.size() + ".csv";
        writeCSV(data, outFile);
        System.out.println("Sorted file saved to: " + outFile);
        System.out.printf("Execution time: %.3f seconds\n", (endTime - startTime) / 1e9);
    }

    static List<DataPair> readCSV(String filename) throws IOException {
        List<DataPair> list = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            list.add(new DataPair(Integer.parseInt(parts[0]), parts[1]));
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

    static void writeCSV(List<DataPair> list, String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (DataPair dp : list) {
            writer.write(dp.number + "," + dp.text);
            writer.newLine();
        }
        writer.close();
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