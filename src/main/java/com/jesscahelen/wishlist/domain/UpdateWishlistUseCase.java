package com.jesscahelen.wishlist.domain;

import com.jesscahelen.wishlist.domain.exception.FullWishlistException;
import com.jesscahelen.wishlist.domain.exception.ProductNotFoundException;
import com.jesscahelen.wishlist.domain.model.Wishlist;

public interface UpdateWishlistUseCase {

    void removeProductFromWishlist(String clientId, String productId) throws ProductNotFoundException;
    Wishlist addProductToWishlist(String clientId, String productId) throws FullWishlistException;
}