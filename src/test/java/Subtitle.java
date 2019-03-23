import com.github.wtekiela.opensub4j.api.OpenSubtitlesClient;
import com.github.wtekiela.opensub4j.impl.OpenSubtitlesClientImpl;
import com.github.wtekiela.opensub4j.response.MovieInfo;
import com.github.wtekiela.opensub4j.response.SubtitleFile;
import com.github.wtekiela.opensub4j.response.SubtitleInfo;
import org.apache.xmlrpc.XmlRpcException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Subtitle {

    public static void main(String[] args) {

        List<SubtitleInfo> subtitles = null;

        try {
            URL serverUrl = new URL("https", "api.opensubtitles.org", 443, "/xml-rpc");
            OpenSubtitlesClient osClient = new OpenSubtitlesClientImpl(serverUrl);
            osClient.login("Adyel", "mvC1Re2soTcr", "en", "TemporaryUserAgent");
            System.out.println(osClient.isLoggedIn());

            MovieInfo movieInfo = osClient.searchMoviesOnImdb("Forrest Gump (1994)").get(0);
            int movieImdbId = movieInfo.getId();
            SubtitleInfo subtitleInfo = osClient.searchSubtitles("eng", ""+movieImdbId).get(0);
            SubtitleFile subtitleFile = osClient.downloadSubtitles(subtitleInfo.getSubtitleFileId()).get(0);

            Path path = Paths.get("/home/adyel/Desktop/Java.txt");
            try(BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"))){
                writer.write(subtitleFile.getContentAsString("UTF-8"));
            }catch(IOException ex){
                ex.printStackTrace();
            }


      System.out.println(subtitleFile.getContentAsString("UTF-8"));

//            subtitles = osClient.searchSubtitles("eng", "Friends", "1", "1");
            osClient.logout();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }

//        System.out.println(subtitles.size());
    }

}
