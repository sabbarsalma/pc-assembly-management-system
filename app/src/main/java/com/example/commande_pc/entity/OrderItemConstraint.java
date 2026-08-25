package com.example.commande_pc.entity;

public class OrderItemConstraint {
    private int min;
    private int max;
    private boolean isRange = true;
    private AllowedCombinaison allowedCombinaison = AllowedCombinaison.ONLY_SAME;
    int position;
    public OrderItemConstraint(int min, int max) {
        this.min = min;
        this.max = max;
    }
    public OrderItemConstraint(int min, int max,boolean isRange) {
        this(min,max);
        this.isRange = isRange;
    }
    public OrderItemConstraint(int min, int max,boolean isRange,AllowedCombinaison allowedCombinaison) {
        this(min,max);
        this.isRange = isRange;
        this.allowedCombinaison = allowedCombinaison;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public boolean isRange() {
        return isRange;
    }
    public int getPosition(){
        return position;
    }

    public void setErrorPosition(int position) {
        this.position = position;
    }

    public AllowedCombinaison getAllowedCombinaison() {
        return allowedCombinaison;
    }
    public String getCombinaisonString(){
        switch (this.allowedCombinaison){
            case ONLY_SAME:
                return " identiques";
            case ONLY_DIFFRERENT:
                return " tous différents";
            case MIXT_ALLOWED:
                return " potentiellement différents";
            default:
                return null;
        }
    }
}
