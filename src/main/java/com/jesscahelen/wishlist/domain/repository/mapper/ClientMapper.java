package com.jesscahelen.wishlist.domain.repository.mapper;

import com.jesscahelen.wishlist.domain.model.Client;
import com.jesscahelen.wishlist.domain.model.Product;
import com.jesscahelen.wishlist.domain.model.Wishlist;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ClientMapper {

    private static final String ID = "_id";
    private static final String WISHLIST = "wishlist";
    private static final String PRODUCTS = "products";
    private static final String WISHLIST_ID = "wishlist._id";

    public Client getClient(Document document) {

        Set<Product> products = Set.of();
        document.getEmbedded(List.of(WISHLIST,PRODUCTS), products);

        Wishlist wishlist = Wishlist.builder()
                .id(UUID.fromString(document.getString(WISHLIST_ID)))
                .products(products)
                .build();

        return Client.builder()
                .id(UUID.fromString(document.getString(ID)))
                .wishlist(wishlist)
                .build();
    }

    public Document getDocument(Client client) {
        List<Document> products = new ArrayList<>();

        client.getWishlist().getProducts().stream().forEach( product -> {
            Document productDocument = new Document(ID, product.getId());
            products.add(productDocument);
                });

        return new Document(ID, client.getId())
                .append(WISHLIST, new Document(ID, client.getWishlist().getId())
                        .append("products", products));
    }
}