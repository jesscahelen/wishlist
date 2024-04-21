package com.jesscahelen.wishlist.config.mongodb;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class MongoDBProperties {

    private static final String MONGO_DB = "mongodb://";

    @Value("mongodb.host")
    private String host;

    @Value("mongodb.port")
    private int port;

    @Value("mongodb.database")
    private String database;

    @Value("mongodb.username")
    private String username;

    @Value("mongodb.password")
    private String password;

    public String getUriConnection() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MONGO_DB);
        stringBuilder.append(username);
        stringBuilder.append(":");
        stringBuilder.append(password);
        stringBuilder.append("@");
        stringBuilder.append(host);
        stringBuilder.append(":");
        stringBuilder.append(port);
        stringBuilder.append("/");
        stringBuilder.append(database);

        return stringBuilder.toString();
    }
}