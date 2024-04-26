package com.jesscahelen.wishlist.domain.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.jesscahelen.wishlist.domain.model.Wishlist;

public interface WishlistRepository extends MongoRepository<Wishlist, String> {

    @Query(value = "{ 'client.clientId' : ?0, 'products.productId' : ?1 }", exists = true)
    boolean existsProductInWishlist(String clientId, String productId);

    @Query(value = "{ 'client.clientId' : ?0 }", exists = true)
    boolean existsClientByClientId(String clientId);

    @Query(value = "{ 'client.clientId' : ?0 }")
    Wishlist findByClientId(String clientId);
}