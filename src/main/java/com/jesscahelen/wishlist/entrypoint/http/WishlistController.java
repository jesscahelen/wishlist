package com.jesscahelen.wishlist.entrypoint.http;

import java.util.Set;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public Set<Product> getAllProductsFromClient(@PathVariable String clientId) {
        return getClientUseCase.getAllProductsFromClient(clientId);
    }

    @GetMapping("/client/{clientId}/product/{productId}")
    public Boolean isProductInWishlist(@PathVariable String clientId, @PathVariable String productId) {
        return getClientUseCase.isProductInWishlist(clientId, productId);
    }

    @DeleteMapping("/client/{clientId}/product/{productId}")
    public String removeProductInWishlist(@PathVariable String clientId, @PathVariable String productId) throws Exception {
        updateWishlistUseCase.removeProductFromWishlist(clientId, productId);
        return null;
    }

    @PostMapping("/")
    public Wishlist addProductInWishlist(@RequestBody WishlistDTO wishlistDTO) throws Exception {
        return updateWishlistUseCase.addProductToWishlist(wishlistDTO.getClientId(), wishlistDTO.getProductId());
    }
}