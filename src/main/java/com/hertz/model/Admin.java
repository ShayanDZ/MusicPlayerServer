package com.hertz.model;

import java.util.Set;

public abstract class Admin {
    private final String username;
    private final String hashedPassword;
    private final Integer id; // ID for all admins except SuperAdmin
    private final Set<Capability> capabilities;

    protected Admin(String username, String hashedPassword, Integer id, Set<Capability> capabilities) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.id = id;
        this.capabilities = capabilities;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public Integer getId() {
        return id;
    }

    public Set<Capability> getCapabilities() {
        return capabilities;
    }

    public boolean hasCapability(Capability capability) {
        return capabilities.contains(capability);
    }

    public abstract boolean canChangeAdminCapabilities(Admin targetAdmin);
}