import com.github.wtekiela.opensub4j.api.OpenSubtitlesClient;
import com.github.wtekiela.opensub4j.impl.OpenSubtitlesClientImpl;
import com.github.wtekiela.opensub4j.response.SubtitleInfo;
import org.apache.xmlrpc.XmlRpcException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Subtitle {

    public static void main(String[] args) {

        List<SubtitleInfo> subtitles = null;

        try {
            URL serverUrl = new URL("https", "api.opensubtitles.org", 443, "/xml-rpc");
            OpenSubtitlesClient osClient = new OpenSubtitlesClientImpl(serverUrl);
            osClient.login("myID", "andPassword", "en", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");
            System.out.println(osClient.isLoggedIn());
            subtitles = osClient.searchSubtitles("eng", "Friends", "1", "1");
            osClient.logout();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }

        System.out.println(subtitles.size());
    }

}
