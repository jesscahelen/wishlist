package com.jesscahelen.wishlist.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class Wishlist {

    UUID id;
    Set<Product> products;
}