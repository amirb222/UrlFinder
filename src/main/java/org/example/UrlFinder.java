package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UrlFinder {
    private final String startUrl;
    private final int maxLinks;
    private final int maxDepth;
    private final boolean unique;

    public UrlFinder(String startUrl, int maxLinks, int maxDepth, boolean unique) {
        this.startUrl = startUrl;
        this.maxLinks = maxLinks;
        this.maxDepth = maxDepth;
        this.unique = unique;
    }

    /**
     The main function of the UrlFinder class. It crawls web pages starting from a given URL and saves their
     HTML content to files on the local file system.
     */
    public void run() {
        Set<String> visitedUrls = new HashSet<>();
        List<String> urlsToVisit = new ArrayList<>();
        urlsToVisit.add(startUrl);
        visitedUrls.add(startUrl);

        for (int depth = 0; depth <= maxDepth; depth++) {
            List<String> newUrls = new ArrayList<>();

            for (String url : urlsToVisit) {
                int linksPerUrl=0; //use to check that we only add maxLinks for each Url
                String sanitizedUrl = sanitizeUrl(url);
                String fileName = depth + "/" + sanitizedUrl + ".html";
                String directoryName = depth + "/";
                String html = downloadHtml(url);

                saveHtmlToFile(html, fileName, directoryName);

                if (depth < maxDepth) {
                    List<String> links = findLinks(html);
                    for (String link : links) {
                        String sanitizedLink = sanitizeUrl(link);
                        if (unique && visitedUrls.contains(sanitizedLink)) {
                            continue;
                        }
                        visitedUrls.add(sanitizedLink);
                        if(linksPerUrl<maxLinks){ //check that we only add maxLinks for each Url
                            newUrls.add(link);
                            linksPerUrl++;
                        }
                        else{
                            break;
                        }
                    }

                }

            }

            urlsToVisit = newUrls;
            if (urlsToVisit.isEmpty()) {
                break;
            }
        }
    }


    /**

     Sanitizes a given URL by removing the "https://" prefix and replacing any non-alphanumeric characters with underscores.
     @param url the URL to sanitize
     @return a sanitized version of the URL
     */
    public String sanitizeUrl(String url) {
        return url.replace("https://", "").replaceAll("[^a-zA-Z0-9-]", "_");
    }

    /**

     Downloads the HTML content of a given URL using the Jsoup library.
     @param url the URL to download the HTML from
     @return a string containing the HTML content of the given URL, or an empty string if the download fails
     */
    public String downloadHtml(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            return doc.html();
        } catch (IOException e) {
            System.out.println("Failed to download " + url + ": " + e.getMessage());
            return "";
        }
    }

    /**

     This method finds all valid links in a given HTML string and returns them as a List of Strings.
     @param html the HTML string to search for links in
     @return a List of Strings representing all valid links found in the HTML string
     */
    public List<String> findLinks(String html) {
        List<String> links = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("a[href]");

        for (Element element : elements) {
            String link = element.absUrl("href");
            if (!link.isEmpty() && isValidURL(link)) {
                links.add(link);
            }
        }

        return links;
    }

    /**

     Saves the provided HTML content to a file in the specified directory with the given file name.
     If the directory does not exist, it will be created.
     @param html the HTML content to be saved to file
     @param fileName the name of the file to be created
     @param directoryName the name of the directory where the file will be created
     */
    private void saveHtmlToFile(String html, String fileName, String directoryName) {
        try {
            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            FileWriter writer = new FileWriter(fileName);
            writer.write(html);
            writer.close();

            System.out.println("Saved " + fileName);
        } catch (IOException e) {
            System.out.println("Failed to save " + fileName + ": " + e.getMessage());
        }
    }

    /**

     Checks if the given URL is valid by attempting to create a URL object from it and verifying its syntax.
     @param url the URL to be checked
     @return true if the URL is valid, false otherwise
     */
    public boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}


