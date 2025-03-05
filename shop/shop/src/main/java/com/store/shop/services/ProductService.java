package com.store.shop.services;

import com.store.shop.models.Product;
import com.store.shop.repositories.ProductRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Fetch products by category, ignoring case.
     */
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }

    /**
     * Create/save a product in the DB.
     */
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Convert the string to ObjectId and find one product by _id.
     * If invalid or not found, returns empty.
     */
    public Optional<Product> getProductById(String idAsString) {
        if (idAsString == null || idAsString.isEmpty()) {
            return Optional.empty();
        }
        try {
            ObjectId oid = new ObjectId(idAsString.trim());
            return productRepository.findById(oid);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
