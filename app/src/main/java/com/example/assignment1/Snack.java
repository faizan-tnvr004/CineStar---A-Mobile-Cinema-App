package com.example.assignment1;

public class Snack {
    private String name;
    private double price;
    private int imageResId;
    private int quantity;

    public Snack(String name, double price, int imageResId) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.quantity = 0;
    }

    public String getName()        { return name; }
    public double getPrice()       { return price; }
    public int getImageResId()     { return imageResId; }
    public int getQuantity()       { return quantity; }
    public void setQuantity(int q) { this.quantity = q; }
}