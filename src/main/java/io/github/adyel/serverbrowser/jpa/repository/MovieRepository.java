package io.github.adyel.serverbrowser.jpa.repository;

import io.github.adyel.serverbrowser.jpa.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
