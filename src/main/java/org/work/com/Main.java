package org.work.com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Main {


    public static void main(String[] args) {
        // Check if the user has passed a word to perform the search
        if (args.length < 1) {
            System.out.println("Error: You need to pass a word to perform the word search.");
            return;
        }

        String searchWord = args[0].toLowerCase();

        String excludedWordsFilePath = "./src/main/resources/excludedWords.txt";
        Set<String> excludedWordSet = getExculdedWordsSet(excludedWordsFilePath);

        // Check that the user passed a word which is not part of the excluded word set
        if (excludedWordSet.contains(searchWord)) {
            System.out.println("Error: You cannot pass any of the excluded words. Excluded words are:");
            for (String word : excludedWordSet) {
                System.out.println(word);
            }
            return;
        }
        Scanner scanner = new Scanner(System.in);

        String filesDirectory = "./src/main/resources/documents";
        Map<String, Map<String, Integer>> fileWordCountMap = getFileWordCountMap(filesDirectory, excludedWordSet);

        printFileWithMaxOccurrences(fileWordCountMap, searchWord);

        while (true) {
            System.out.println("Would you like to search for another word? (yes/no)");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n")) {
                break;
            }

            System.out.println("Please enter the next word to search:");
            searchWord = scanner.nextLine().toLowerCase();

            if (excludedWordSet.contains(searchWord)) {
                System.out.println("Error: You cannot pass any of the excluded words. Excluded words are:");
                for (String word : excludedWordSet) {
                    System.out.println(word);
                }
            } else {
                printFileWithMaxOccurrences(fileWordCountMap, searchWord);
            }
        }

        scanner.close();
    }

    private static Set<String> getExculdedWordsSet(String filePath) {
        /**
         * Returns a HashSet of all the words to be excluded from word search
         *
         * @param filePath: the file path of the txt file with all the excluded words
         */
        Set<String> excludedWordSet = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    // Convert each word to lower case and add to set
                    excludedWordSet.add(line.trim().toLowerCase());
                }
            }
        } catch (IOException e) {
            System.out.println("Error occurred forming excluded words set" + e.getMessage());
        }

        return excludedWordSet;
    }

    public static Map<String, Map<String, Integer>> getFileWordCountMap(String directoryPath, Set<String> excludedWordsSet) {
        /**
         * Returns a Map of all the files inside a directory with the key as filename and value as
         * another map with all the words inside the file and how often they occur
         *
         * @param directoryPath: the directory path which contains all the txt files to be searched
         * @param excludedWordsSet: the set of words to be excluded from the search
         */
        Map<String, Map<String, Integer>> fileWordCountMap = new HashMap<>();

        File directory = new File(directoryPath);

        // Check if the directory exists and is indeed a directory
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        Map<String, Integer> wordCountMap = getWordCountMap(file, excludedWordsSet);
                        fileWordCountMap.put(file.getName(), wordCountMap);
                    }
                }
            }
        } else {
            System.out.println("Main.getFileWordCountMap(): The provided path is not a valid directory.");
        }

        return fileWordCountMap;
    }

    private static Map<String, Integer> getWordCountMap(File file, Set<String> excludedWordsSet) {
        /**
         * Returns a Map of all the words inside a file and their count
         *
         * @param file: the file to be searched
         * @param excludedWordsSet: the set of words to be excluded from the search
         */
        Map<String, Integer> wordCountMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\\W+");
                for (String word: words) {
                    if (word.isEmpty() || excludedWordsSet.contains(word)) {
                        continue;
                    }
                    word = word.toLowerCase();
                    wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);

                }
            }
        } catch (IOException e) {
            System.out.println("Main.getWordCountMap(): Error occurred " + e.getMessage());
        }

        return wordCountMap;
    }

    private static void printFileWithMaxOccurrences(Map<String, Map<String, Integer>> fileWordCountMap, String searchWord) {
        /**
         * Prints the file with the maximum occurrences of the search word.
         *
         * @param fileWordCountMap the map with filenames as keys and word count maps as values
         * @param searchWord the word to search for in the files
         */
        String maxFile = null;
        int maxCount = 0;

        for (Map.Entry<String, Map<String, Integer>> entry : fileWordCountMap.entrySet()) {
            String fileName = entry.getKey();
            Map<String, Integer> wordCountMap = entry.getValue();
            int count = wordCountMap.getOrDefault(searchWord, 0);

            if (count > maxCount) {
                maxCount = count;
                maxFile = fileName;
            }
        }

        if (maxFile != null) {
            System.out.println("The file with the maximum occurrences of the word \"" + searchWord + "\" is: " + maxFile + " with " + maxCount + " occurrences.");
        } else {
            System.out.println("The word \"" + searchWord + "\" does not occur in any file.");
        }
    }
}
