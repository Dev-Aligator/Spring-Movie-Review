package com.dsi.spring.dao;

import com.dsi.spring.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieDao extends JpaRepository<Movie, Long> {

}
