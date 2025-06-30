import csv
from typing import List
import time
import os
import sys

class DataPair:
    def __init__(self, value: int, label: str):
        self.value = value
        self.label = label

    def __repr__(self):
        return f"({self.value}, {self.label})"

def read_data_from_file(filename: str) -> List[DataPair]:
    data = []
    try:
        with open(filename, mode='r', newline='', encoding='utf-8') as file:
            reader = csv.reader(file)
            for line in reader:
                if len(line) == 2:
                    try:
                        value = int(line[0].strip())
                        label = line[1].strip()
                        data.append(DataPair(value, label))
                    except ValueError:
                        print(f"Skipping malformed line (non-integer value): {line}")
                else:
                    print(f"Skipping malformed line (incorrect number of parts): {line}")
    except FileNotFoundError:
        print(f"File not found: {filename}")
    return data

def binary_search_step(data: List[DataPair], target: int, output_file: str) -> int:
    low = 0
    high = len(data) - 1
    target_index = -1

    with open(output_file, 'w', encoding='utf-8') as writer:
        writer.write(f"Searching for target: {target}\n")
        writer.write("[" + ", ".join(map(str, data)) + "]\n")

        step = 1
        while low <= high:
            mid = low + (high - low) // 2
            current = data[mid]
            writer.write(f"{step}: {current}\n")
            step += 1

            if current.value == target:
                target_index = mid
                break
            elif current.value < target:
                low = mid + 1
            else:
                high = mid - 1

        if target_index == -1:
            writer.write("-1\n")

    return target_index

def main():
    dataset_filename = "quick_sort_1000_java.csv"  # input file

    # Get target number
    if len(sys.argv) != 2:
        try:
            target = int(input("Please enter the target number to search: ").strip())
        except ValueError:
            print("Error: Target must be an integer.")
            return
    else:
        try:
            target = int(sys.argv[1])
        except ValueError:
            print("Error: Target must be an integer.")
            return

    data = read_data_from_file(dataset_filename)
    if not data:
        print("Dataset is empty or could not be read. Cannot perform binary search.")
        return

    output_filename = f"binary_search_step_{target}.txt"

    start_time = time.time()
    binary_search_step(data, target, output_filename)
    end_time = time.time()

    print(f"\nSearch steps saved to: {output_filename}")
    print(f"Full path: {os.path.abspath(output_filename)}")
    print(f"Execution time: {end_time - start_time:.6f} seconds\n")

if __name__ == "__main__":
    main()