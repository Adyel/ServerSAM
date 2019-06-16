package io.github.adyel.serverbrowser.jpa.model;

import io.github.adyel.serverbrowser.jpa.service.MovieService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class ModelTest {

  @Autowired
  private MovieService movieService;

  @Test
  public void movieYear(){
    Movie movie = new Movie("Up",2009);
    System.out.println(movie.getYear());
    Assert.assertEquals(movie.getYear(), 2009);
  }
}
