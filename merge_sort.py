import csv
import sys
import time

def read_csv(filename):
    with open(filename, newline='') as f:
        return [[int(row[0]), row[1]] for row in csv.reader(f)]

def write_csv(data, filename):
    with open(filename, "w", newline='') as f:
        writer = csv.writer(f)
        writer.writerows(data)

def merge_sort(arr, left, right):
    if left < right:
        mid = (left + right) // 2
        merge_sort(arr, left, mid)
        merge_sort(arr, mid + 1, right)
        merge(arr, left, mid, right)

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

def main():
    if len(sys.argv) != 2:
        print("Usage: python merge_sort.py <dataset.csv>")
        return
    filename = sys.argv[1]
    data = read_csv(filename)
    start = time.time()
    merge_sort(data, 0, len(data) - 1)
    end = time.time()
    output = f"merge_sort_{len(data)}.csv"
    write_csv(data, output)
    print(f"Sorted file saved to {output}")
    print(f"Execution time: {end - start:.3f} seconds")

if __name__ == "__main__":
    main()
