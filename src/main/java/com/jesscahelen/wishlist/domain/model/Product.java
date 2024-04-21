package com.jesscahelen.wishlist.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Product {

    UUID id;
}