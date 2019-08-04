package io.github.adyel.serverbrowser.util;

import io.github.adyel.serverbrowser.jpa.model.Movie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.WordUtils;
import org.springframework.util.StringUtils;

@Slf4j
public class Parser implements Callable<Movie> {

  private static final String DELIMITER = "[\\[\\](){} _,.-]";
  private static final String[] STOP_WORDS = {
    "1080",
    "1080i",
    "1080p",
    "2160p",
    "2160i",
    "3d",
    "480i",
    "480p",
    "576i",
    "576p",
    "720",
    "720i",
    "720p",
    "ac3",
    "ac3ld",
    "ac3md",
    "aoe",
    "atmos",
    "bd5",
    "bdrip",
    "bdrip",
    "blueray",
    "bluray",
    "brrip",
    "cam",
    "cd1",
    "cd2",
    "cd3",
    "cd4",
    "cd5",
    "cd6",
    "cd7",
    "cd8",
    "cd9",
    "complete",
    "custom",
    "dc",
    "disc1",
    "disc2",
    "disc3",
    "disc4",
    "disc5",
    "disc6",
    "disc7",
    "disc8",
    "disc9",
    "divx",
    "divx5",
    "dl",
    "docu",
    "dsr",
    "dsrip",
    "dts",
    "dtv",
    "dubbed",
    "dutch",
    "dvd",
    "dvd1",
    "dvd2",
    "dvd3",
    "dvd4",
    "dvd5",
    "dvd6",
    "dvd7",
    "dvd8",
    "dvd9",
    "dvdivx",
    "dvdrip",
    "dvdscr",
    "dvdscreener",
    "emule",
    "etm",
    "extended",
    "fragment",
    "fs",
    "fps",
    "german",
    "h264",
    "hd",
    "hddvd",
    "hdrip",
    "hdtv",
    "hdtvrip",
    "hevc",
    "hrhd",
    "hrhdtv",
    "ind",
    "internal",
    "ld",
    "limited",
    "ma",
    "md",
    "multisubs",
    "nfo",
    "nfofix",
    "ntg",
    "ntsc",
    "ogg",
    "ogm",
    "pal",
    "pdtv",
    "proper",
    "pso",
    "r3",
    "r5",
    "read",
    "repack",
    "rerip",
    "remux",
    "retail",
    "roor",
    "rs",
    "rsvcd",
    "screener",
    "se",
    "subbed",
    "svcd",
    "swedish",
    "tc",
    "telecine",
    "telesync",
    "ts",
    "truehd",
    "uncut",
    "unrated",
    "vcf",
    "webdl",
    "webrip",
    "workprint",
    "ws",
    "www",
    "x264",
    "xf",
    "xvid",
    "xvidvd",
    "xxx",
    "hc",
    "hq",
    "tcrip"
  };
  private static final String[] CLEAN_WORDS = {"24\\.000", "23\\.976", "23\\.98", "24\\.00"};

  private final String filename;

  private Parser(String filename) {
    this.filename = filename;
  };

  public static Parser of(String filename) {
    return new Parser(filename);
  }

  public Movie parse() {
    String[] value = cleanFilename(filename);
    String name = value[0];
    int year = !value[1].isEmpty() ? Integer.parseInt(value[1]) : 0;
    return new Movie(name, year);
  }

