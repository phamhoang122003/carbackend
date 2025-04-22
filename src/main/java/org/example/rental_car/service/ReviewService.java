package org.example.rental_car.service;

import org.example.rental_car.entities.Review;
import org.example.rental_car.request.ReviewRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService {
    Review saveReview(ReviewRequest reviewRequest,long customerId, long carId);
    Review getReviewById(long id);
    Page<Review> getReviewsByCarId(long carId, int page, int size);
    Double getAverageRatingByCarId(long carId);
    List<Review> getAllReviews();
    Page<Review> getReviewsByCustomerId(long customerId, int page, int size);
    Review updateReview(ReviewRequest reviewRequest, long reviewId);
    void deleteReview(long reviewId);
    Review getReviewByCustomerIdAndCarId(long customerId, long carId);
}
