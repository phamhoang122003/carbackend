package org.example.rental_car.service;

import lombok.RequiredArgsConstructor;
import org.example.rental_car.entities.Car;
import org.example.rental_car.entities.Customer;
import org.example.rental_car.entities.Review;
import org.example.rental_car.repository.CarRepository;
import org.example.rental_car.repository.CustomerRepository;
import org.example.rental_car.repository.ReviewRepository;
import org.example.rental_car.request.ReviewRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final CarRepository carRepository;
    private final CustomerRepository CustomerRepository;

    @Override
    @Transactional
    public Review saveReview(ReviewRequest reviewRequest, long customerId, long carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));
        if (car.getOwner().getId() == customerId) {
            throw new IllegalArgumentException("Car owner can not review");
        }
        Customer customer = CustomerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
//        Review review = new Review();
//        review.setRating(reviewRequest.getRating());
//        review.setComment(reviewRequest.getComment());
//        review.setCustomer(customer);
//        review.setCar(car);
//        return reviewRepository.save(review);
        Optional<Review> existingReview = reviewRepository.findByCustomerIdAndCarId(customerId, carId);
        if (existingReview.isPresent()) {
            // Nếu đã review trước đó, cập nhật review cũ
            Review review = existingReview.get();
            review.setRating(reviewRequest.getRating());
            review.setComment(reviewRequest.getComment());
            return reviewRepository.save(review);
        } else {
            // Nếu chưa review trước đó, tạo review mới
            Review review = new Review();
            review.setRating(reviewRequest.getRating());
            review.setComment(reviewRequest.getComment());
            review.setCustomer(customer);
            review.setCar(car);
            return reviewRepository.save(review);
        }
    }

    @Override
    public Review getReviewById(long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
    }

    @Override
    public Page<Review> getReviewsByCarId(long carId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return reviewRepository.findAllByCarId(carId, pageRequest);
    }

    @Transactional(readOnly = true)
    @Override
    public Double getAverageRatingByCarId(long carId) {
        List<Review> reviews = getReviewsByCarId(carId, 0, 5).getContent();
        OptionalDouble averageRating = reviews.stream().mapToDouble(Review::getRating).average();
        return averageRating.orElse(0.0);
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Page<Review> getReviewsByCustomerId(long customerId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return reviewRepository.findAllByCustomerId(customerId, pageRequest);
    }

    @Override
    public Review updateReview(ReviewRequest reviewRequest, long reviewId) {
        Review review = getReviewById(reviewId);
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(long reviewId) {
        Review review = getReviewById(reviewId);
        reviewRepository.delete(review);
    }

    @Override
    public Review getReviewByCustomerIdAndCarId(long customerId, long carId) {
        Optional<Review> optionalReview = reviewRepository.findByCustomerIdAndCarId(customerId, carId);
        return optionalReview.orElseThrow(() -> new RuntimeException("Review not found"));
    }
}
