import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class quick_sort {

    public static void main(String[] args) throws IOException {
        String inputFile = "dataset_2500000.csv";
        String outputFile;

        List<String[]> list = new ArrayList<>();

        //----Read data from CSV file----
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] rowParts = line.split(",", 2);  // split into 2 parts only (number, text)
                list.add(rowParts);
            }
        }

        // Set outputFile name based on actual list size
        outputFile = "quick_sort_" + list.size() + "_java.csv";

        //----Start timing the sort----
        long start = System.nanoTime();
        quickSort(list, 0, list.size() - 1);
        long end = System.nanoTime();

        //----Write sorted data to output file----
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            for (String[] row : list) {
                bw.write(row[0] + "," + row[1]);
                bw.newLine();
            }
        }

        //----Print the runtime in seconds----
        System.out.printf("Quick Sort runtime for dataset size %d: %.3f seconds%n",
                list.size(), (end - start) / 1e9);
    }

    //-----Quicksort implementation---------------------
    static void quickSort(List<String[]> S, int left, int right) {
        if (left < right) {
            int pi = partition(S, left, right);
            quickSort(S, left, pi - 1);
            quickSort(S, pi + 1, right);
        }
    }

    //-----Partition method using String[]-----//
    static int partition(List<String[]> S, int left, int right) {
        LinkedList<String[]> L = new LinkedList<>();
        LinkedList<String[]> E = new LinkedList<>();
        LinkedList<String[]> G = new LinkedList<>();

        String[] pivot = S.get(right);
        int pivot_num = Integer.parseInt(pivot[0]);

        for (int i = left; i <= right; i++) {
            String[] e = S.get(i);
            int eval = Integer.parseInt(e[0]);

            if (eval < pivot_num) {
                L.add(e);
            } else if (eval == pivot_num) {
                E.add(e);
            } else {
                G.add(e);
            }
        }

        int index = left;
        for (String[] e : L) {
            S.set(index++, e);
        }
        for (String[] e : E) {
            S.set(index++, e);
        }
        for (String[] e : G) {
            S.set(index++, e);
        }

        return left + L.size(); // Return the index where pivot section starts
    }
}
