package com.example.homeal;

import java.util.List;

public class Order {
    private String StoreName;
    private List<CartItem> CartItems;
    private double TotalPrice;
    private String Status;
    private String Date;
    private String Payment;

    public Order() {}

    public Order(String storeName, List<CartItem> cartItems, double totalPrice, String status, String date, String payment){
        this.StoreName = storeName;
        this.CartItems = cartItems;
        this.TotalPrice = totalPrice;
        this.Status = status;
        this.Date = date;
        this.Payment = payment;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public List<CartItem> getCartItems() {
        return CartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        CartItems = cartItems;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPayment() {
        return Payment;
    }

    public void setPayment(String payment) {
        Payment = payment;
    }
}
