package com.jesscahelen.wishlist.domain;

import java.util.HashSet;

import com.jesscahelen.wishlist.domain.model.Product;
import com.jesscahelen.wishlist.domain.model.Wishlist;

public interface GetClientUseCase {
    HashSet<Product> getAllProductsFromClient(String clientId);

    Boolean isProductInWishlist(String clientId, String productId);

    Wishlist getWishlistByClientId(String clientId);
}