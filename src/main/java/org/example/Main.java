package org.example;

import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        // Parse command line arguments
        if (args.length != 4) {
            System.err.println("You must enter 4 arguments: <start_url> <max_urls> <depth> <uniqueness>");
            System.exit(1);
        }
        // put default values only to make the compiler work:
        String startUrl = null;
        int maxUrls = 0;
        int depth = 0;
        boolean uniqueness = false;
        // if I won't get new values to all of the above the program will exit anyway
        try {
            startUrl = args[0];
            new URL(startUrl);
            maxUrls = Integer.parseInt(args[1]);
            depth = Integer.parseInt(args[2]);
            uniqueness = Boolean.parseBoolean(args[3]);

        } catch (MalformedURLException e) {
            System.err.println("Error: Invalid URL");
            System.exit(1);
        } catch (NumberFormatException e) {
            System.err.println("Error: Arguments 2 and 3 must be integers");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Argument 4 must be a boolean");
            System.exit(1);
        }
        if(maxUrls <=0) {
            System.err.println("Error: Number of maxUrls must be a positive number");
            System.exit(1);
        }
        if(depth <0) {
            System.err.println("Error: Number of depth must be 0 or bigger");
            System.exit(1);
        }

        // Create org.example.UrlFinder instance
        UrlFinder urlFinder = new UrlFinder(startUrl, maxUrls, depth, uniqueness);

        // Run org.example.UrlFinder
        urlFinder.run();
    }
}
