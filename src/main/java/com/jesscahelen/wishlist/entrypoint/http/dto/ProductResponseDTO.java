package com.jesscahelen.wishlist.entrypoint.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResponseDTO {

    String success;
    String failed;
    String message;
}