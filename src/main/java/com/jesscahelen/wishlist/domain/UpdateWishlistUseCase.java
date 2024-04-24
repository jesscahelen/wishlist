package com.jesscahelen.wishlist.domain;

import com.jesscahelen.wishlist.domain.model.Wishlist;

public interface UpdateWishlistUseCase {

    void removeProductFromWishlist(String clientId, String productId) throws Exception;
    Wishlist addProductToWishlist(String clientId, String productId) throws Exception;
}