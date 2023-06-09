package com.dsi.spring.service.impl;

import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.lang.*;
import java.util.HashSet;
import java.util.Comparator;
import java.util.ArrayList;
import com.dsi.spring.model.User;
import javax.swing.JOptionPane;

import com.dsi.spring.dao.MovieDao;
import com.dsi.spring.model.Movie;
import com.dsi.spring.service.MovieService;

import org.hibernate.criterion.LikeExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Calendar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;

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

    @Override
    public Page<Movie> matchMovies(User user, Pageable pageable) {
        List<Movie> movies = movieDao.findAll();
        List<Movie> matchingMovies = new ArrayList<>();
    
        Set<Movie> likeMovies = user.getFavouriteMovies();
        if (likeMovies.isEmpty()) {
            return new PageImpl<>(movies, pageable, movies.size());
        }
    
        for (Movie movie : movies) {
            double genreScore = calculateGenreScore(movie, likeMovies);
            double yearScore = calculateYearScore(movie, likeMovies);
            double nameScore = calculateNameScore(movie, likeMovies);
    
            // Combine the scores using appropriate weights or scoring mechanism
            double totalScore = 0.8 * genreScore - 0.01 * yearScore + 0.6 * nameScore;
    
            movie.setScore(totalScore);
            matchingMovies.add(movie);
        }
        matchingMovies.sort(Comparator.comparingDouble(Movie::getScore).reversed());
    
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), matchingMovies.size());
    
        List<Movie> paginatedMovies = matchingMovies.subList(start, end);
        return new PageImpl<>(paginatedMovies, pageable, matchingMovies.size());
    }
    
    @Override
    public List<Movie> filterMovies(String genre, String year, Integer rating){
        List<Movie> movies = movieDao.findAll();
        List<Movie> matchingMovies = new ArrayList<>();

        for (Movie movie: movies){
            int match = 0;
            // Check genre match
            if (genre.equals("all")){
                match++;
            }
            else{
                String[] movieGenres = movie.getGenre().toLowerCase().split(", ");
                for (String movieGenre : movieGenres) {
                    if (movieGenre.contains(genre)) {
                        match++;
                        break;
                    }
                }
            }

            if (year.equals("all")){
                match++;
            }
            else if (isMovieWithinYearRange(movie, year)){
                match++;
            }
            if(movie.getRating() > rating){
                match++;
            }

            if(match == 3){
                matchingMovies.add(movie);
            }
        }

        return matchingMovies;

    }

    private boolean isMovieWithinYearRange(Movie movie, String yearFilter) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(movie.getReleaseDate());
        int movieYear = calendar.get(Calendar.YEAR);
        if (yearFilter.contains("-")) {
            String[] years = yearFilter.split("-");
            int startYear = Integer.parseInt(years[0]);
            int endYear = Integer.parseInt(years[1]);
            return movieYear >= startYear && movieYear <= endYear;
        } else {
            int singleYear = Integer.parseInt(yearFilter);
            return movieYear == singleYear;
        }
    }

    private double calculateGenreScore(Movie movie, Set<Movie> likeMovies) {
        double genreScore = 0.0;
        int genre_count = 0;
        String[] movieGenres = movie.getGenre().split(",\\s*");
    
        for (Movie lmovie : likeMovies) {
            String[] likeMovieGenres = lmovie.getGenre().split(",\\s*");
        
            for (String genre : movieGenres) {
                if (Arrays.asList(likeMovieGenres).contains(genre)) {
                genre_count++;
                 }
            }   
        }
        if (genre_count > 0) {
            // Increase the score based on the number of occurrences
            genreScore = 0.05 * genre_count;
        }

        return genreScore;
    }

    private double calculateYearScore(Movie movie, Set<Movie> likeMovies) {
        double yearScore = 0.0;
        double averageYear = 0;
        for (Movie lmMovie : likeMovies){
            try {
                averageYear += lmMovie.getReleaseDate().getYear();
            }
            catch(Exception e){
                averageYear+= movie.getReleaseDate().getYear();
            }
        }
        averageYear/= likeMovies.size();
        yearScore = Math.abs(averageYear - movie.getReleaseDate().getYear());
        return yearScore/100;
 
    }

    private double calculateNameScore(Movie movie, Set<Movie> likeMovies) {
        double nameScore = 0.0;
        
        // Iterate over the user's favorite movies and find the highest name match score
        for (Movie likeMovie : likeMovies) {
            String movieName = movie.getName().toLowerCase();
            String likeMovieName = likeMovie.getName().toLowerCase();
            
            // Calculate the name match score using a similarity metric (e.g., Levenshtein distance, Jaccard similarity)
            double matchScore = calculateNameMatchScore(movieName, likeMovieName);
            
            // Update the name score if the current match score is higher
            if (matchScore > nameScore) {
                nameScore = matchScore;
            }
        }
        
        return nameScore;
    }
    
    private double calculateNameMatchScore(String movieName, String likeMovieName) {
        // Implement your own logic to calculate the name match score
        // You can use similarity metrics like Levenshtein distance, Jaccard similarity, etc.
        // Return a value between 0.0 and 1.0 indicating the match score
        
        // Example implementation using Jaccard similarity
        Set<Character> movieNameSet = new HashSet<>();
        Set<Character> likeMovieNameSet = new HashSet<>();
        
        for (char c : movieName.toCharArray()) {
            movieNameSet.add(c);
        }
        
        for (char c : likeMovieName.toCharArray()) {
            likeMovieNameSet.add(c);
        }
        
        Set<Character> intersection = new HashSet<>(movieNameSet);
        intersection.retainAll(likeMovieNameSet);
        
        Set<Character> union = new HashSet<>(movieNameSet);
        union.addAll(likeMovieNameSet);
        
        double jaccardSimilarity = (double) intersection.size() / union.size();
        
        return jaccardSimilarity;
    }


}
