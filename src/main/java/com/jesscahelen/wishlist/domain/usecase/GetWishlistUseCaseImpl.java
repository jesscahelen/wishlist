package com.jesscahelen.wishlist.domain.usecase;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.jesscahelen.wishlist.domain.GetClientUseCase;
import com.jesscahelen.wishlist.domain.infrastructure.repository.WishlistRepository;
import com.jesscahelen.wishlist.domain.model.Product;
import com.jesscahelen.wishlist.domain.model.Wishlist;

@Service
public class GetWishlistUseCaseImpl implements GetClientUseCase {

    private final WishlistRepository wishlistRepository;

    public GetWishlistUseCaseImpl(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public HashSet<Product> getAllProductsFromClient(String clientId) {
        Wishlist wishlist = wishlistRepository.findByClientId(clientId);
        return wishlist != null ? wishlist.getProducts() : new HashSet<>(Set.of());
    }

    public Boolean isProductInWishlist(String clientId, String productId) {
        return wishlistRepository.existsProductInWishlist(clientId, productId);
    }

    public Wishlist getWishlistByClientId(String clientId) {
        return wishlistRepository.findByClientId(clientId);
    }
}