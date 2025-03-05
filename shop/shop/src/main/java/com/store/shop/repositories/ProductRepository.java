package com.store.shop.repositories;

import com.store.shop.models.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, ObjectId> {

    // Original exact match (optional)
    List<Product> findByCategory(String category);

    // NEW: case-insensitive category finder
    List<Product> findByCategoryIgnoreCase(String category);
}
