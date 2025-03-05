package com.store.shop.controllers;

import com.store.shop.models.Product;
import com.store.shop.models.Review;
import com.store.shop.services.ProductService;
import com.store.shop.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    // 1) Get all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // 2) Get categories
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = List.of(
                "Laptop, Tablete & Telefoane",
                "PC, Periferice & Software",
                "TV, Audio-Video & Foto",
                "Electrocasnice & Climatizare",
                "Gaming, Carti & Birotica",
                "Bacanie",
                "Fashion",
                "Ingrijire personala si Cosmetica",
                "Casa, gradina si bricolaj",
                "Sport & Travel",
                "Auto, Moto & RCA",
                "Jucarii, copii si bebe"
        );
        return ResponseEntity.ok(categories);
    }

    /**
     * 3) Get products by category
     * We return a list of maps so that each product has "_id" as a hex string.
     */
    @GetMapping("/by-category/{category}")
    public ResponseEntity<List<Map<String, Object>>> getProductsByCategory(@PathVariable String category) {
        // decode URL if it has spaces, ampersands, etc.
        String decoded = URLDecoder.decode(category, StandardCharsets.UTF_8).trim();
        List<Product> products = productService.getProductsByCategory(decoded);

        // Transform each Product -> Map, ensuring _id is a string
        List<Map<String, Object>> result = new ArrayList<>();
        for (Product p : products) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("_id", p.getId().toHexString()); // <--- The crucial part
            map.put("name", p.getName());
            map.put("category", p.getCategory());
            map.put("price", p.getPrice());
            map.put("image", p.getImage());
            result.add(map);
        }

        return ResponseEntity.ok(result);
    }

    // 4) Get product by ID (simple)
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 5) Get all products with average rating
     */
    @GetMapping("/with-ratings")
    public ResponseEntity<List<Map<String, Object>>> getAllProductsWithRatings() {
        List<Product> products = productService.getAllProducts();
        List<Map<String, Object>> result = products.stream().map(prod -> {
            double avg = reviewService.getAverageRating(prod.getId());
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("_id", prod.getId().toHexString());
            map.put("name", prod.getName());
            map.put("category", prod.getCategory());
            map.put("price", prod.getPrice());
            map.put("image", prod.getImage());
            map.put("averageRating", avg);
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * 6) Get single product with average rating + reviews
     */
    @GetMapping("/{id}/with-rating")
    public ResponseEntity<Map<String, Object>> getProductByIdWithRating(@PathVariable String id) {
        return productService.getProductById(id).map(prod -> {
            double avg = reviewService.getAverageRating(prod.getId());
            List<Review> reviews = reviewService.getReviewsForProduct(prod.getId());

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("_id", prod.getId().toHexString());
            map.put("name", prod.getName());
            map.put("category", prod.getCategory());
            map.put("price", prod.getPrice());
            map.put("image", prod.getImage());
            map.put("averageRating", avg);
            map.put("reviews", reviews);

            return ResponseEntity.ok(map);
        }).orElse(ResponseEntity.notFound().build());
    }
}
