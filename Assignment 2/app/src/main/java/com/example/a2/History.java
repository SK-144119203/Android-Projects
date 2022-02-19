package com.example.a2;

import java.io.Serializable;

public class History implements Serializable {
    public String name;
    public double totalPrice;
    public int quantity;
    public String purchaseDate;

    public History(String name, double price, int quantity, String purchaseDate) {
        this.name = name;
        this.totalPrice = price;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
