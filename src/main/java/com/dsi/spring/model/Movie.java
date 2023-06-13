package com.dsi.spring.model;

import java.util.*;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Entity(name = "movie")
@Table(name = "movie", uniqueConstraints = @UniqueConstraint(name = "movie_name_unique", columnNames = "name"))

public class Movie {

    @Id

    @SequenceGenerator(name = "movie_id_sequence", sequenceName = "movie_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_id_sequence")
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "release_date", nullable = false, columnDefinition = "DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-mm-dd") // default html date type input date pattern "yyyy-mm-dd"
    private Date releaseDate;

    // only for users to see
    @Column(name = "rating", nullable = true, columnDefinition = "Decimal(10,2)")
    private Double rating;

    @Column(name = "genre", nullable = false, columnDefinition = "TEXT")
    private String genre;

    @Column(name = "poster", nullable = true, columnDefinition = "TEXT")
    private String poster;

    @OneToMany(mappedBy = "movie")
    private List<Review> reviews;

    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "movie_actor", joinColumns = { @JoinColumn(name = "movie_id") }, inverseJoinColumns = {
            @JoinColumn(name = "actor_id") })
    private Set<Actor> actors;

    @ManyToMany(mappedBy = "watchListedMovies")
    private Set<User> watchlistUsers = new HashSet<>();

    @ManyToMany(mappedBy = "favouriteMovies")
    private Set<User> favouriteMovieUsers = new HashSet<>();

    @Column(name="trailer_link", nullable = true, columnDefinition = "TEXT")
    private String trailerLink;

    @Column(name = "score", nullable = true)
    private Double score;

    public Movie() {
    }

    public Movie(String name, Date releaseDate, Double rating, String genre, String poster, String description,
            Set<Actor> actors,Set<User>watchlistUsers, String trailerLink) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.genre = genre;
        this.poster = poster;
        this.description = description;
        this.actors = actors;
        this.trailerLink = trailerLink;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }
    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getTrailerLink(){
        return trailerLink;
    }

    public void setTrailerLink(String trailerLink){
        this.trailerLink = trailerLink;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Set<User> getWatchlistUsers() {
        return watchlistUsers;
    }
    public void setWatchlistUsers(Set<User> watchlistUsers) {
        this.watchlistUsers = watchlistUsers;
    }
    public void addUserToWatchListedMovie(User user) { watchlistUsers.add(user); }
    public boolean isUserAvailableInMovieWatchlist(Long user_id){
        Set<User>watchlistUser = this.getWatchlistUsers();
        for(User user:watchlistUser){
            if(user.getId() == user_id)
                return true;
        }
        return false;
    }

    public Set<User> getFavouriteMovieUsers() {
        return favouriteMovieUsers;
    }
    public void setFavouriteMovieUsers(Set<User> favouriteMovieUsers) { this.favouriteMovieUsers = favouriteMovieUsers; }
    public void addUserToFavouriteMovie(User user) { favouriteMovieUsers.add(user); }
    public boolean isUserAvailableInFavouriteMovie(Long user_id){
        Set<User>favouriteMovieUsers = this.getFavouriteMovieUsers();
        for(User user:favouriteMovieUsers){
            if(user.getId() == user_id)
                return true;
        }
        return false;
    }




    @Override
    public String toString() {
        return "Movie [id=" + id + ", name=" + name + ", releaseDate=" + releaseDate + ", rating=" + rating + ", genre="
                + genre + ", poster=" + poster + ", description=" + description + ", actors=" + actors + "trailer=" + trailerLink + "]";
    }
}
