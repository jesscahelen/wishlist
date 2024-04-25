package com.jesscahelen.wishlist.domain.model;

import java.util.HashSet;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "wishlists")
public class Wishlist {

    @Id
    String wishlistId;
    Client client;
    HashSet<Product> products;
}