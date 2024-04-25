package com.jesscahelen.wishlist.entrypoint.http;

import java.util.HashSet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jesscahelen.wishlist.domain.GetClientUseCase;
import com.jesscahelen.wishlist.domain.UpdateWishlistUseCase;
import com.jesscahelen.wishlist.domain.model.Product;
import com.jesscahelen.wishlist.domain.model.Wishlist;
import com.jesscahelen.wishlist.entrypoint.http.dto.WishlistDTO;

@RestController
@RequestMapping("wishlist")
public class WishlistController {

    private final GetClientUseCase getClientUseCase;
    private final UpdateWishlistUseCase updateWishlistUseCase;

    public WishlistController(GetClientUseCase getClientUseCase, UpdateWishlistUseCase updateWishlistUseCase) {
        this.getClientUseCase = getClientUseCase;
        this.updateWishlistUseCase = updateWishlistUseCase;
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<HashSet<Product>> getAllProductsFromClient(@PathVariable String clientId) {
        return ResponseEntity.ok(getClientUseCase.getAllProductsFromClient(clientId));
    }

    @GetMapping("/client/{clientId}/product/{productId}")
    public ResponseEntity<Boolean> isProductInWishlist(@PathVariable String clientId, @PathVariable String productId) {
        return ResponseEntity.ok(getClientUseCase.isProductInWishlist(clientId, productId));
    }

    @DeleteMapping("/client/{clientId}/product/{productId}")
    public ResponseEntity<String> removeProductInWishlist(@PathVariable String clientId, @PathVariable String productId) {
        try {
            updateWishlistUseCase.removeProductFromWishlist(clientId, productId);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(productId);
    }

    @PutMapping("/")
    public ResponseEntity<Wishlist> addProductInWishlist(@RequestBody WishlistDTO wishlistDTO) {
        Wishlist wishlist;
        try {
            wishlist = updateWishlistUseCase.addProductToWishlist(wishlistDTO.getClientId(), wishlistDTO.getProductId());
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(wishlist);
    }
}