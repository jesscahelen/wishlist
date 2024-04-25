package com.jesscahelen.wishlist.domain.usecase;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jesscahelen.wishlist.domain.GetClientUseCase;
import com.jesscahelen.wishlist.domain.UpdateWishlistUseCase;
import com.jesscahelen.wishlist.domain.exception.FullWishlistException;
import com.jesscahelen.wishlist.domain.exception.ProductNotFoundException;
import com.jesscahelen.wishlist.domain.infrastructure.repository.WishlistRepository;
import com.jesscahelen.wishlist.domain.model.Client;
import com.jesscahelen.wishlist.domain.model.Product;
import com.jesscahelen.wishlist.domain.model.Wishlist;

@Service
public class UpdateWishlistUseCaseImpl implements UpdateWishlistUseCase {

    private final WishlistRepository wishlistRepository;
    private final GetClientUseCase getClientUseCase;
    private final Logger LOGGER = LogManager.getLogger(UpdateWishlistUseCaseImpl.class);

    public UpdateWishlistUseCaseImpl(WishlistRepository wishlistRepository, GetClientUseCase getClientUseCase) {
        this.wishlistRepository = wishlistRepository;
        this.getClientUseCase = getClientUseCase;
    }

    @Transactional
    public void removeProductFromWishlist(String clientId, String productId) throws ProductNotFoundException {
        if (getClientUseCase.isProductInWishlist(clientId, productId)) {
            Wishlist wishlist = getClientUseCase.getWishlistByClientId(clientId);
            Product product = Product.builder().productId(productId).build();
            wishlist.getProducts().remove(product);
            wishlistRepository.save(wishlist);
        } else {
            LOGGER.error(String.format("Product id: %s not found in client id: %s wishlist.", productId, clientId));
            throw new ProductNotFoundException("Product not found in client's wishlist.");
        }
    }

    public Wishlist addProductToWishlist(String clientId, String productId) throws FullWishlistException {
        boolean doesClientExists = wishlistRepository.existsClientByClientId(clientId);
        Wishlist wishlist;
        if (!doesClientExists) {
            wishlist = createWishlist(clientId, productId);
            LOGGER.info(String.format("Wishlist id: %s created.", wishlist.getWishlistId()));
        } else {
            if (wishlistRepository.countProductsInWishlistByClientId(clientId) >= 20) {
                LOGGER.error(String.format("Client id: %s wishlist has 20 products.", clientId));
                throw new FullWishlistException("Client's wishlist has 20 products. To add a new product it is necessary to remove an existent one.");
            }
            wishlist = wishlistRepository.findByClientId(clientId);
            wishlist.getProducts().add(createProduct(productId));
        }
        wishlistRepository.save(wishlist);
        return wishlist;
    }

    private Wishlist createWishlist(String clientId, String productId) {
        return Wishlist.builder()
                .wishlistId(UUID.randomUUID().toString())
                .client(Client.builder()
                        .clientId(clientId)
                        .build())
                .products(new HashSet<>(Set.of(Product.builder()
                        .productId(productId)
                        .build())))
                .build();
    }

    private Product createProduct(String productId) {
        return Product.builder()
                .productId(productId)
                .build();
    }
}