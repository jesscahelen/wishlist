package com.jesscahelen.wishlist.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "clients")
public class Client {

    @Id
    String clientId;
    Wishlist wishlist;
}