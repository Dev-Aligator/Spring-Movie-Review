package com.dsi.spring.service;

import java.util.List;

import com.dsi.spring.model.Movie;
import com.dsi.spring.model.User;


import org.springframework.stereotype.Service;

@Service
public interface MovieService {
    void saveMovie(Movie movie);

    List<Movie> getMovies();

    void deleteMovie(Movie movie);

    Movie getMovieById(Long id) throws Exception;

    List<Movie> searchMovies(String keyword);

    List<Movie> matchMovies(User user);
}
