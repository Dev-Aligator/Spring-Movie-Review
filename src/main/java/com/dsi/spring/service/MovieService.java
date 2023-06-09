package com.dsi.spring.service;

import java.util.List;

import com.dsi.spring.model.Movie;
import com.dsi.spring.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

@Service
public interface MovieService {
    void saveMovie(Movie movie);

    List<Movie> getMovies();

    void deleteMovie(Movie movie);

    Movie getMovieById(Long id) throws Exception;

    List<Movie> searchMovies(String keyword);

    Page<Movie> matchMovies(User user, Pageable pageable);

    List<Movie> filterMovies(String genre, String year, Integer rating);
}
