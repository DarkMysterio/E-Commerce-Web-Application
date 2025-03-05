package com.store.shop.repositories;

import com.store.shop.models.Review;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, ObjectId> {

    List<Review> findByProductId(ObjectId productId);

    Optional<Review> findByUserIdAndProductId(ObjectId userId, ObjectId productId);
}
