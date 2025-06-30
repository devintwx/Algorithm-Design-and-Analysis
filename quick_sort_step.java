
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class quick_sort_step {

    static class Data {

        int number;
        String text;

        Data(int number, String text) {
            this.number = number;
            this.text = text;
        }

        @Override
        public String toString() {
            return number + "/" + text; // format: "number/text"
        }
    }

    public static void main(String[] args) throws IOException {
        String inputFile = "dataset_sample_1000.csv";
        int startRow = 1;
        int endRow = 7;

        List<Data> list = new ArrayList<>();

        // Reads text from the csv file
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            int currentLine = 1; //1-based
            String line;

            //read the file line by line
            while ((line = br.readLine()) != null) {
                if (currentLine >= startRow && currentLine <= endRow) {
                    String[] parts = line.split(","); //split csv file
                    list.add(new Data(Integer.parseInt(parts[0]), parts[1]));
                }
                currentLine++; //move next linee
            }
        }

        // Writes the sorting steps to output file
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("quick_sort_step_" + startRow + "_" + endRow + ".txt"))) { //filename
            printList(bw, list); // write original array (unsorted array)
            quickSort(list, 0, list.size() - 1, bw); // start sort and log
        }
    }

    //-----Quicksort implementation--------------------- (inside lecture notes)
    static void quickSort(List<Data> arr, int left, int right, BufferedWriter bw) throws IOException {
        if (left < right) { //only sort if atleast got 2 elements

            int pi = partition(arr, left, right); //get pivot index position
            printList(bw, arr, pi); // log current state with pivot

            quickSort(arr, left, pi - 1, bw);    //left side of pivot
            quickSort(arr, pi + 1, right, bw);   //right side of pivot
        }
    }

    //-----Partition for quicksort--------------------------(lecture notes use LinkedList)
    static int partition(List<Data> arr, int left, int right) {
        // Create temporary queues
        LinkedList<Data> queue = new LinkedList<>(arr.subList(left, right + 1));

        LinkedList<Data> L = new LinkedList<>(); // Elements less than pivot
        LinkedList<Data> E = new LinkedList<>(); // Elements equal to pivot
        LinkedList<Data> G = new LinkedList<>(); // Elements greater than pivot

        Data pivot = queue.getLast(); // Pivot is last element

        while (!queue.isEmpty()) {
            Data current = queue.removeFirst();
            if (current.number < pivot.number) {
                L.addLast(current);
            } else if (current.number == pivot.number) {
                E.addLast(current);
            } else {
                G.addLast(current);
            }
        }

        // Rebuild the original list
        int index = left;
        for (Data d : L) {
            arr.set(index++, d); // Copy all 'less than' elements back

                }int pivotIndex = index;
        for (Data d : E) {
            arr.set(index++, d); //remeber where pivot section starts

                }for (Data d : G) {
            arr.set(index++, d); // Copy all than elements back
        }
        return pivotIndex;
    }

    static void printList(BufferedWriter bw, List<Data> list) throws IOException {
        for (Data d : list) {
            bw.write(d.toString() + ", ");
        }
        bw.newLine();
    }

    static void printList(BufferedWriter bw, List<Data> list, int pivotIndex) throws IOException {
        bw.write("pi=" + pivotIndex + " [");
        for (int i = 0; i < list.size(); i++) {
            bw.write(list.get(i).toString());
            if (i < list.size() - 1) {
                bw.write(", ");
            }
        }
        bw.write("]");
        bw.newLine();
    }
}
