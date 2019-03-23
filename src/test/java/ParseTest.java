import util.ParseData;

public class ParseTest {

    public static void main(String[] args) {


        ParseData parseData = new ParseData();
        parseData.add("Jurassic.World.Fallen.Kingdom.2018.1080p.BluRay.x264-[YTS.AM]");
        parseData.getMovieDetailsList().forEach(System.out::println);
    }
}
