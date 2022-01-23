package com.example.jcafe;

public class itemcount {
    String itemid;
    int count;

    public itemcount() {
    }

    public itemcount(String itemid, int count) {
        this.itemid = itemid;
        this.count = count;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
