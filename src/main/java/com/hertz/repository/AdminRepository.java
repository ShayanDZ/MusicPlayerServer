package com.hertz.repository;

import com.hertz.model.*;
import com.hertz.utils.DatabaseConnection;
import com.mongodb.Block;
import org.bson.Document;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class AdminRepository {
    private static volatile AdminRepository instance;
    private final List<Admin> adminList = new ArrayList<>();

    private AdminRepository() {
        loadAdminData();
    }

    private void loadAdminData() {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        databaseConnection.getDatabase()
                .getCollection("admins")
                .find()
                .forEach((Block<? super Document>) (Document adminDocument) -> {
                    String username = adminDocument.getString("username");
                    String hashedPassword = adminDocument.getString("hashedPassword");
                    Integer id = adminDocument.getInteger("id");
                    String type = adminDocument.getString("type");
                    Set<Capability> capabilities = EnumSet.noneOf(Capability.class);
                    List<String> capabilityStrings = (List<String>) adminDocument.get("capabilities");
                    if (capabilityStrings != null) {
                        for (String capability : capabilityStrings) {
                            capabilities.add(Capability.valueOf(capability));
                        }
                    }

                    Admin admin = switch (type) {
                        case "SuperAdmin" -> SuperAdmin.getInstance();
                        case "FullAdmin" -> new FullAdmin(username, hashedPassword, id);
                        case "LimitedAdmin" -> new LimitedAdmin(username, hashedPassword, id, capabilities);
                        default -> throw new IllegalArgumentException("Unknown admin type: " + type);
                    };
                    adminList.add(admin);
                });

        // Ensure SuperAdmin is always present
        if (adminList.stream().noneMatch(admin -> admin instanceof SuperAdmin)) {
            adminList.add(SuperAdmin.getInstance());
        }
    }

    public static synchronized AdminRepository getInstance() {
        if (instance == null) {
            instance = new AdminRepository();
        }
        return instance;
    }

    public synchronized boolean addAdmin(Admin admin) {
        if (adminList.stream().anyMatch(existingAdmin -> existingAdmin.getId() != null && existingAdmin.getId().equals(admin.getId()))) {
            return false; // Admin with the same ID already exists
        }
        adminList.add(admin);

        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        Document adminDocument = new Document("username", admin.getUsername())
                .append("hashedPassword", admin.getHashedPassword())
                .append("id", admin.getId())
                .append("type", admin instanceof SuperAdmin ? "SuperAdmin" :
                        admin instanceof FullAdmin ? "FullAdmin" : "LimitedAdmin")
                .append("capabilities", admin.getCapabilities().stream().map(Enum::name).toList());
        databaseConnection.getDatabase().getCollection("admins").insertOne(adminDocument);

        return true;
    }

    public synchronized boolean updateAdmin(Admin admin) {
        try {
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            Document updatedAdminDocument = new Document("username", admin.getUsername())
                    .append("hashedPassword", admin.getHashedPassword())
                    .append("id", admin.getId())
                    .append("type", admin instanceof SuperAdmin ? "SuperAdmin" :
                            admin instanceof FullAdmin ? "FullAdmin" : "LimitedAdmin")
                    .append("capabilities", admin.getCapabilities().stream().map(Enum::name).toList());

            databaseConnection.getDatabase().getCollection("admins")
                    .updateOne(new Document("id", admin.getId()), new Document("$set", updatedAdminDocument));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized Admin findById(Integer id) {
        return adminList.stream()
                .filter(admin -> admin.getId() != null && admin.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public synchronized Admin findByUsername(String username) {
        return adminList.stream()
                .filter(admin -> admin.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    public synchronized List<Admin> getAllAdmins() {
        return new ArrayList<>(adminList);
    }

    public synchronized boolean removeAdmin(Admin admin) {
        boolean removed = adminList.removeIf(existingAdmin -> existingAdmin.getId() != null && existingAdmin.getId().equals(admin.getId()));
        if (removed) {
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            databaseConnection.getDatabase().getCollection("admins")
                    .deleteOne(new Document("id", admin.getId()));
        }
        return removed;
    }
}