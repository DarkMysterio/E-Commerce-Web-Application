package com.store.shop.controllers;

import com.store.shop.models.Product;
import com.store.shop.services.RecommendationService;
import com.store.shop.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "http://localhost:3000")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private ReviewService reviewService;

    /**
     * Original endpoint (returns raw Product list without rating)
     */
    @GetMapping("/{username}")
    public ResponseEntity<List<Product>> getRecommendedProducts(@PathVariable String username) {
        List<Product> recommended = recommendationService.getRecommendationsForUser(username);
        return ResponseEntity.ok(recommended);
    }

    /**
     * NEW endpoint: recommended products WITH average rating
     * GET /api/recommendations/{username}/with-ratings
     */
    @GetMapping("/{username}/with-ratings")
    public ResponseEntity<List<Map<String, Object>>> getRecommendedProductsWithRatings(@PathVariable String username) {
        List<Product> recommended = recommendationService.getRecommendationsForUser(username);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Product p : recommended) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("_id", p.getId().toHexString());
            map.put("name", p.getName());
            map.put("category", p.getCategory());
            map.put("price", p.getPrice());
            map.put("image", p.getImage());

            double avgRating = reviewService.getAverageRating(p.getId());
            map.put("averageRating", avgRating);

            result.add(map);
        }

        return ResponseEntity.ok(result);
    }
}
