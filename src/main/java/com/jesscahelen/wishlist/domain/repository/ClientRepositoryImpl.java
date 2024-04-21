package com.jesscahelen.wishlist.domain.repository;

import com.jesscahelen.wishlist.config.mongodb.MongoDBProperties;
import com.jesscahelen.wishlist.domain.ClientRepository;
import com.jesscahelen.wishlist.domain.model.Client;
import com.jesscahelen.wishlist.domain.model.Product;
import com.jesscahelen.wishlist.domain.repository.mapper.ClientMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private final MongoClient mongoClient;
    private final ClientMapper clientMapper;
    private MongoDBProperties mongoDBProperties;
    private static final Logger LOGGER = LogManager.getLogger(ClientRepositoryImpl.class);
    private static final String CLIENTS = "clients";
    private static final String ID = "_id";
    private static final String PRODUCTS = "wishlist.products";
    private static final String PRODUCT_ID = "wishlist.products._id";
    private static final String PUSH = "$push";
    private static final String PULL = "$pull";

    public ClientRepositoryImpl(MongoClient mongoClient, ClientMapper clientMapper) {
        this.mongoClient = mongoClient;
        this.clientMapper = clientMapper;
    }

    @Override
    public void save(Document client) {
        try {
            MongoCollection<Document> collection = getClientCollection();
            collection.insertOne(client);
            LOGGER.info(String.format("Client id: {} saved.", client.get(ID)));
        } catch (Exception exception) {
            LOGGER.error(String.format("Problem occur when saving Client id: {}.", client.get(ID)), exception);
        }
    }

    @Override
    public void addProductToWishlist(UUID clientId, UUID productId) {
        try {
            MongoCollection<Document> collection = getClientCollection();

            Document filter = new Document(ID, clientId.toString());
            Document updateFields = new Document(PRODUCTS, new Document(ID, productId));
            Document updateOperation = new Document(PUSH, updateFields);

            collection.updateOne(filter, updateOperation);
            LOGGER.info(String.format("Product id: {} added to Client id: {}", productId, clientId));
        } catch (Exception exception) {
            LOGGER.error(String.format("Problem occur when adding product to Client id: {}.", clientId), exception);
        }
    }

    @Override
    public UUID removeProductFromWishlist(UUID clientId, UUID productId) {
        try {
            MongoCollection<Document> collection = getClientCollection();

            Document filter = new Document(ID, clientId);
            Document updateFields = new Document(PRODUCTS, new Document(ID, productId));
            Document updateOperation = new Document(PULL, updateFields);

            collection.updateOne(filter, updateOperation);
            LOGGER.info(String.format("Product id: {} removed from Client id: {}", productId, clientId));
            return productId;
        } catch (Exception exception) {
            LOGGER.error(String.format("Problem occur when removing product from Client id: {}.", clientId), exception);
            return null;
        }
    }

    @Override
    public Set<Product> findWishlistProductsByClientId(UUID clientId) {
        try {
            MongoCollection<Document> collection = getClientCollection();

            Document filter = new Document(ID, clientId.toString());
            Document foundDocument = collection.find(filter).first();

            if (Objects.isNull(foundDocument)) {
                return Set.of();
            }

            Client client = clientMapper.getClient(foundDocument);
            return client.getWishlist().getProducts();
        } catch (Exception exception) {
            LOGGER.error(String.format("Problem occur when retrieving products from Client's wishlist. Client id: {}.", clientId), exception);
            return Set.of();
        }
    }

    @Override
    public boolean isProductInClientWishlist(UUID clientId, UUID productId) {
        try {
            MongoCollection<Document> collection = getClientCollection();

            Document filter = new Document(ID, clientId)
                    .append(PRODUCT_ID, productId);

            Document result = collection.find(filter).first();
            return Objects.nonNull(result);
        } catch (Exception exception) {
            LOGGER.error(String.format("Problem occur when retrieving products from Client's wishlist. Client id: {}.", clientId), exception);
            return false;
        }
    }

    private MongoCollection<Document> getClientCollection() {
        return mongoClient.getDatabase(mongoDBProperties.getDatabase()).getCollection(CLIENTS);
    }
}