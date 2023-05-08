import org.example.UrlFinder;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import java.util.List;


public class UrlFinderTest {


    @Test
    public void testSanitizeUrl() {
        UrlFinder urlFinder = new UrlFinder("http://example.com", 2, 4, true);
        String sanitizedUrl = urlFinder.sanitizeUrl("https://www.example.com/path?query=string#fragment");
        assertEquals("www_example_com_path_query_string_fragment", sanitizedUrl);
    }

    @Test
    public void testDownloadHtml() {
        UrlFinder urlFinder = new UrlFinder("http://example.com", 2, 4, true);
        String html = urlFinder.downloadHtml("http://example.com");
        assertTrue(html.contains("<html"));
        assertTrue(html.contains("</html>"));
    }

    @Test
    public void testFindLinks() {
        UrlFinder urlFinder = new UrlFinder("http://example.com", 2, 10, true);
        String html = "<html><body><a href=\"http://example.com/page1\">Link 1</a><a href=\"http://example.com/page2\">Link 2</a></body></html>";
        List<String> links = urlFinder.findLinks(html);
        assertEquals(2, links.size());
        assertEquals("http://example.com/page1", links.get(0));
        assertEquals("http://example.com/page2", links.get(1));
    }

    @Test
    public void testIsValidURL() {
        UrlFinder urlFinder = new UrlFinder("http://example.com", 2, 4, true);
        assertTrue(urlFinder.isValidURL("https://www.example.com"));
        assertTrue(urlFinder.isValidURL("http://example.com"));
        assertFalse(urlFinder.isValidURL("not a valid url"));
        assertFalse(urlFinder.isValidURL(""));
    }
}

