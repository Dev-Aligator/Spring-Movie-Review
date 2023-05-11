package com.dsi.spring.service.impl;

import com.dsi.spring.dao.ReviewDao;
import com.dsi.spring.model.Review;
import com.dsi.spring.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    ReviewDao reviewdao;

    @Override
    public void saveNewReview(Review review) {
        reviewdao.save(review);
    }

    @Override
    public void deleteReview(Long id) {
        reviewdao.deleteById(id);
    }

    @Override
    public Review getSingleReview(Long id) {
        return reviewdao.findById(id).orElse(new Review());
    }
}
