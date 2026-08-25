package com.example.commande_pc.entity;

public class OrderItem {
    private long id;
    private long order_id;
    private long item_id;
    private int quantity;
    private Item item;

    public OrderItem(long item_id, int quantity) {
        this.item_id = item_id;
        this.quantity = quantity;
    }
    public long getItemId(){
        return this.item_id;
    }

    public int getQuantity() {
        return quantity;
    }
}
