import csv
from collections import deque # For using queue-like linked lists

#----Class to represent each record (number, text)----
class Data:
    def __init__(self, number, text):
        self.number = number
        self.text = text

    def __str__(self):
        return f"{self.number}/{self.text}"

#----Read only a portion of the CSV file based on start and end row----
def read_csv_subset(filename, start_row, end_row):
    data_list = []
    with open(filename, 'r') as csvfile:
        reader = csv.reader(csvfile)
        for i, row in enumerate(reader, start=1):     # 1-based index
            if start_row <= i <= end_row:
                number = int(row[0])                  # Parse number
                text = row[1]                         # Get associated string
                data_list.append(Data(number, text))  # Add as Data object
    return data_list        # Return list of selected Data


#----Write the current list state to output file----
def write_list(bw, data_list, pivot_index=None):
    if pivot_index is None:         # the First line: print unsorted list
        bw.write(", ".join(str(d) for d in data_list)) # Convert each item to string
        bw.write("\n")
    else:     # Print list during sorting with pivot index
        line = f"pi={pivot_index} ["
        line += ", ".join(str(d) for d in data_list)
        line += "]\n"
        bw.write(line) # Write full sorting step

#----Partition method using deque----
def partition(arr, left, right):
    # Create a temporary queue containing the sublist to sort
    queue = deque(arr[left:right+1])

    L = deque()  # Elements less than pivot
    E = deque()  # Elements equal to pivot
    G = deque()  # Elements greater than pivot

    pivot = queue[-1] # Pivot is the last element

#-- Partition method : distribute elements into L, E, G
    while queue:                                   # While queue is not empty
        current = queue.popleft()                  # Get first element
        if current.number < pivot.number:
            L.append(current)                      # Add to less than pivot
        elif current.number == pivot.number:
            E.append(current)                      # Add to equal
        else:
            G.append(current)                      # Add to greater than pivot

    #-- Copy the partitions back into the original array
    index = left          # Start from left position
    for d in L:
        arr[index] = d    # Set element in original list
        index += 1
    pivot_index = index   # Mark where pivot group begins
    for d in E:
        arr[index] = d
        index += 1
    for d in G:
        arr[index] = d
        index += 1

    return pivot_index   # Return the new pivot index for further sorting

#----Quick Sort recursive implementation ----
def quick_sort(arr, left, right, bw):
    if left < right:
        pi = partition(arr, left, right)  # Partition and get pivot index (above code)
        write_list(bw, arr, pi)           # Write current state with pivot index
        quick_sort(arr, left, pi - 1, bw) #sort left side
        quick_sort(arr, pi + 1, right, bw) #sort right side

#----Main driver function----
def main():
    input_file = "dataset_sample_1000.csv"
    start_row = 1
    end_row = 7
    output_file = f"quick_sort_step_{start_row}_{end_row}.txt"

    data_list = read_csv_subset(input_file, start_row, end_row)

    with open(output_file, "w") as bw:
        write_list(bw, data_list)  # Initial unsorted list
        quick_sort(data_list, 0, len(data_list) - 1, bw)

if __name__ == "__main__":
    main()