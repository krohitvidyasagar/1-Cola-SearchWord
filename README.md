# Word Search Program

## Special Note

The preprocessing part of this program should be handled separately. It would involve forming a word map, where each 
word and its  count in a document should be maintained in a table. The search program should query this table to 
retrieve the count of a specified word  in each document and identifies the document with the maximum count.

## Description

This program searches for the maximum occurrences of a specified word across multiple text files within a directory. It excludes certain words specified in a separate text file. The user can repeatedly search for different words until they choose to exit.

## Features

- Reads a list of excluded words from a file.
- Counts the occurrences of each word in all text files within a specified directory.
- Identifies and prints the file with the maximum occurrences of the specified word.
- Allows the user to repeatedly search for different words until they choose to exit.

## Requirements

- Java Development Kit (JDK) 8 or higher

## Usage

1. **Compile the program:**

    ```sh
    javac -d . src/org/work/com/Main.java
    ```

2. **Run the program:**

    ```sh
    java org.work.com.Main
    ```

3. **Program Instructions:**
   - The program will prompt you to enter a word to search for.
   - If the word is in the list of excluded words, it will display an error message and the list of excluded words.
   - If the word is valid, the program will search for its occurrences in all text files within the specified directory and print the file with the maximum occurrences.
   - The program will then ask if you want to search for another word. Enter "yes" to search again or "no" to exit.
