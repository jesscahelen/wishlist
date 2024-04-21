package com.jesscahelen.wishlist.domain.repository;

import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.jesscahelen.wishlist.domain.model.Client;

public interface ClientRepository extends MongoRepository<Client, String> {

    @Query("{clientId:'?0'}")
    Client findItemById(UUID id);
}