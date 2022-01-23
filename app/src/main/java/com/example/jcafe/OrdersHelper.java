package com.example.jcafe;

import java.util.ArrayList;

public class OrdersHelper {
    String orderID,cname,snum,ctime,status;

    public String getStatus() {
        return status;
    }

    public OrdersHelper(String orderID, String cname, String snum, String ctime, String status, ArrayList<itemcount> itemcountArrayList) {
        this.orderID = orderID;
        this.cname = cname;
        this.snum = snum;
        this.ctime = ctime;
        this.status = status;
        this.itemcountArrayList = itemcountArrayList;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    ArrayList<itemcount> itemcountArrayList;

    public OrdersHelper() {
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getSnum() {
        return snum;
    }

    public void setSnum(String snum) {
        this.snum = snum;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public ArrayList<itemcount> getItemcountArrayList() {
        return itemcountArrayList;
    }

    public void setItemcountArrayList(ArrayList<itemcount> itemcountArrayList) {
        this.itemcountArrayList = itemcountArrayList;
    }
}
