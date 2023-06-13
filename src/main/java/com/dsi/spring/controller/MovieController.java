package com.dsi.spring.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dsi.spring.dao.UserDao;
import com.dsi.spring.model.Actor;
import com.dsi.spring.model.Movie;
import com.dsi.spring.model.Review;
import com.dsi.spring.model.User;
import com.dsi.spring.security.MyUserDetails;
import com.dsi.spring.service.ActorService;
import com.dsi.spring.service.AuthService;
import com.dsi.spring.service.MovieService;
import com.dsi.spring.utility.FileUpload;
import com.dsi.spring.utility.constants.ImageType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private UserDao userService;

    @Autowired
    private AuthService authService;

    @RequestMapping("/movies")
    public String getHomeMovies(Model model) {
        List<Movie> movies = movieService.getMovies();
        model.addAttribute("movies", movies);
        return "user/home";
    }

    @RequestMapping("/movies/search")
    public String getHomeMovies(@RequestParam("keyword") String keyword, Model model) {
        List<Movie> movies = movieService.searchMovies(keyword);
        model.addAttribute("movies", movies);
        return "user/home";
    }

    @RequestMapping("/movies/{id}")
    public String getMoviePreview(@PathVariable("id") long id, Model model) {
        try {
            //System.out.println("MOVIE ID: "+id);
            Movie movie = movieService.getMovieById(id);
            model.addAttribute("movie", movie);
            model.addAttribute("new_review", new Review());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "user/movie/movie_preview";
    }

    @RequestMapping("/admin/movies")
    public String getMovies(Model model) {
        List<Movie> movies = movieService.getMovies();
        model.addAttribute("movies", movies);
        return "admin/movie/movies";
    }

    @RequestMapping("/admin/movies/search")
    public String getMovies(@RequestParam("keyword") String keyword, Model model) {
        List<Movie> movies = movieService.searchMovies(keyword);
        model.addAttribute("movies", movies);
        return "admin/movie/movies";
    }

    @RequestMapping(value = "/admin/movies/add", method = RequestMethod.GET)
    public String addMovieForm(Model model) {
        List<Actor> actors = actorService.getActors();
        model.addAttribute("actors", actors);
        model.addAttribute("movieForm", new Movie());
        return "admin/movie/movie_form";
    }

    @RequestMapping(value = "/admin/movies/add", method = RequestMethod.POST)
    public String addMovie(Movie movie, BindingResult result, @RequestParam(value = "file") MultipartFile file,
                           RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(movie);
            return "admin/movie/movie_form";
        }
        try {
            String path = FileUpload.saveImage(ImageType.MOVIE_POSTER, movie.getName(), file);
            movie.setPoster(path);

            String videoId = extractVideoId(movie.getTrailerLink());
            String embedLink = "https://www.youtube.com/embed/" + videoId;
            movie.setTrailerLink(embedLink);
            
            movieService.saveMovie(movie);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(movie);
            return "admin/movie/movie_form";
        }
        return "redirect:/admin/movies/";
    }

    // shows update form
    @RequestMapping(value = "/admin/movies/edit/{id}", method = RequestMethod.GET)
    public String updateMovieForm(@PathVariable("id") long id, Model model) {
        List<Actor> actors = actorService.getActors();
        model.addAttribute("actors", actors);

        try {
            Movie movie = movieService.getMovieById(id);
            model.addAttribute("movieForm", movie);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return "admin/movie/movie_form";
    }

    @RequestMapping(value = "/admin/movies/edit/{id}", method = RequestMethod.POST)
    public String updateMovie(@PathVariable("id") long id, Movie movie, BindingResult result, Model model,
                              @RequestParam(value = "file", required = false) MultipartFile file, RedirectAttributes redirectAttributes) {


        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(model);
            return "admin/movie/movie_form";
        }
        try {
            if (!file.isEmpty()) {
                String path = FileUpload.saveImage(ImageType.MOVIE_POSTER, movie.getName(), file);
                movie.setPoster(path);
            } else {
                movie.setPoster(movieService.getMovieById(id).getPoster());
            }

            String videoId = extractVideoId(movie.getTrailerLink());
            String embedLink = "https://www.youtube.com/embed/" + videoId;
            movie.setTrailerLink(embedLink);

            movieService.saveMovie(movie);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(model);
            return "admin/movie/movie_form";
        }
        return "redirect:/admin/movies/";
    }

    // Method to extract the video ID from the YouTube link
    private String extractVideoId(String youtubeLink) {
        String videoId = "";
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|\\/v%2F)[^#\\&\\?\\n]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youtubeLink); //url is youtube url for which you want to extract video id.
        if (matcher.find()) {
            videoId = matcher.group();
        }
        return videoId;
    }


    @RequestMapping("/admin/movies/delete/{id}")
    public String deleteMovie(@PathVariable("id") long id, Model model) {
        try {
            Movie movie = movieService.getMovieById(id);
            movieService.deleteMovie(movie);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return "redirect:/admin/movies/";
    }

    @RequestMapping(value = "/movies/{movie_id}/add-to-watchlist", method = RequestMethod.GET)
    public String addMovieToWatchlist(@AuthenticationPrincipal MyUserDetails principal, @PathVariable("movie_id") long movieId) {
        try {
            Movie movie = movieService.getMovieById(movieId);
            User user = authService.profile(principal);
            user.addMovieToWatchlist(movie);
            userService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/movies/watch-listed-movies";
    }

    @RequestMapping(value = "/movies/{movie_id}/remove-from-watchlist", method = RequestMethod.GET)
    public String removeMovieFromWatchlist(@AuthenticationPrincipal MyUserDetails principal, @PathVariable("movie_id") long movieId) {
        try {
            Movie movie = movieService.getMovieById(movieId);
            User user = authService.profile(principal);
            user.removeMovieFromWatchlist(movie.getId());
            userService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/movies/watch-listed-movies";
    }

    @RequestMapping(value = "/movies/watch-listed-movies/clear-all", method = RequestMethod.GET)
    public String clearWatchList(@AuthenticationPrincipal MyUserDetails principal, Model model) {
        try {
            User user = authService.profile(principal);
            user.setWatchListedMovies(new HashSet<>());
            userService.save(user);
            Set<Movie> movies = user.getWatchListedMovies();
            model.addAttribute("movies", movies);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/movies/watch-listed-movies";
    }

    @RequestMapping(value = "/movies/watch-listed-movies", method = RequestMethod.GET)
    public String showWatchListedMovies(@AuthenticationPrincipal MyUserDetails principal, Model model) {
        try {
            User user = authService.profile(principal);
            Set<Movie> movies = user.getWatchListedMovies();
            model.addAttribute("movies", movies);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "watchlist";
    }

    @RequestMapping(value = "/movies/{movie_id}/add-to-favourite", method = RequestMethod.GET)
    public String addMovieToFavourite(@AuthenticationPrincipal MyUserDetails principal, @PathVariable("movie_id") long movieId) {
        try {
            Movie movie = movieService.getMovieById(movieId);
            User user = authService.profile(principal);
            user.addMovieToFavourite(movie);
            userService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/movies/favourite-movies";
    }


    @RequestMapping(value = "/movies/{movie_id}/remove-from-favourite", method = RequestMethod.GET)
    public String removeMovieFromFavourite(@AuthenticationPrincipal MyUserDetails principal, @PathVariable("movie_id") long movieId) {
        try {
            Movie movie = movieService.getMovieById(movieId);
            User user = authService.profile(principal);
            user.removeMovieFromFavourite(movie.getId());
            userService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/movies/favourite-movies";
    }

    @RequestMapping(value = "/movies/favourite-movies/clear-all", method = RequestMethod.GET)
    public String clearFavouriteMovies(@AuthenticationPrincipal MyUserDetails principal, Model model) {
        try {
            User user = authService.profile(principal);
            user.setFavouriteMovies(new HashSet<>());
            userService.save(user);
            Set<Movie> movies = user.getWatchListedMovies();
            model.addAttribute("movies", movies);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/movies/favourite-movies";
    }

    @RequestMapping(value = "/movies/favourite-movies", method = RequestMethod.GET)
    public String showFavouriteMovies(@AuthenticationPrincipal MyUserDetails principal, Model model) {
        try {
            User user = authService.profile(principal);
            Set<Movie> movies = user.getFavouriteMovies();
            model.addAttribute("movies", movies);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "favourite";
    }

}
