package com.hertz.model;

import com.hertz.utils.DatabaseConnection;
import org.bson.Document;

import java.util.EnumSet;

public class SuperAdmin extends Admin {
    private static SuperAdmin INSTANCE;

    private SuperAdmin(String username, String hashedPassword) {
        super(username, hashedPassword, null, EnumSet.allOf(Capability.class));
    }

    public static synchronized SuperAdmin getInstance() {
        if (INSTANCE == null) {
            loadFromDatabase();
        }
        return INSTANCE;
    }

    private static void loadFromDatabase() {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        Document superAdminDocument = databaseConnection.getDatabase()
                .getCollection("admins")
                .find(new Document("type", "SuperAdmin"))
                .first();

        if (superAdminDocument == null) {
            throw new IllegalStateException("SuperAdmin not found in the database.");
        }

        String username = superAdminDocument.getString("username");
        String hashedPassword = superAdminDocument.getString("hashedPassword");

        INSTANCE = new SuperAdmin(username, hashedPassword);
    }

    @Override
    public boolean canChangeAdminCapabilities(Admin targetAdmin) {
        return true; // SuperAdmin can change any admin's capabilities
    }
}