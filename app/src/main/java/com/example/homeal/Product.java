package com.example.homeal;

public class Product {
    private String name;
    private double price;
    private String description;
    private String id;
    private String storeId;

    public Product() {
    }


    public Product(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Product(String name, double price, String description, String id, String storeId)
    {
        this.name = name;
        this.price = price;
        this.description = description;
        this.id = id;
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

}
