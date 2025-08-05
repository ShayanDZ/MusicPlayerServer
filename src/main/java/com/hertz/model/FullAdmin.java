package com.hertz.model;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class FullAdmin extends Admin {
    private final Set<Integer> registeredAdmins = new HashSet<>();

    public FullAdmin(String username, String hashedPassword, Integer id) {
        super(username, hashedPassword, id, EnumSet.of(
                Capability.VIEW_SONGS, Capability.CHANGE_SONGS,
                Capability.VIEW_USERS, Capability.CHANGE_USERS,
                Capability.VIEW_ADMINS, Capability.CHANGE_ADMINS,
                Capability.CREATE_ADMINS
        ));
    }

    public void registerAdmin(Admin admin) {
        registeredAdmins.add(admin.getId());
    }

    @Override
    public boolean canChangeAdminCapabilities(Admin targetAdmin) {
        // FullAdmin can only change capabilities of admins it has registered
        return registeredAdmins.contains(targetAdmin);
    }
}