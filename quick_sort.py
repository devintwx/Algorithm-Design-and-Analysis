import csv
import time
import sys
import os

sys.setrecursionlimit(2000000)          # Increase recursion depth for large datasets



#----read the entire CSV file into a list of Data objects----
def read_csv(filename):
    data_list = []
    with open(filename, 'r') as csvfile:
        reader = csv.reader(csvfile)
        for row in reader:
            data_list.append([int(row[0]), row[1]])  # Add to the list as a Data object
    return data_list


#----Function to write the sorted Data list into a new CSV file----
def write_csv(filename, data_list):
    with open(filename, 'w', newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerows(data_list)  # Write each row as [number, text]

#-----Partition method ----
def partition(arr, left, right):
    pivot = arr[right]
    pivot_num = pivot[0]

    L, E, G = [], [], []

    for i in range(left, right + 1):
        current = arr[i]
        if current[0] < pivot_num:
            L.append(current)
        elif current[0] == pivot_num:
            E.append(current)
        else:
            G.append(current)

    index = left
    for item in L:
        arr[index] = item
        index += 1
    for item in E:
        arr[index] = item
        index += 1
    for item in G:
        arr[index] = item
        index += 1

    return left + len(L)  # Return the pivot starting index


#-----QuickSort recursive implementation -----
def quick_sort(arr, left, right):
   if left < right:  # Only sort if at least two elements
       pi = partition(arr, left, right)     # Partition the list
       quick_sort(arr, left, pi - 1)        # sort left half
       quick_sort(arr, pi + 1, right)       # sort right half

#-----Main function to execute the full quick sort process-----#
def main():
    input_file = "dataset_2500000.csv"  # Input file

    # Read data from CSV file
    data_list = read_csv(input_file)

    # Set output filename using actual data size
    output_file = f"quick_sort_{len(data_list)}_python.csv"

    # Start timer (exclude I/O)
    start_time = time.perf_counter_ns()
    quick_sort(data_list, 0, len(data_list) - 1)
    # quick_sort_iterative(data_list)
    end_time = time.perf_counter_ns()

    # Write sorted data to output file
    write_csv(output_file, data_list)

    # Print runtime in seconds
    runtime_sec = (end_time - start_time) / 1e9
    print(f"Quick Sort runtime for dataset size {len(data_list)}: {runtime_sec:.3f} seconds")

#----Run main() if this file is executed directly----#
if __name__ == "__main__":
    main()