  /**
   * Tries to get movie name and year from filename<br>
   * 1. splits string using common {@link Parser#DELIMITER} ".- ()"<br>
   * 2. searches for first occurrence of common {@link Parser#STOP_WORDS}<br>
   * 3. if last token is 4 digits, assume year and set [1]<br>
   * 4. everything before the first {@link Parser#STOP_WORDS} must be the movie name :p
   *
   * @param filename the filename to get the title and year from
   * @return title/year string (year can be empty)
   */
  private String[] cleanFilename(String filename) {
    log.trace("Parsing Filename: " + filename);

    if (filename == null || filename.isEmpty()) {
      log.warn("Filename Empty/null ?");
      return new String[] {"", ""};
    }

    // remove extension (if found) and split (keep var)
    // replaces any resolution 1234x1234 (must start and end with a non-word (else too global)

    String name =
        filename.replaceFirst("\\.\\w{2,4}$", "").replaceFirst("(?i)\\W\\d{3,4}x\\d{3,4}", " ");
    // replace FPS specific words (must start with a non-word (else too global)
    for (String cleanWord : CLEAN_WORDS) {
      name = name.replaceFirst("(?i)\\W" + cleanWord, " ");
    }

    log.trace("Inside: " + name);

    // Get [optionals] delimited
    List<String> optionals = new ArrayList<>();
    Pattern pattern = Pattern.compile("\\[(.*?)\\]");
    Matcher matcher = pattern.matcher(name);
    while (matcher.find()) {
      log.trace("OPT: " + matcher.group(1));
      String[] o = matcher.group(1).split(DELIMITER);
      //      String[] o = StringUtils.split(matcher.group(1), DELIMITER);
      optionals.addAll(Arrays.asList(o));
      name = name.replace(matcher.group(), ""); // remove complete group from name
    }
    log.trace("ARR: " + optionals);

    // detect OTR recordings - at least with that special pattern
    pattern =
        Pattern.compile(
            ".*?(_\\d{2}\\.\\d{2}\\.\\d{2}[_ ]+\\d{2}\\-\\d{2}\\_).*"); // like _12.11.17_20-15_
    matcher = pattern.matcher(name);
    if (matcher.matches() && matcher.start(1) > 10) {
      // start at some later point, not that if pattern is first
      log.trace("OTR: " + matcher.group(1));
      name = name.substring(0, matcher.start(1));
    }

    // parse good filename
    String[] s = name.split(DELIMITER);

    if (s.length == 0) {
      s = optionals.toArray(new String[optionals.size()]);
    }
    int firstFoundStopWordPosition = s.length;

    // iterate over all splitted items
    for (int i = 0; i < s.length; i++) {
      // search for stopword position
      for (String stop : STOP_WORDS) {
        if (s[i].equalsIgnoreCase(stop)) {
          s[i] = ""; // delete stopword
          // remember lowest position, but not lower than 2!!!
          if (i < firstFoundStopWordPosition && i >= 2) {
            firstFoundStopWordPosition = i;
          }
        }
      }
      if (isValidImdbId(s[i])) {
        s[i] = ""; // delete imdbId from name
      }
    }

    // scan backwards - if we have at least 1 token, and the last one is a 4 digit, assume year and
    // remove
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    String year = "";
    for (int i = s.length - 1; i > 0; i--) {
      if (s[i].matches("\\d{4}")) {
        int parsedYear = Integer.parseInt(s[i]);
        if (parsedYear > 1800 && parsedYear < currentYear + 5) {
          // well, limit the year a bit...
          log.trace("removed token '" + s[i] + "'- seems to be year");
          year = s[i];
          s[i] = "";
          break;
        }
      }
    }
    if (year.isEmpty()) {
      // parse all optional tags for it
      for (String o : optionals) {
        if (o.matches("\\d{4}")) {
          int parsedYear = Integer.parseInt(o);
          if (parsedYear > 1800 && parsedYear < currentYear + 5) {
            year = String.valueOf(parsedYear);
            log.trace("found possible year " + o);
          }
        }
      }
    }

    // rebuild string, respecting bad words
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < firstFoundStopWordPosition; i++) {
      if (!s[i].isEmpty()) {

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
            stringBuilder.append(word.toUpperCase(Locale.ROOT)).append(" ");
            break;

          default:
            stringBuilder.append(WordUtils.capitalizeFully(word)).append(" "); // make CamelCase
            break;
        }
      }
    }

    String[] ret = {"", ""};
    if (stringBuilder.length() == 0) {
      // started with a badword - return name unchanged
      ret[0] = name;
    } else {
      ret[0] = stringBuilder.toString().trim();
    }
    ret[1] = year.trim();
    log.trace("Movie title should be: \"" + ret[0] + "\", from " + ret[1]);

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

  @Override
  public Movie call() throws Exception {
    return null;
  }
}
