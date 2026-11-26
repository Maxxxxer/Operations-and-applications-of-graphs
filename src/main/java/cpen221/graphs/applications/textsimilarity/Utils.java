package cpen221.graphs.applications.textsimilarity;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Utils {

    // read content from a file or from a URL
    public static String readContent(String input) {
        StringBuilder content = new StringBuilder();

        if (isFile(input)) {
            // Read content from the file
            try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            } catch (IOException e) {
                System.err.println("Error reading from file: " + e.getMessage());
                return null;
            }
        } else if (isValidURL(input)) {
            // Read content from the URL
            try {
                URL url = new URL(input);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reader.close();
            } catch (IOException e) {
                System.err.println("Error reading from URL: " + e.getMessage());
                return null;
            }
        } else {
            System.out.println("Invalid file name or URL.");
            return null;
        }
        return content.toString();
    }

    // Method to check if the input is a file
    public static boolean isFile(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    // Method to check if the input is a valid URL
    public static boolean isValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
            return false;
        }
    }

    // Method to extract a word by removing starting and trailing punctuation
    public static String extractWord(String token) {
        String regex = "^[\\p{Punct}]+|[\\p{Punct}]+$";
        String cleanedWord = token.replaceAll(regex, "").trim();
        return cleanedWord.isEmpty() || cleanedWord.matches("^[\\p{Punct}]+$") ? "" : cleanedWord;
    }

}
