package com.example.commande_pc.entity;

import java.util.Date;

public class SofwareItem extends Item{
    public SofwareItem(long id, String subtype, String description, String string, int quantity, String comment, String image, Date createdAt, Date updatedAt) {
        super(id, "logiciel", subtype, description, quantity, comment, image, createdAt, updatedAt);
    }
    public SofwareItem(String subtype, String description, int quantity, String comment, String image) {
        super(-1, "logiciel", subtype, description, quantity, comment, image, null, null);
    }

}