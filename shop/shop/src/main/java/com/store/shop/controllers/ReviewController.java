package com.store.shop.controllers;

import com.store.shop.models.Review;
import com.store.shop.services.ReviewService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * POST /api/reviews
     *
     * JSON Body: {
     *   "userId": "64ed12345...",  (the user's ObjectId as a hex string)
     *   "productId": "64ed67890...", (the product's ObjectId as a hex string)
     *   "rating": 4,
     *   "body": "Great product"
     * }
     *
     * If the user tries to post a second review for the same product,
     * the service throws an exception => we return 409 Conflict.
     */
    @PostMapping
    public ResponseEntity<?> addReview(@RequestBody Review review) {
        try {
            // Basic check
            if (review.getUserId() == null || review.getProductId() == null) {
                return ResponseEntity.badRequest().body("Missing userId or productId");
            }

            // Attempt to save. May throw a RuntimeException if user already reviewed.
            Review saved = reviewService.saveReview(review);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            // e.g. "User already reviewed this product!"
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }



// GET all reviews for a product by its ID
    @GetMapping("/by-product/{productId}")
    public ResponseEntity<List<Review>> getReviewsForProduct(@PathVariable String productId) {
        // convert string to ObjectId
        ObjectId pid = new ObjectId(productId);
        List<Review> found = reviewService.getReviewsForProduct(pid);
        return ResponseEntity.ok(found);
    }

}
