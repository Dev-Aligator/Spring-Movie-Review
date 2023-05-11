package com.dsi.spring.service;

import com.dsi.spring.model.Review;

public interface ReviewService {
    public void saveNewReview(Review review);

    public void deleteReview(Long id);

    public Review getSingleReview(Long id);
}
