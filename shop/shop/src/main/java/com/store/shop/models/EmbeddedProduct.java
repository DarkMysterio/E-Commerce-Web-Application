package com.store.shop.models;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

/**
 * A minimal copy of product info to embed in a user's favorites or cart.
 */
public class EmbeddedProduct {

    // A separate unique ID for this embedded item instance
    private String embeddedItemId;  // always set!

    // Original product info
    private String productId;       // store original product's _id as string
    private String name;
    private String category;
    private double price;
    private String image;

    public EmbeddedProduct() {}

    public EmbeddedProduct(String embeddedItemId, String productId, String name,
                           String category, double price, String image) {
        this.embeddedItemId = embeddedItemId;
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.image = image;
    }

    public String getEmbeddedItemId() {
        return embeddedItemId;
    }

    public void setEmbeddedItemId(String embeddedItemId) {
        this.embeddedItemId = embeddedItemId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
}
