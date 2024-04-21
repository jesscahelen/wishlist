package com.jesscahelen.wishlist.domain.model;

import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Wishlist {

    String wishlistId;
    Set<Product> products;
}