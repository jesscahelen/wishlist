package com.jesscahelen.wishlist.entrypoint.http;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jesscahelen.wishlist.domain.GetClientUseCase;
import com.jesscahelen.wishlist.domain.UpdateWishlistUseCase;
import com.jesscahelen.wishlist.domain.exception.FullWishlistException;
import com.jesscahelen.wishlist.domain.exception.ProductNotFoundException;
import com.jesscahelen.wishlist.domain.model.Product;
import com.jesscahelen.wishlist.domain.model.Wishlist;
import com.jesscahelen.wishlist.entrypoint.http.dto.ProductResponseDTO;
import com.jesscahelen.wishlist.entrypoint.http.dto.WishlistDTO;
import com.jesscahelen.wishlist.fixtures.WishlistFixture;

import static com.jesscahelen.wishlist.fixtures.WishlistFixture.createWishlistDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WishlistControllerTest {

    @Mock
    private GetClientUseCase getClientUseCase;

    @Mock
    private UpdateWishlistUseCase updateWishlistUseCase;

    @InjectMocks
    private WishlistController wishlistController;

    @Test
    void shouldReturnAllProductsFromWishlist() {
        String clientId = UUID.randomUUID().toString();
        Product product1 = WishlistFixture.createProduct(UUID.randomUUID().toString());
        Product product2 = WishlistFixture.createProduct(UUID.randomUUID().toString());
        HashSet products = new HashSet(Set.of(product1, product2));

        when(getClientUseCase.getAllProductsFromClient(clientId)).thenReturn(products);

        ResponseEntity<HashSet<Product>> response = wishlistController.getAllProductsFromClient(clientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }

    @Test
    void shouldReturnEmptyWhenRetrieveProductsFromWishlist() {
        String clientId = UUID.randomUUID().toString();
        HashSet products = new HashSet(Set.of());

        when(getClientUseCase.getAllProductsFromClient(clientId)).thenReturn(products);

        ResponseEntity<HashSet<Product>> response = wishlistController.getAllProductsFromClient(clientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }


    @Test
    void shouldReturnTrueWhenVerifyIfProductIsInWishlist() {
        String clientId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();

        when(getClientUseCase.isProductInWishlist(clientId, productId)).thenReturn(true);

        ResponseEntity<Boolean> response = wishlistController.isProductInWishlist(clientId, productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void shouldReturnFalseWhenVerifyIfProductIsInWishlist() {
        String clientId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();

        when(getClientUseCase.isProductInWishlist(clientId, productId)).thenReturn(false);

        ResponseEntity<Boolean> response = wishlistController.isProductInWishlist(clientId, productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    void shouldRemoveProductFromWishlist() {
        String clientId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();
        ProductResponseDTO productResponseDTO = ProductResponseDTO.builder()
                .success(productId)
                .build();

        ResponseEntity<ProductResponseDTO> response = wishlistController.removeProductInWishlist(clientId, productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productResponseDTO, response.getBody());
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonExistentProduct() {
        String clientId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();
        String errorMessage = "Product not found in client's wishlist.";
        ProductResponseDTO productResponseDTO = ProductResponseDTO.builder()
                .failed(productId)
                .message(errorMessage)
                .build();

        doThrow(new ProductNotFoundException(errorMessage)).when(updateWishlistUseCase).removeProductFromWishlist(clientId, productId);

        ResponseEntity<ProductResponseDTO> response = wishlistController.removeProductInWishlist(clientId, productId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(productResponseDTO, response.getBody());
    }

    @Test
    void shouldAddProductToWishlist() {
        String productId = UUID.randomUUID().toString();
        Product product = WishlistFixture.createProduct(productId);
        Wishlist wishlist = WishlistFixture.build(new HashSet<>(Set.of(product)));
        String clientId = wishlist.getClient().getClientId();
        WishlistDTO wishlistDTO = createWishlistDTO(clientId, productId);

        when(updateWishlistUseCase.addProductToWishlist(clientId, productId)).thenReturn(wishlist);

        ResponseEntity<Object> response = wishlistController.addProductInWishlist(wishlistDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(wishlist, response.getBody());
    }

    @Test
    void shouldNotAddProductToWishlistWhenWishlistIsFull() {
        String clientId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();
        WishlistDTO wishlistDTO = createWishlistDTO(clientId, productId);
        String errorMessage = "Client's wishlist has 20 products. To add a new product it is necessary to remove an existent one.";
        ProductResponseDTO productResponseDTO = ProductResponseDTO.builder()
                .failed(productId)
                .message(errorMessage)
                .build();

        doThrow(new FullWishlistException(errorMessage)).when(updateWishlistUseCase).addProductToWishlist(clientId, productId);

        ResponseEntity<Object> response = wishlistController.addProductInWishlist(wishlistDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(productResponseDTO, response.getBody());
    }
}