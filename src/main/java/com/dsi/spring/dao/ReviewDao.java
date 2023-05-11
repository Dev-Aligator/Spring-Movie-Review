package com.dsi.spring.dao;

import com.dsi.spring.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewDao extends JpaRepository<Review, Long> {
}
