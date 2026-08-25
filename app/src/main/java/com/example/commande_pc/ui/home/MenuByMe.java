package com.example.commande_pc.ui.home;

public class MenuByMe {


    private int type;
    private String text;

    public MenuByMe(int type, String text){
        this.type=type;
        this.text=text;

    }
    public int getType(){
        return type;
    }

    public String getText() {
        return text;
    }

}
