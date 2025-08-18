package com.eventmanagement.backend.service;

import com.eventmanagement.backend.entity.Review;
import com.eventmanagement.backend.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private NotificationService notificationService;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    public Review saveReview(Review review) {
        Review saved = reviewRepository.save(review);
        if (review.getUser() != null) {
            notificationService.notifyAdminAndEmail(
                "REVIEW_SUBMITTED",
                "New review submitted by: " + review.getUser().getUsername(),
                "New Review Submitted",
                "A new review has been submitted by user: " + review.getUser().getUsername()
            );
        }
        return saved;
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public Review updateReview(Long id, Review updatedReview) {
        return reviewRepository.findById(id).map(review -> {
            review.setRating(updatedReview.getRating());
            review.setComment(updatedReview.getComment());
            // Add more fields as needed
            return reviewRepository.save(review);
        }).orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + id));
    }
}
