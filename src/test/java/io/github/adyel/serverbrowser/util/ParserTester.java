package io.github.adyel.serverbrowser.util;

import io.github.adyel.serverbrowser.jpa.service.MovieService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@SpringBootTest
public class ParserTester {

  @Test
  public void checkParser() {

    List<Parser> parserList = new ArrayList<>();

    List<String> movies =
        Arrays.asList(
            "First Comes Like (2016) 720p Web X264 Solar",
            "Forbidden World (1982) [BluRay] [1080p] [YTS] [YIFY]",
            "For.Love.Or.Money.2019.HDRip.XviD.AC3-EVO",
            "Fraud Saiyyan (2019) 720p HDRip Hindi Full Movie x264 AAC ESubs SM Team",
            "Free Solo (2018) [BluRay] [1080p] [YTS] [YIFY]",
            "GoldenEye 1995.2160p.UHDrip.H265.SDR.DTS-HDMA.5.1-DDR",
            "Happy Death Day 2U (2019) 720p HC HDRip 850MB - MkvCage",
            "Her.Smell.2019.720p.HDRip.1GB.x264-GalaxyRG",
            "If Beale Street Could Talk (2018) [BluRay] [720p] [YTS] [YIFY]",
            "Love, Death & Robots S01 Complete 720p NF WEB-DL 2GB - MkvCage",
            "Madonna.and.the.Breakfast.Club.2019.1080p.WEB-DL.H264.AAC-EVO[EtHD]",
            "Mortal Engines.2018.Multi.UHD.2160p.Blu-ray.x265.HDR.Atmos.7.1-DDR",
            "Never Grow Old (2019) [WEBRip] [720p] [YTS] [YIFY]",
            "Nurse 3D - Nurse - L'infermiera (2013).720p.H264.italian.english.Ac3.sub.NUita",
            "How to Train Your Dragon The Hidden World (2019) HC HQ TCRip x264 AAC",
            "Aquaman.2018.IMAX.720p.BluRay.x264-NeZu",
            "Chimera Strain (2018) [WEBRip] [1080p] [YTS] [YIFY]",
            "Fantastic Beasts - The Crimes of Grindelwald (2018) Extended (1080p BluRay x265 HEVC 10bit AAC 7 1 Tigole) QxR",
            "Spider-Man - Into the Spider-Verse (2018) (1080p BluRay x265 HEVC 10bit AAC 5 1 Tigole) QxR",
            "Sex and the City (2008) [BluRay] [720p] [YTS] [YIFY]",
            "Second Act 2018 1080p BluRay x264-DRONES TGx");

    Flux.fromIterable(movies)
        .subscribeOn(Schedulers.elastic())
        .map(Parser::of)
        .map(Parser::parse)
        .log()
        .subscribe(null, Throwable::printStackTrace, this::updateTable);

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void updateTable(){
    System.out.println("Updated");
  }
}
