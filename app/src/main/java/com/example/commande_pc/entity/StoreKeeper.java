package com.example.commande_pc.entity;

import com.example.commande_pc.database.SqliteDatabaseHelper;

import java.util.ArrayList;
import java.util.Date;

public class StoreKeeper extends User{
    private static long role_id;
    public StoreKeeper(String lastName, String firstName, String email, long id, String password, Date createdAt, Date updatedAt) {
        super(lastName, firstName, email, id, password, createdAt, updatedAt);
    }
    static {
        StoreKeeper.role_id = Role.findRoleByRoleName("storekeeper").getId();
    }

    @Override
    public Role getRole() {
        if(this.role != null) return this.role;
        this.role =  Role.findRoleById(StoreKeeper.role_id);
        return this.role;
    }
    @Override
    public long getRoleId() {
        return  StoreKeeper.role_id;
    }
    public ArrayList<Item> getItems() {
        SqliteDatabaseHelper dbHelper = new SqliteDatabaseHelper();
        ArrayList<Item> items = dbHelper.getItems();
        dbHelper.close();
        return items;
    }
    public long addItem(Item item) {
        if(item == null){
            return -1;
        }
        SqliteDatabaseHelper dbHelper = new SqliteDatabaseHelper();
        long returnValue = dbHelper.insertComponentToDatabase(item);
        dbHelper.close();
        return returnValue;
    }

    public boolean deleteItem(Item item) {
        SqliteDatabaseHelper dbHelper = new SqliteDatabaseHelper();
        int returnValue = dbHelper.deleteItem(item.getId());
        dbHelper.close();
        return returnValue != 0;
    }
    public boolean updateItem(Item item) {
        SqliteDatabaseHelper dbHelper = new SqliteDatabaseHelper();
        int returnValue = dbHelper.updateItem(item);
        dbHelper.close();
        return returnValue != 0;
    }
}

