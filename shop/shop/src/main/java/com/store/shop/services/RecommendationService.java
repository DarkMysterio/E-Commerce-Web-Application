package com.store.shop.services;

import com.store.shop.models.EmbeddedProduct;
import com.store.shop.models.Product;
import com.store.shop.models.User;
import com.store.shop.repositories.ProductRepository;
import com.store.shop.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public RecommendationService(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    /**
     * Return recommended products for the user based on categories in their favorites.
     */
    public List<Product> getRecommendationsForUser(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            return Collections.emptyList();
        }
        User user = userOpt.get();
        if (user.getFavorites() == null || user.getFavorites().isEmpty()) {
            return Collections.emptyList();
        }

        // Gather categories from favorites
        Set<String> favoriteCategories = new HashSet<>();
        for (EmbeddedProduct fav : user.getFavorites()) {
            if (fav.getCategory() != null) {
                favoriteCategories.add(fav.getCategory());
            }
        }

        // Exclude exact favorites from recommendations so we don't show duplicates
        Set<String> favoriteProductIds = user.getFavorites().stream()
                .map(EmbeddedProduct::getProductId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // For each category, pick up to 3 products
        List<Product> recommended = new ArrayList<>();
        for (String cat : favoriteCategories) {
            List<Product> catProducts = productRepository.findByCategory(cat);

            // exclude products the user already has
            catProducts = catProducts.stream()
                    .filter(p -> !favoriteProductIds.contains(p.getId().toHexString()))
                    .collect(Collectors.toList());

            if (catProducts.size() > 3) {
                catProducts = catProducts.subList(0, 3);
            }
            recommended.addAll(catProducts);
        }

        return recommended;
    }
}
