package com.example.app1;

public class Product {
    private String reference;
    private String description;
    private int price;
    private int typeref;

    public Product(){

    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTyperef() {
        return typeref;
    }

    public void setTyperef(int typeref) {
        this.typeref = typeref;
    }
}
