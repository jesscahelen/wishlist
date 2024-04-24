package com.jesscahelen.wishlist.domain.usecase;

import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jesscahelen.wishlist.domain.GetClientUseCase;
import com.jesscahelen.wishlist.domain.UpdateWishlistUseCase;
import com.jesscahelen.wishlist.domain.infrastructure.repository.WishlistRepository;
import com.jesscahelen.wishlist.domain.model.Client;
import com.jesscahelen.wishlist.domain.model.Product;
import com.jesscahelen.wishlist.domain.model.Wishlist;

@Service
public class UpdateWishlistUseCaseImpl implements UpdateWishlistUseCase {

    private final WishlistRepository wishlistRepository;
    private final GetClientUseCase getClientUseCase;

    public UpdateWishlistUseCaseImpl(WishlistRepository wishlistRepository, GetClientUseCase getClientUseCase) {
        this.wishlistRepository = wishlistRepository;
        this.getClientUseCase = getClientUseCase;
    }

    @Transactional
    public void removeProductFromWishlist(String clientId, String productId) throws Exception {
        if (getClientUseCase.isProductInWishlist(clientId, productId)) {
            Wishlist wishlist = getClientUseCase.getWishlistByClientId(clientId);
            wishlist.getProducts().remove(Product.builder().productId(productId).build());
            wishlistRepository.save(wishlist);
        } else {
//        throw new ProductNotFoundException("Product not found in client's wishlist.");
            throw new Exception("Product not found in client's wishlist.");
        }
    }

    public Wishlist addProductToWishlist(String clientId, String productId) throws Exception {
        boolean doesClientExists = wishlistRepository.existsClientByClientId(clientId);
        Wishlist wishlist;
        if (!doesClientExists) {
            wishlist = createWishlist(clientId, productId);
        } else {
            if (wishlistRepository.countProductsInWishlistByClientId(clientId) >= 20) {
//                throw new FullWishlistException("Client's wishlist has 20 products. To add a new product it is necessary to remove an existent one.");
                throw new Exception("Client's wishlist has 20 products. To add a new product it is necessary to remove an existent one.");
            }
            wishlist = wishlistRepository.findByClientId(clientId);
            wishlist.getProducts().add(createProduct(productId));
        }
        return wishlistRepository.save(wishlist);
    }

    private Wishlist createWishlist(String clientId, String productId) {
        return Wishlist.builder()
                .wishlistId(UUID.randomUUID().toString())
                .client(Client.builder()
                        .clientId(clientId)
                        .build())
                .products(Set.of(Product.builder()
                        .productId(productId)
                        .build()))
                .build();
    }

    private Product createProduct(String productId) {
        return Product.builder()
                        .productId(productId)
                        .build();
    }
}