package com.store.shop.controllers;

import com.store.shop.models.Product;
import com.store.shop.models.User;
import com.store.shop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        // ensures user.favorites & user.cart are never null
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        Optional<User> existingUser = userService.getUserByUsername(user.getUsername());
        if (existingUser.isPresent() && existingUser.get().getPassword().equals(user.getPassword())) {
            return new ResponseEntity<>(existingUser.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // Add product to favorites
    @PostMapping("/{username}/favorites")
    public ResponseEntity<String> addToFavorites(@PathVariable String username, @RequestBody Product product) {
        userService.addProductToFavorites(username, product);
        return ResponseEntity.ok("Product added to favorites");
    }

    // Remove product from favorites
    @DeleteMapping("/{username}/favorites/{productId}")
    public ResponseEntity<String> removeFromFavorites(@PathVariable String username, @PathVariable String productId) {
        userService.removeProductFromFavorites(username, productId);
        return ResponseEntity.ok("Product removed from favorites");
    }

    // Get all favorites
    @GetMapping("/{username}/favorites")
    public ResponseEntity<List<?>> getFavorites(@PathVariable String username) {
        return ResponseEntity.ok(userService.getFavorites(username));
    }

    // Add product to cart
    @PostMapping("/{username}/cart")
    public ResponseEntity<String> addToCart(@PathVariable String username, @RequestBody Product product) {
        userService.addProductToCart(username, product);
        return ResponseEntity.ok("Product added to cart");
    }

    // Remove product from cart
    @DeleteMapping("/{username}/cart/{productId}")
    public ResponseEntity<String> removeFromCart(@PathVariable String username, @PathVariable String productId) {
        userService.removeProductFromCart(username, productId);
        return ResponseEntity.ok("Product removed from cart");
    }

    // Get all cart items
    @GetMapping("/{username}/cart")
    public ResponseEntity<List<?>> getCart(@PathVariable String username) {
        return ResponseEntity.ok(userService.getCart(username));
    }
}
