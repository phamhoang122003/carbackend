package org.example.rental_car.controller;

import lombok.RequiredArgsConstructor;
import org.example.rental_car.common.Url;
import org.example.rental_car.entities.Review;
import org.example.rental_car.request.ReviewRequest;
import org.example.rental_car.response.APIResponse;
import org.example.rental_car.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Url.REVIEW)
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping(Url.ADD)
    public ResponseEntity<APIResponse> createReview(@RequestBody ReviewRequest reviewRequest,
                                                    @RequestParam long customerId,
                                                    @RequestParam long carId) {
        Review review = reviewService.saveReview(reviewRequest, customerId, carId);
        return ResponseEntity.ok(new APIResponse("SUCCESS", review));
    }

    @GetMapping(Url.GET_BY_ID)
    public ResponseEntity<APIResponse> getReviewById(@PathVariable long id) {
        Review review = reviewService.getReviewById(id);
        return ResponseEntity.ok(new APIResponse("FOUND", review));
    }

    @GetMapping(Url.GET_ALL)
    public ResponseEntity<APIResponse> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(new APIResponse("FOUND", reviews));
    }

    @GetMapping(Url.GET_BY_CAR_ID)
    public ResponseEntity<APIResponse> getReviewByCarId(@PathVariable long carId,
                                                        @RequestParam(defaultValue = "0") int size,
                                                        @RequestParam(defaultValue = "5") int page) {
        Page<Review> reviews = reviewService.getReviewsByCarId(carId, size, page);
        return ResponseEntity.ok(new APIResponse("FOUND", reviews));
    }

    @GetMapping(Url.GET_BY_CUSTOMER_ID)
    public ResponseEntity<APIResponse> getReviewsByCustomerId(@PathVariable long customerId,
                                                              @RequestParam(defaultValue = "0") int size,
                                                              @RequestParam(defaultValue = "5") int page) {
        Page<Review> reviews = reviewService.getReviewsByCustomerId(customerId, size, page);
        return ResponseEntity.ok(new APIResponse("FOUND", reviews));
    }

    @PutMapping(Url.UPDATE_BY_ID)
    public ResponseEntity<APIResponse> updateReviewById(@PathVariable long id,
                                                        @RequestBody ReviewRequest reviewRequest) {
        Review review = reviewService.updateReview(reviewRequest, id);
        return ResponseEntity.ok(new APIResponse("UPDATE_SUCCESS", review));
    }

    @DeleteMapping(Url.DELETE_BY_ID)
    public ResponseEntity<APIResponse> deleteReviewById(@PathVariable long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/car")
    public ResponseEntity<APIResponse> getReviewByCustomerIdAndCarId(@RequestParam long customerId, @RequestParam long carId) {
        Review review = reviewService.getReviewByCustomerIdAndCarId(customerId, carId);
        return ResponseEntity.ok(new APIResponse("FOUND", review));
    }

}
