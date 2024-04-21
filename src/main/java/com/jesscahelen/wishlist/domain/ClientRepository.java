package com.jesscahelen.wishlist.domain;

import com.jesscahelen.wishlist.domain.model.Product;
import org.bson.Document;

import java.util.Set;
import java.util.UUID;

public interface ClientRepository {

    void save(Document client);
    void addProductToWishlist(UUID clientId, UUID productId);
    UUID removeProductFromWishlist(UUID clientId, UUID productId);
    Set<Product> findWishlistProductsByClientId(UUID clientId);
    boolean isProductInClientWishlist(UUID clientId, UUID productId);
}