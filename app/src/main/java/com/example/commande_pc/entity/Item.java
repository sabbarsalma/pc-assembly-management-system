package com.example.commande_pc.entity;

import java.util.Date;

public class Item {
    private String type;
    private String subtype;
    private String description;
    private int quantity;
    private String comment;
    private String image;
    private long id;
    private Date createdAt;
    private Date updatedAt;

    public Item( long id,String type,String subtype ,String description,int quantity, String comment, String image,Date createdAt,Date updatedAt) {
        this.id = id;
        this.type = type;
        this.subtype = subtype;
        this.description = description;
        this.quantity = quantity;
        this.comment = comment;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getType() {
        return type;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getComment() {
        return comment;
    }

    public String getImage() {
        return image;
    }

    public long getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
