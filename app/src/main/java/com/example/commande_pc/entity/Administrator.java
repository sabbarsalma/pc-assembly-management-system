package com.example.commande_pc.entity;

import com.example.commande_pc.MainActivity;
import com.example.commande_pc.database.SqliteDatabaseHelper;

import java.util.ArrayList;
import java.util.Date;

public class Administrator extends  User{
    private static long role_id;
    static {
        Administrator.role_id = Role.findRoleByRoleName("administrator").getId();
    }

    public Administrator(String lastName, String firstName, String email, long id, String password, Date createdAt, Date updatedAt) {
        super(lastName, firstName, email, id, password, createdAt, updatedAt);
    }

    @Override
    public Role getRole() {
        if(this.role != null) return this.role;
        this.role =  Role.findRoleById(Administrator.role_id);
        return this.role;
    }
    @Override
    public long getRoleId() {
        return  Administrator.role_id;
    }
    public long addRequester(Requester requester){
        //insertRequester
        SqliteDatabaseHelper dbHelper = new SqliteDatabaseHelper();
        long returnValue = dbHelper.insertRequester(requester);
        dbHelper.close();
        return returnValue;
    }
    public boolean deleteRequester(Requester requester){
        SqliteDatabaseHelper dbHelper = new SqliteDatabaseHelper();
        long returnValue = dbHelper.deleteRequester(requester.getId());
        dbHelper.close();
        return returnValue != 0;
    }
    public ArrayList<Requester> getRequesters(){
        SqliteDatabaseHelper dbHelper = new SqliteDatabaseHelper();
        ArrayList<Requester> requesters = dbHelper.getRequesters();
        dbHelper.close();
        return requesters;
    }
    public void emptyDatabase(){
        SqliteDatabaseHelper sqliteDatabaseHelper = new SqliteDatabaseHelper();
        sqliteDatabaseHelper.emptyDatabase();
        sqliteDatabaseHelper.close();
    }
}
