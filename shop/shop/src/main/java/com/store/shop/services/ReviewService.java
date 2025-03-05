package com.store.shop.services;

import com.store.shop.models.Review;
import com.store.shop.repositories.ReviewRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Save a new review, ensuring each user can only review a product once.
     * If the user tries to review the same product again, we throw a RuntimeException.
     */
    public Review saveReview(Review review) {
        // 1) Check if userId + productId are set
        if (review.getUserId() != null && review.getProductId() != null) {
            // 2) See if a review already exists for (userId, productId)
            reviewRepository
                    .findByUserIdAndProductId(review.getUserId(), review.getProductId())
                    .ifPresent(existingReview -> {
                        throw new RuntimeException("User already reviewed this product!");
                    });
        }

        // 3) Validate rating
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5!");
        }

        // 4) Save in DB
        return reviewRepository.save(review);
    }

    /**
     * Return all reviews for a product
     */
    public List<Review> getReviewsForProduct(ObjectId productId) {
        return reviewRepository.findByProductId(productId);
    }

    /**
     * Compute the average rating for a product via aggregation
     */
    public double getAverageRating(ObjectId productId) {
        // aggregator pipeline: match => group => avg(rating)
        Aggregation agg = newAggregation(
                match(Criteria.where("productId").is(productId)),
                group().avg("rating").as("avgRating")
        );
        AggregationResults<AvgRatingResult> results =
                mongoTemplate.aggregate(agg, "reviews", AvgRatingResult.class);

        AvgRatingResult result = results.getUniqueMappedResult();
        if (result == null || result.avgRating == null) {
            return 0.0;
        }
        return result.avgRating;
    }

    // Helper class for aggregator
    static class AvgRatingResult {
        Double avgRating;

        public Double getAvgRating() { return avgRating; }
        public void setAvgRating(Double avgRating) { this.avgRating = avgRating; }
    }
}
