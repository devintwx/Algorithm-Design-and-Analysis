import csv
import sys

merge_steps = []

def read_csv_subset(filename, start, end):
    data = []
    with open(filename, newline='') as csvfile:
        reader = list(csv.reader(csvfile))
        for row in reader[start - 1:end]:
            data.append([int(row[0]), row[1]])
    return data

def merge_sort(arr, left, right):
    if left < right:
        mid = (left + right) // 2
        merge_sort(arr, left, mid)
        merge_sort(arr, mid + 1, right)
        merge(arr, left, mid, right)
        log_step(arr)

def merge(arr, left, mid, right):
    L = arr[left:mid + 1]
    R = arr[mid + 1:right + 1]
    i = j = 0
    k = left
    while i < len(L) and j < len(R):
        if L[i][0] <= R[j][0]:
            arr[k] = L[i]
            i += 1
        else:
            arr[k] = R[j]
            j += 1
        k += 1
    while i < len(L):
        arr[k] = L[i]
        i += 1
        k += 1
    while j < len(R):
        arr[k] = R[j]
        j += 1
        k += 1

def log_step(arr):
    # Always copy the data to avoid overwriting during recursion
    copied = [f"{x[0]}/{x[1]}" for x in arr]
    step = ', '.join(copied)
    merge_steps.append(f"[{step}]")

def write_steps_to_file(filename):
    with open(filename, "w") as f:
        for step in merge_steps:
            f.write(step + "\n")
    print(f"Merge steps saved to {filename}")

def main():
    if len(sys.argv) != 4:
        print("Usage: python merge_sort_step.py <dataset.csv> <start_row> <end_row>")
        return
    dataset = sys.argv[1]
    start = int(sys.argv[2])
    end = int(sys.argv[3])
    data = read_csv_subset(dataset, start, end)

    log_step(data)  # Log the initial unsorted array
    merge_sort(data, 0, len(data) - 1)
    write_steps_to_file(f"merge_sort_step_{start}_{end}.txt")

if __name__ == "__main__":
    main()
