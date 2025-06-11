package com.hertz.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class DatabaseConnection {
    public static void main(String[] args) {
        // Replace with your connection string
        String uri = "mongodb://localhost:27017";
        MongoClientURI mongoClientURI = new MongoClientURI(uri);
        MongoClient mongoClient = new MongoClient(mongoClientURI);

        // Access the database
        MongoDatabase database = mongoClient.getDatabase("yourDatabaseName");

        System.out.println("Connected to the database successfully");

        // Close the connection
        mongoClient.close();
    }
}
