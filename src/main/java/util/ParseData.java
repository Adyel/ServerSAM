package util;

import model.orm.MovieDetails;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseData {

    private String name;

    private int year;

    private String quality;

    private boolean validData = false;

    private static Set<String> hashSet = new HashSet<>();

    private static List<MovieDetails> movieDetailsList = new ArrayList<>();

    // ! Better implementation

    private static final Logger LOGGER = LoggerFactory.getLogger(ParseData.class);

    private final String DELIMITER = "[\\[\\](){} _,.-]";

    private final String[] stopwords = {"1080", "1080i", "1080p", "2160p", "2160i", "3d", "480i", "480p", "576i", "576p", "720", "720i", "720p",
            "ac3", "ac3ld", "ac3md", "aoe", "atmos", "bd5", "bdrip", "bdrip", "blueray", "bluray", "brrip", "cam", "cd1", "cd2", "cd3", "cd4", "cd5", "cd6",
            "cd7", "cd8", "cd9", "complete", "custom", "dc", "disc1", "disc2", "disc3", "disc4", "disc5", "disc6", "disc7", "disc8", "disc9", "divx",
            "divx5", "dl", "docu", "dsr", "dsrip", "dts", "dtv", "dubbed", "dutch", "dvd", "dvd1", "dvd2", "dvd3", "dvd4", "dvd5", "dvd6", "dvd7", "dvd8",
            "dvd9", "dvdivx", "dvdrip", "dvdscr", "dvdscreener", "emule", "etm", "extended", "fragment", "fs", "fps", "german", "h264", "hd", "hddvd",
            "hdrip", "hdtv", "hdtvrip", "hevc", "hrhd", "hrhdtv", "ind", "internal", "ld", "limited", "ma", "md", "multisubs", "nfo", "nfofix", "ntg",
            "ntsc", "ogg", "ogm", "pal", "pdtv", "proper", "pso", "r3", "r5", "read", "repack", "rerip", "remux", "retail", "roor", "rs", "rsvcd",
            "screener", "se", "subbed", "svcd", "swedish", "tc", "telecine", "telesync", "ts", "truehd", "uncut", "unrated", "vcf", "webdl", "webrip",
            "workprint", "ws", "www", "x264", "xf", "xvid", "xvidvd", "xxx"};

    // clean before splitting (needs delimiter in front!)
    private String[] cleanwords = {"24\\.000", "23\\.976", "23\\.98", "24\\.00"};


    public ParseData() {
    }


    @Deprecated
    public void setFileName(String _fullTitle) {



        if (_fullTitle.contains("(") && _fullTitle.contains(")")) {
            String[] splitted = _fullTitle.split("[\\(\\)]");
            name = splitted[0].trim();

            try {
                year = Integer.parseInt(splitted[1].trim());
            } catch (NumberFormatException e) {
                year = Integer.parseInt(splitted[3]);
            }

            try {
                quality = splitted[2].trim().split(" ")[0].replaceAll("[\\[\\]]", "");

            } catch (ArrayIndexOutOfBoundsException e) {
                quality = "";
            }
            validData = true;
        }
    }


    /**
     * Tries to get movie name and year from filename<br>
     * 1. splits string using common delimiters ".- ()"<br>
     * 2. searches for first occurrence of common stopwords<br>
     * 3. if last token is 4 digits, assume year and set [1]<br>
     * 4. everything before the first stopword must be the movie name :p
     *
     * @param filename the filename to get the title from
     * @return title/year string (year can be empty)
     */
    private String[] detectCleanMovieNameAndYear(String filename) {
        String[] ret = {"", ""};
        // use trace to not remove logging completely (function called way to often on multi movie dir parsing)
        LOGGER.trace("Parse filename for movie title: \"" + filename + "\"");

        if (filename == null || filename.isEmpty()) {
            LOGGER.warn("Filename empty?!");
            return ret;
        }

        // remove extension (if found) and split (keep var)
        String fname = filename.replaceFirst("\\.\\w{2,4}$", "");
        // replaces any resolution 1234x1234 (must start and end with a non-word (else too global)
        fname = fname.replaceFirst("(?i)\\W\\d{3,4}x\\d{3,4}", " ");
        // replace FPS specific words (must start with a non-word (else too global)
        for (String cw : cleanwords) {
            fname = fname.replaceFirst("(?i)\\W" + cw, " ");
        }

        LOGGER.trace("--------------------");
        LOGGER.trace("IN:  " + fname);

        // Get [optionals] delimited
        List<String> opt = new ArrayList<>();
        Pattern p = Pattern.compile("\\[(.*?)\\]");
        Matcher m = p.matcher(fname);
        while (m.find()) {
            LOGGER.trace("OPT: " + m.group(1));
            String[] o = StringUtils.split(m.group(1), DELIMITER);
            opt.addAll(Arrays.asList(o));
            fname = fname.replace(m.group(), ""); // remove complete group from name
        }
        LOGGER.trace("ARR: " + opt);

        // detect OTR recordings - at least with that special pattern
        p = Pattern.compile(".*?(_\\d{2}\\.\\d{2}\\.\\d{2}[_ ]+\\d{2}\\-\\d{2}\\_).*"); // like _12.11.17_20-15_
        m = p.matcher(fname);
        if (m.matches() && m.start(1) > 10) {
            // start at some later point, not that if pattern is first
            LOGGER.trace("OTR: " + m.group(1));
            fname = fname.substring(0, m.start(1));
        }

        // parse good filename
        String[] s = StringUtils.split(fname, DELIMITER);
        if (s.length == 0) {
            s = opt.toArray(new String[opt.size()]);
        }
        int firstFoundStopwordPosition = s.length;

        // iterate over all splitted items
        for (int i = 0; i < s.length; i++) {
            // search for stopword position
            for (String stop : stopwords) {
                if (s[i].equalsIgnoreCase(stop)) {
                    s[i] = ""; // delete stopword
                    // remember lowest position, but not lower than 2!!!
                    if (i < firstFoundStopwordPosition && i >= 2) {
                        firstFoundStopwordPosition = i;
                    }
                }
            }
            if (isValidImdbId(s[i])) {
                s[i] = ""; // delete imdbId from name
            }
        }

        // scan backwards - if we have at least 1 token, and the last one is a 4 digit, assume year and remove
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String year = "";
        for (int i = s.length - 1; i > 0; i--) {
            if (s[i].matches("\\d{4}")) {
                int parsedYear = Integer.parseInt(s[i]);
                if (parsedYear > 1800 && parsedYear < currentYear + 5) {
                    // well, limit the year a bit...
                    LOGGER.trace("removed token '" + s[i] + "'- seems to be year");
                    year = s[i];
                    s[i] = "";
                    break;
                }
            }
        }
        if (year.isEmpty()) {
            // parse all optional tags for it
            for (String o : opt) {
                if (o.matches("\\d{4}")) {
                    int parsedYear = Integer.parseInt(o);
                    if (parsedYear > 1800 && parsedYear < currentYear + 5) {
                        year = String.valueOf(parsedYear);
                        LOGGER.trace("found possible year " + o);
                    }
                }
            }
        }

        // rebuild string, respecting bad words
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < firstFoundStopwordPosition; i++) {
            if (!s[i].isEmpty()) {
                // check for bad words
                if (true) {
                    String word = s[i];
                    // roman characters such as "Part Iv" should not be camel-cased
                    switch (word.toUpperCase(Locale.ROOT)) {
                        case "I":
                        case "II":
                        case "III":
                        case "IV":
                        case "V":
                        case "VI":
                        case "VII":
                        case "VIII":
                        case "IX":
                        case "X":
                            name.append(word.toUpperCase(Locale.ROOT)).append(" ");
                            break;

                        default:
                            name.append(WordUtils.capitalizeFully(word)).append(" "); // make CamelCase
                            break;
                    }
                }
            }
        }

        if (name.length() == 0) {
            // started with a badword - return name unchanged
            ret[0] = fname;
        } else {
            ret[0] = name.toString().trim();
        }
        ret[1] = year.trim();
        LOGGER.trace("Movie title should be: \"" + ret[0] + "\", from " + ret[1]);

        return ret;
    }


    /**
     * Checks if is valid imdb id.
     *
     * @param imdbId the imdb id
     * @return true, if is valid imdb id
     */
    private static boolean isValidImdbId(String imdbId) {
        if (StringUtils.isEmpty(imdbId)) {
            return false;
        }

        return imdbId.matches("tt\\d{7}");
    }

    @Deprecated
    public void insertToList() {

        if (validData) {
            if (hashSet.add(name + year)) {
                movieDetailsList.add(new MovieDetails(name, year));
            }
        }
    }

    /**
     * This function will get the filename and Clean the String. It
     * will separate "Title" & "Release Year" of the movie and create
     * a hash_list with it. If a entry does not exist on the hash_list
     * it will be added to the "MovieDetails" Collection.
     *
     * @param fileName the filename to get the title and year from.
     */
    public void add(String fileName){
        String[] movie = detectCleanMovieNameAndYear(fileName);

        org.pmw.tinylog.Logger.info(movie[0] + " " + movie[1]);

        if ( hashSet.add(movie[0] + movie[1]) && StringUtils.isNotBlank(movie[1]) ){
            movieDetailsList.add( new MovieDetails(movie[0], Integer.parseInt(movie[1])) );
        }
    }

    public static void flush(){
        movieDetailsList.clear();
    }

    public List<MovieDetails> getMovieDetailsList() {
        return movieDetailsList;
    }
}
