package com.example.commande_pc.entity;

import java.util.Date;

public class MaterielItem extends Item{
    public MaterielItem(long id, String subtype, String description, String string, int quantity, String comment, String image, Date createdAt, Date updatedAt) {
        super(id, "materiel", subtype, description, quantity, comment, image, createdAt, updatedAt);
    }
    public MaterielItem(String subtype, String description, int quantity, String comment, String image) {
        super(-1, "materiel", subtype, description, quantity, comment, image, null, null);
    }
}