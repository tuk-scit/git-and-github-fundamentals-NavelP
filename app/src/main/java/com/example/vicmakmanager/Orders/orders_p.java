package com.example.vicmakmanager.Orders;

public class orders_p {

    String name, phoneNumber, item, location, date_time, item_price;

    public orders_p(String name, String phoneNumber, String item, String location, String date_time, String item_price) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.item = item;
        this.location = location;
        this.date_time = date_time;
        this.item_price = item_price;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
