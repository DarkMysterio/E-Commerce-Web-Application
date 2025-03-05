package com.store.shop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private ObjectId id;
    private String username;
    private String password;

    // Instead of references, store an embedded product list
    private List<EmbeddedProduct> favorites;
    private List<EmbeddedProduct> cart;

    // getters/setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<EmbeddedProduct> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<EmbeddedProduct> favorites) {
        this.favorites = favorites;
    }

    public List<EmbeddedProduct> getCart() {
        return cart;
    }

    public void setCart(List<EmbeddedProduct> cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", favorites=" + favorites +
                ", cart=" + cart +
                '}';
    }
}
