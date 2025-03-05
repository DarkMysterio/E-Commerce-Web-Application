package com.store.shop.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)  // optional, helps ignore extra JSON
@Document(collection = "products")
public class Product {
    @Id
    private ObjectId id;
    private String name;
    private String category;
    private double price;
    private String image; // e.g. "/images/<image-name>.jpg"

    @DocumentReference
    private List<Review> reviews;

    public ObjectId getId() {
        return id;
    }

    // Keep your original setter for when you pass an actual ObjectId
    public void setId(ObjectId id) {
        this.id = id;
    }

    // NEW: Overload that takes a String ID and converts it to an ObjectId
    // so that Jackson can properly deserialize { "id": "..." }
    public void setId(String idAsString) {
        if (idAsString != null && !idAsString.isEmpty()) {
            this.id = new ObjectId(idAsString);
        }
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public List<Review> getReviews() {
        return reviews;
    }
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
