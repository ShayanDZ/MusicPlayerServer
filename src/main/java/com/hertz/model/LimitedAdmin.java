package com.hertz.model;

import java.util.Set;

public class LimitedAdmin extends Admin {
    public LimitedAdmin(String username, String hashedPassword, Integer id, Set<Capability> capabilities) {
        super(username, hashedPassword, id, capabilities);
    }

    @Override
    public boolean canChangeAdminCapabilities(Admin targetAdmin) {
        // LimitedAdmin cannot change admin capabilities
        return false;
    }
}