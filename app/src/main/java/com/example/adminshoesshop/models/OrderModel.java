package com.example.adminshoesshop.models;

import java.util.ArrayList;

public class OrderModel {
    String userId;
    String id, address;
    ArrayList<OrderProductModel> cartList;
    String dateOrder;
    long status;
    String timeOrder, totalAmount;

    public OrderModel() {
    }

    public OrderModel(String userId, String id, String address, ArrayList<OrderProductModel> cartList, String dateOrder, long status, String timeOrder, String totalAmount) {
        this.userId = userId;
        this.id = id;
        this.address = address;
        this.cartList = cartList;
        this.dateOrder = dateOrder;
        this.status = status;
        this.timeOrder = timeOrder;
        this.totalAmount = totalAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<OrderProductModel> getCartList() {
        return cartList;
    }

    public void setCartList(ArrayList<OrderProductModel> cartList) {
        this.cartList = cartList;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getTimeOrder() {
        return timeOrder;
    }

    public void setTimeOrder(String timeOrder) {
        this.timeOrder = timeOrder;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
