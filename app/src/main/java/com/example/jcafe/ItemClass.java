package com.example.jcafe;

public class ItemClass {
    String itemId;
    String itemName;
    String itemImage;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    String itemPrice;

    public ItemClass() {
    }

    public ItemClass(String itemId, String itemName, String itemImage, String itemPrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemPrice = itemPrice;
    }
}
