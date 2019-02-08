package crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
    
    private String padingPatern = "------";
    private String startingUrl;
    private Set<String> pages = new HashSet<String>();

    public Crawler(String url) {
        startingUrl = url;
    }

    public static void main(String[] args) {
        if(args != null && args.length > 0) {
            Crawler crawler = new Crawler(args[0]);
            crawler.crawl(args[0], 0);
        }
        else {
            System.out.println("Argument error!");
        }
    }

    private void crawl(String url, int padingIndex) {
        if(!url.startsWith(this.startingUrl)) {
            print("EXTERNAL " + url, padingIndex);
            return; 
        }
        if(pages.contains(url)) {
//            print("Page already visied : " + url, padingIndex);
            return;
        }
        
        print(url, padingIndex);
        pages.add(url);
        Connection connection = Jsoup.connect(url);
        try {
            Document htmlDocument = connection.get();
            
            getStaticContent(htmlDocument, padingIndex);
            
            Elements linksOnPage = htmlDocument.select("a[href]");
            Iterator<Element> it = linksOnPage.iterator();
            while(it.hasNext()) {
                Element e = it.next();
                crawl(e.absUrl("href"), padingIndex+1);
            }
        } catch (IOException e) {
            System.out.println("unable to get document from : " + url);
            e.printStackTrace();
        }
    }

    private void getStaticContent(Document htmlDocument, int padingIndex) {
        Elements staticContent = htmlDocument.select("img");
        Iterator<Element> it = staticContent.iterator();
        while(it.hasNext()) {
            Element e = it.next();
            print("STATIC " + e.absUrl("src"), padingIndex);
        }        
    }

    private void print(String url, int padingIndex) {
        for(int i = 0; i < padingIndex; i++) {
            System.out.print(padingPatern);
        }
        System.out.println(url);
    }

}
