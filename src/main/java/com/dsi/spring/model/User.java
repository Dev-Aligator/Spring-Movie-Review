package com.dsi.spring.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePicPath;


    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_movie_watchlist",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "user_id") ,
            inverseJoinColumns = @JoinColumn(name="movie_id",referencedColumnName = "id")
    )
    private Set<Movie> watchListedMovies = new HashSet<>();


    @ManyToMany
    @JoinTable(
            name = "user_movie_favourite",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "user_id") ,
            inverseJoinColumns = @JoinColumn(name="movie_id",referencedColumnName = "id")
    )
    private Set<Movie> favouriteMovies = new HashSet<>();

    public Set<Movie> getWatchListedMovies() {
        return watchListedMovies;
    }
    public void setWatchListedMovies(Set<Movie> watchListedMovies) {
        this.watchListedMovies = watchListedMovies;
    }
    public void addMovieToWatchlist(Movie movie) { watchListedMovies.add(movie); }
    public void removeMovieFromWatchlist(Long movie_id) {
        Set<Movie>movies = this.getWatchListedMovies();
        for(Movie movie: movies){
            if(movie.getId()==movie_id){
                movies.remove(movie);
                return;
            }
        }
    }

    public Set<Movie> getFavouriteMovies() {
        return favouriteMovies;
    }
    public void setFavouriteMovies(Set<Movie> favouriteMovies) {
        this.favouriteMovies = favouriteMovies;
    }
    public void addMovieToFavourite(Movie movie) { favouriteMovies.add(movie); }
    public void removeMovieFromFavourite(Long movie_id) {
        Set<Movie>movies = this.getFavouriteMovies();
        for(Movie movie: movies){
            if(movie.getId()==movie_id){
                movies.remove(movie);
                return;
            }
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", profilePicPath='" + profilePicPath + '\'' +
                ", roles=" + roles +
                '}';
    }
}
