package com.example.commande_pc.entity;

import android.app.Activity;
import android.content.Context;

import com.example.commande_pc.Utils;

import java.util.Date;

abstract public class User {
    protected String lastName;
    protected String firstName;
    protected String email;
    protected long id;
    protected String password;
    protected Date createdAt;
    protected Date updatedAt;
    protected Role role;

    public User(String lastName, String firstName, String email, long id, String password, Date createdAt, Date updatedAt) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.id = id;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public abstract  Role getRole();
    public abstract long getRoleId();

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getCreatedAt() {
        return Utils.dateToString(createdAt);
    }

    public String getUpdatedAt() {
        return Utils.dateToString(updatedAt);
    }

    public float myWallet() {
        return 500.00f;
    }
    public static void logout(Activity activity){
        activity.getSharedPreferences(Utils.loginPreferenceName, Context.MODE_PRIVATE).edit().clear().apply();
    }
    public static User getLoggedUser(Activity activity){
        return null;
    }
}