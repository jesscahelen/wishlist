package com.jesscahelen.wishlist.domain.usecase;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jesscahelen.wishlist.domain.GetClientUseCase;
import com.jesscahelen.wishlist.domain.exception.FullWishlistException;
import com.jesscahelen.wishlist.domain.exception.ProductNotFoundException;
import com.jesscahelen.wishlist.domain.infrastructure.repository.WishlistRepository;
import com.jesscahelen.wishlist.domain.model.Product;
import com.jesscahelen.wishlist.domain.model.Wishlist;
import com.jesscahelen.wishlist.fixtures.WishlistFixture;

import static com.jesscahelen.wishlist.fixtures.WishlistFixture.createProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateWishlistUseCaseTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private GetClientUseCase getClientUseCase;

    @InjectMocks
    private UpdateWishlistUseCaseImpl wishlistUseCase;

    @Test
    public void shouldRemoveProductFromWishlistWhenProductExists() {
        Product product = createProduct(UUID.randomUUID().toString());
        Wishlist wishlist = WishlistFixture.build(new HashSet<>(Set.of(product)));

        String clientId = wishlist.getClient().getClientId();
        String productId = product.getProductId();

        when(getClientUseCase.isProductInWishlist(clientId, productId)).thenReturn(true);
        when(getClientUseCase.getWishlistByClientId(clientId)).thenReturn(wishlist);

        wishlistUseCase.removeProductFromWishlist(clientId, productId);

        assertFalse(wishlist.getProducts().contains(product));
        verify(wishlistRepository, times(1)).save(wishlist);
    }

    @Test
    public void givenProductDoesNotExistInWishlistShouldThrowExceptionWhenRemoveProductFromWishlist() {
        String clientId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();

        when(getClientUseCase.isProductInWishlist(clientId, productId)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> wishlistUseCase.removeProductFromWishlist(clientId, productId));
    }

    @Test
    public void shouldSaveNewClientWhenAddProductToWishlist() {
        String clientId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();

        when(wishlistRepository.existsClientByClientId(clientId)).thenReturn(false);

        Wishlist result = wishlistUseCase.addProductToWishlist(clientId, productId);

        assertEquals(clientId, result.getClient().getClientId());
        assertEquals(1, result.getProducts().size());
        verify(wishlistRepository, times(1)).save(result);
    }

    @Test
    public void shouldAddProductToWishlistWhenClientExists() {
        Product product = createProduct(UUID.randomUUID().toString());
        Wishlist wishlist = WishlistFixture.build(new HashSet<>(Set.of(product)));
        String clientId = wishlist.getClient().getClientId();
        String productId = product.getProductId();

        when(wishlistRepository.existsClientByClientId(clientId)).thenReturn(true);
        when(wishlistRepository.findByClientId(clientId)).thenReturn(wishlist);

        Wishlist result = wishlistUseCase.addProductToWishlist(clientId, productId);

        assertEquals(clientId, result.getClient().getClientId());
        assertEquals(1, result.getProducts().size());
        verify(wishlistRepository, times(1)).save(result);
    }

    @Test
    public void shouldNotSaveNewProductInWishlistWhenWishlistIsFull() {
        Wishlist wishlist = WishlistFixture.build(new HashSet<>(Set.of()));
        for (int i = 0; i < 20; i++) {
            wishlist.getProducts().add(createProduct(UUID.randomUUID().toString()));
        }
        String clientId = wishlist.getClient().getClientId();
        String newProductId = UUID.randomUUID().toString();

        when(wishlistRepository.existsClientByClientId(clientId)).thenReturn(true);
        when(wishlistRepository.findByClientId(clientId)).thenReturn(wishlist);

        assertThrows(FullWishlistException.class, () -> wishlistUseCase.addProductToWishlist(clientId, newProductId));
    }
}