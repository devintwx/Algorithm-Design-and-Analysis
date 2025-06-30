import csv
import random
import time
import os

class DataEntry:
    def __init__(self, value: int, text: str):
        self.value = value
        self.text = text

def read_data_from_file(filename):
    data = []
    try:
        with open(filename, 'r', encoding='utf-8') as file:
            reader = csv.reader(file)
            for row in reader:
                if len(row) == 2:
                    try:
                        value = int(row[0].strip())
                        text = row[1].strip()
                        data.append(DataEntry(value, text))
                    except ValueError:
                        print(f"Skipping malformed line (non-integer): {row}")
                else:
                    print(f"Skipping malformed line: {row}")
    except FileNotFoundError:
        print(f" File not found: {filename}")
    return data

def perform_binary_search(data, target):
    low, high = 0, len(data) - 1
    while low <= high:
        mid = low + (high - low) // 2
        current = data[mid].value
        if current == target:
            return mid
        elif current < target:
            low = mid + 1
        else:
            high = mid - 1
    return -1

def main():
    dataset_filename = "merge_sort_2500000.csv"
    print(f"Using dataset: {dataset_filename}")

    data = read_data_from_file(dataset_filename)

    if not data:
        print("Dataset is empty or unreadable.")
        return

    n = len(data)
    output_filename = f"binary_search_{n}.txt"

    # Best Case: middle value
    total_best = 0
    mid_value = data[n // 2].value
    for _ in range(n):
        start = time.perf_counter_ns()
        perform_binary_search(data, mid_value)
        end = time.perf_counter_ns()
        total_best += (end - start)
    avg_best_ns = total_best / n

    # Worst Case: value not found
    total_worst = 0
    not_found_value = data[-1].value + 1
    for _ in range(n):
        start = time.perf_counter_ns()
        perform_binary_search(data, not_found_value)
        end = time.perf_counter_ns()
        total_worst += (end - start)
    avg_worst_ns = total_worst / n

    # Average Case: Exclude middle 20%
    total_avg = 0
    exclusion_size = n // 5
    exclusion_start = max(0, (n // 2) - (exclusion_size // 2))
    exclusion_end = min(n - 1, (n // 2) + (exclusion_size // 2))

    for _ in range(n):
        while True:
            rand_index = random.randint(0, n - 1)
            if not (exclusion_start <= rand_index <= exclusion_end):
                break
        target = data[rand_index].value
        start = time.perf_counter_ns()
        perform_binary_search(data, target)
        end = time.perf_counter_ns()
        total_avg += (end - start)
    avg_avg_ns = total_avg / n

    # Save results
    with open(output_filename, 'w', encoding='utf-8') as f:
        f.write(f"Binary Search Performance for dataset size: {n}\n")
        f.write(f"Best Case Average Running Time: {avg_best_ns:.2f} ns\n")
        f.write(f"Average Case Average Running Time: {avg_avg_ns:.2f} ns\n")
        f.write(f"Worst Case Average Running Time (Unsuccessful Search): {avg_worst_ns:.2f} ns\n")

    print(f"\nResults saved to: {output_filename}")
    print(f"Full path: {os.path.abspath(output_filename)}")

# Run the main function
main()
