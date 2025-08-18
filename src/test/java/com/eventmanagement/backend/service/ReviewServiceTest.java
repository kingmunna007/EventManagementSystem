package com.eventmanagement.backend.service;

import com.eventmanagement.backend.entity.Review;
import com.eventmanagement.backend.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllReviews() {
        Review r1 = new Review(); r1.setId(1L); r1.setRating(5); r1.setComment("Great");
        Review r2 = new Review(); r2.setId(2L); r2.setRating(4); r2.setComment("Good");
        when(reviewRepository.findAll()).thenReturn(Arrays.asList(r1, r2));
        List<Review> reviews = reviewService.getAllReviews();
        assertEquals(2, reviews.size());
    }

    @Test
    void testGetReviewById() {
        Review r = new Review(); r.setId(1L); r.setRating(5); r.setComment("Great");
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(r));
        Optional<Review> found = reviewService.getReviewById(1L);
        assertTrue(found.isPresent());
        assertEquals(5, found.get().getRating());
    }

    @Test
    void testSaveReview() {
        Review r = new Review(); r.setRating(5); r.setComment("Great");
        when(reviewRepository.save(r)).thenReturn(r);
        Review saved = reviewService.saveReview(r);
        assertEquals(5, saved.getRating());
    }

    @Test
    void testDeleteReview() {
        doNothing().when(reviewRepository).deleteById(1L);
        reviewService.deleteReview(1L);
        verify(reviewRepository, times(1)).deleteById(1L);
    }
}
