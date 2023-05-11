package com.dsi.spring.service;

import java.util.List;

import com.dsi.spring.model.Movie;

import org.springframework.stereotype.Service;

@Service
public interface MovieService {
    void saveMovie(Movie movie);

    List<Movie> getMovies();

    void deleteMovie(Movie movie);

    Movie getMovieById(Long id) throws Exception;
}
