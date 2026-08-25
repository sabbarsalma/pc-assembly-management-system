package com.example.commande_pc.entity;

import com.example.commande_pc.database.SqliteDatabaseHelper;

import java.util.ArrayList;
import java.util.Date;

public class Assembler extends User{
    private static long role_id;
    public Assembler(String lastName, String firstName, String email, long id, String password, Date createdAt, Date updatedAt) {
        super(lastName, firstName, email, id, password, createdAt, updatedAt);
    }
    static {
        Assembler.role_id = Role.findRoleByRoleName("assembler").getId();
    }

    @Override
    public Role getRole() {
        if(this.role != null) return this.role;
        this.role =  Role.findRoleById(Assembler.role_id);
        return this.role;
    }
    @Override
    public long getRoleId() {
        return  Assembler.role_id;
    }
    public ArrayList<Order> getAllOrders(){
        SqliteDatabaseHelper dbHelper = new SqliteDatabaseHelper();
        return dbHelper.getAllOrder();


    }
    public ArrayList<Item> getItems() {
        SqliteDatabaseHelper dbHelper = new SqliteDatabaseHelper();
        ArrayList<Item> items = dbHelper.getItems();
        dbHelper.close();
        return items;
    }
}
