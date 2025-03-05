package com.store.shop.services;

import com.store.shop.models.EmbeddedProduct;
import com.store.shop.models.Product;
import com.store.shop.models.User;
import com.store.shop.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1) Return a user by username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // 2) Save user with non-null favorites/cart
    public User saveUser(User user) {
        if (user.getFavorites() == null) {
            user.setFavorites(new ArrayList<>());
        }
        if (user.getCart() == null) {
            user.setCart(new ArrayList<>());
        }
        return userRepository.save(user);
    }

    // --- MIGRATION: ensure embeddedItemId is not null for all items in a list:
    private boolean fixEmbeddedIds(List<EmbeddedProduct> items) {
        boolean changed = false;
        for (EmbeddedProduct ep : items) {
            if (ep.getEmbeddedItemId() == null) {
                // Assign a new random ID
                ep.setEmbeddedItemId(new ObjectId().toHexString());
                changed = true;
            }
        }
        return changed;
    }

    // --- Favorites ---

    // 3) Add product to favorites with a guaranteed embeddedItemId
    public void addProductToFavorites(String username, Product productParam) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getFavorites() == null) {
                user.setFavorites(new ArrayList<>());
            }
            // Generate a new ID for this embedded item
            String newEmbeddedId = new ObjectId().toHexString();

            EmbeddedProduct embedded = new EmbeddedProduct();
            embedded.setEmbeddedItemId(newEmbeddedId);
            if (productParam.getId() != null) {
                embedded.setProductId(productParam.getId().toHexString());
            } else {
                embedded.setProductId("NoMongoId");
            }
            embedded.setName(productParam.getName());
            embedded.setCategory(productParam.getCategory());
            embedded.setPrice(productParam.getPrice());
            embedded.setImage(productParam.getImage());

            user.getFavorites().add(embedded);
            userRepository.save(user);
        }
    }

    // 4) Get favorites, auto-fix old items with null embedded IDs
    public List<EmbeddedProduct> getFavorites(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            boolean changed = fixEmbeddedIds(user.getFavorites());
            if (changed) {
                // At least one item had null ID, we assigned it => re-save
                userRepository.save(user);
            }
            return user.getFavorites();
        }
        return List.of();
    }

    // 5) Remove from favorites by embeddedItemId
    public void removeProductFromFavorites(String username, String embeddedItemId) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<EmbeddedProduct> updatedList = user.getFavorites().stream()
                    .filter(item -> !embeddedItemId.equals(item.getEmbeddedItemId()))
                    .toList();
            user.setFavorites(updatedList);
            userRepository.save(user);
        }
    }

    // --- Cart ---

    // 6) Add product to cart with a guaranteed embeddedItemId
    public void addProductToCart(String username, Product productParam) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getCart() == null) {
                user.setCart(new ArrayList<>());
            }
            String newEmbeddedId = new ObjectId().toHexString();

            EmbeddedProduct embedded = new EmbeddedProduct();
            embedded.setEmbeddedItemId(newEmbeddedId);
            if (productParam.getId() != null) {
                embedded.setProductId(productParam.getId().toHexString());
            } else {
                embedded.setProductId("NoMongoId");
            }
            embedded.setName(productParam.getName());
            embedded.setCategory(productParam.getCategory());
            embedded.setPrice(productParam.getPrice());
            embedded.setImage(productParam.getImage());

            user.getCart().add(embedded);
            userRepository.save(user);
        }
    }

    // 7) Get cart, auto-fix old items with null embedded IDs
    public List<EmbeddedProduct> getCart(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            boolean changed = fixEmbeddedIds(user.getCart());
            if (changed) {
                userRepository.save(user);
            }
            return user.getCart();
        }
        return List.of();
    }

    // 8) Remove from cart by embeddedItemId
    public void removeProductFromCart(String username, String embeddedItemId) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<EmbeddedProduct> updatedList = user.getCart().stream()
                    .filter(item -> !embeddedItemId.equals(item.getEmbeddedItemId()))
                    .toList();
            user.setCart(updatedList);
            userRepository.save(user);
        }
    }
}
