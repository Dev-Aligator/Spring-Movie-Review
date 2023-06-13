package com.dsi.spring.service.impl;

import java.util.List;
import java.util.ArrayList;


import com.dsi.spring.dao.MovieDao;
import com.dsi.spring.model.Movie;
import com.dsi.spring.service.MovieService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieDao movieDao;

    @Override
    public void saveMovie(Movie movie) {
        movieDao.save(movie);
    }

    @Override
    public List<Movie> getMovies() {
        return movieDao.findAll();
    }

    @Override
    public void deleteMovie(Movie movie) {
        movieDao.delete(movie);
    }

    @Override
    public Movie getMovieById(Long id) throws Exception {
        return movieDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Movie id: " + id));
    }

    @Override
    public List<Movie> searchMovies(String keyword){
        List<Movie> movies = movieDao.findAll();
        List<Movie> matchingMovies = new ArrayList<>();

    for (Movie movie : movies) {
        if (movie.getName().toLowerCase().contains(keyword.toLowerCase())) {
            matchingMovies.add(movie);
        }
    }
    return matchingMovies;
    }
}
