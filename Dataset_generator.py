import sys
import random
import csv

def generate_random_string(length):
    return ''.join(random.choices('abcdefghijklmnopqrstuvwxyz', k=length))

def generate_dataset(size, filename):
    unique_integers = set()

    # Generate unique integers
    while len(unique_integers) < size:
        num = random.randint(1, 1_000_000_000)
        unique_integers.add(num)

    # Write to CSV file
    try:
        with open(filename, 'w', newline='') as csvfile:
            writer = csv.writer(csvfile)
            for num in unique_integers:
                str_len = random.choice([5, 6])
                rand_str = generate_random_string(str_len)
                writer.writerow([num, rand_str])
        print(f"Generated dataset saved to {filename}")
    except IOError as e:
        print(f"Error writing file: {e}")

def main():
    if len(sys.argv) != 2:
        print("Usage: python dataset_generator.py <size>")
        return

    try:
        size = int(sys.argv[1])
    except ValueError:
        print("Size must be an integer.")
        return

    filename = f"dataset_{size}.csv"
    generate_dataset(size, filename)

if __name__ == "__main__":
    main()
