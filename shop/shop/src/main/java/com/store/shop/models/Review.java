package com.store.shop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    private ObjectId id;

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", productId=" + productId +
                ", userId=" + userId +
                ", body='" + body + '\'' +
                ", rating=" + rating +
                '}';
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getProductId() {
        return productId;
    }

    public void setProductId(ObjectId productId) {
        this.productId = productId;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    private ObjectId productId; // which product
    private ObjectId userId;    // which user wrote it

    private String body;        // textual comment
    private int rating;         // 1..5
}
