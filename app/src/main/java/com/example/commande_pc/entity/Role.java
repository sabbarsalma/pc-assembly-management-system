package com.example.commande_pc.entity;

import com.example.commande_pc.LoginActivity;
import com.example.commande_pc.database.MemoryDataBaseHelper;
import com.example.commande_pc.database.SqliteDatabaseHelper;

public class Role {
    private long id;
    private String name;

    public Role(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    public long getId() {
        return this.id;
    }

    public static Role findRoleById(long role_id) {
        //return MemoryDataBaseHelper.findRoleById(role_id);
        SqliteDatabaseHelper databaseConnection = new SqliteDatabaseHelper();
        Role role = databaseConnection.findRoleById(role_id);
        databaseConnection.close();
        return role;
    }
    public static Role findRoleByRoleName(String role_name) {
        //return MemoryDataBaseHelper.findRoleByRoleName(role_name);
        SqliteDatabaseHelper databaseConnection = new SqliteDatabaseHelper();
        Role role = databaseConnection.findRoleByRoleName(role_name);
        databaseConnection.close();
        return role;
    }
}

