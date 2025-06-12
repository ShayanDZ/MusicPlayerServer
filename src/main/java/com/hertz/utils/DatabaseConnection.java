package com.hertz.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private MongoDatabase database;
    private MongoClient mongoClient;
    private MongoClientURI mongoClientURI;
    public static String uri = "mongodb://localhost:27017";
    public static String dataBaseName = "MusicApp";

    private DatabaseConnection() {
        mongoClientURI = new MongoClientURI(uri);
        mongoClient = new MongoClient(mongoClientURI);
        database = mongoClient.getDatabase(dataBaseName);
        System.out.println("Connected to the database successfully");
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoClientURI getMongoClientURI() {
        return mongoClientURI;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Database connection closed");
        }
    }

    public boolean isConnected() {
        return mongoClient != null && mongoClient.getAddress() != null;
    }
}
