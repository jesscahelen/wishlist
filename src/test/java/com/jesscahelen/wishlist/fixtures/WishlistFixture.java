package com.jesscahelen.wishlist.fixtures;

import java.util.HashSet;
import java.util.UUID;

import com.jesscahelen.wishlist.domain.model.Client;
import com.jesscahelen.wishlist.domain.model.Product;
import com.jesscahelen.wishlist.domain.model.Wishlist;
import com.jesscahelen.wishlist.entrypoint.http.dto.WishlistDTO;

public class WishlistFixture {

    public static Wishlist build(HashSet<Product> products) {
        return Wishlist.builder()
                .wishlistId(UUID.randomUUID().toString())
                .client(Client.builder()
                        .clientId(UUID.randomUUID().toString())
                        .build())
                .products(products)
                .build();
    }

    public static Product createProduct(String productId) {
        return Product.builder().productId(productId).build();
    }

    public static WishlistDTO createWishlistDTO(String clientId, String productId) {
        return WishlistDTO.builder()
                .clientId(clientId)
                .productId(productId)
                .build();
    }
}