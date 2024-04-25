package com.jesscahelen.wishlist.domain.usecase;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jesscahelen.wishlist.domain.infrastructure.repository.WishlistRepository;
import com.jesscahelen.wishlist.domain.model.Product;
import com.jesscahelen.wishlist.domain.model.Wishlist;
import com.jesscahelen.wishlist.fixtures.WishlistFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetWishlistUseCaseTest {
    @Mock
    private WishlistRepository wishlistRepository;

    @InjectMocks
    private GetWishlistUseCaseImpl wishlistUseCase;

    @Test
    public void shouldReturnAllProductsForClient() {
        Product product = WishlistFixture.createProduct(UUID.randomUUID().toString());
        Wishlist wishlist = WishlistFixture.build(new HashSet<>(Set.of(product)));
        String clientId = wishlist.getClient().getClientId();

        when(wishlistRepository.findByClientId(clientId)).thenReturn(wishlist);

        HashSet<Product> result = wishlistUseCase.getAllProductsFromClient(clientId);

        assertEquals(wishlist.getProducts(), result);
    }

    @Test
    public void shouldReturnNoProductsForClientWhenWishlistIsEmpty() {
        Wishlist wishlist = WishlistFixture.build(new HashSet<>(Set.of()));
        String clientId = wishlist.getClient().getClientId();

        when(wishlistRepository.findByClientId(clientId)).thenReturn(wishlist);

        HashSet<Product> result = wishlistUseCase.getAllProductsFromClient(clientId);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnNoProductsForClientWhenWishlistIsNull() {
        String clientId = UUID.randomUUID().toString();

        when(wishlistRepository.findByClientId(clientId)).thenReturn(null);

        HashSet<Product> result = wishlistUseCase.getAllProductsFromClient(clientId);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnTrueWhenVerifyingIfProductIsInWishlist() {
        Product product = WishlistFixture.createProduct(UUID.randomUUID().toString());
        Wishlist wishlist = WishlistFixture.build(new HashSet<>(Set.of(product)));
        String clientId = wishlist.getClient().getClientId();

        when(wishlistRepository.existsProductInWishlist(clientId, product.getProductId())).thenReturn(true);

        Boolean result = wishlistUseCase.isProductInWishlist(clientId, product.getProductId());

        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenVerifyingIfProductIsInWishlist() {
        Product product = WishlistFixture.createProduct(UUID.randomUUID().toString());
        Wishlist wishlist = WishlistFixture.build(new HashSet<>(Set.of(product)));
        String productIdNonExistent = UUID.randomUUID().toString();
        String clientId = wishlist.getClient().getClientId();

        when(wishlistRepository.existsProductInWishlist(clientId, productIdNonExistent)).thenReturn(false);

        Boolean result = wishlistUseCase.isProductInWishlist(clientId, productIdNonExistent);

        assertFalse(result);
    }

    @Test
    public void shouldReturnWishlistWhenSearchByClientId() {
        Product product = WishlistFixture.createProduct(UUID.randomUUID().toString());
        Wishlist wishlist = WishlistFixture.build(new HashSet<>(Set.of(product)));
        String clientId = wishlist.getClient().getClientId();

        when(wishlistRepository.findByClientId(clientId)).thenReturn(wishlist);

        Wishlist result = wishlistUseCase.getWishlistByClientId(clientId);

        assertEquals(wishlist, result);
    }

    @Test
    public void shouldReturnNullWhenSearchWishlistByClientId() {
        String clientId = UUID.randomUUID().toString();

        when(wishlistRepository.findByClientId(clientId)).thenReturn(null);

        Wishlist result = wishlistUseCase.getWishlistByClientId(clientId);

        assertNull(result);
    }
}