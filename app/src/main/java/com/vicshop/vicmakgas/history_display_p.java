package com.vicshop.vicmakgas;

public class history_display_p {
    public String item_price, item_name, date_ordered, time_ordered, status, time_delivered, date_delivered, image_url;

    public history_display_p(){}

    public history_display_p(String date_delivered, String date_ordered, String status, String time_delivered,String time_ordered
                             ,String image_url, String item_name, String item_price) {
        this.image_url = image_url;
        this.item_price = item_price;
        this.item_name = item_name;
        this.date_ordered = date_ordered;
        this.time_ordered = time_ordered;
        this.status = status;
        this.time_delivered = time_delivered;
        this.date_delivered = date_delivered;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getDate_ordered() {
        return date_ordered;
    }

    public void setDate_ordered(String date_ordered) {
        this.date_ordered = date_ordered;
    }

    public String getTime_ordered() {
        return time_ordered;
    }

    public void setTime_ordered(String time_ordered) {
        this.time_ordered = time_ordered;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime_delivered() {
        return time_delivered;
    }

    public void setTime_delivered(String time_delivered) {
        this.time_delivered = time_delivered;
    }

    public String getDate_delivered() {
        return date_delivered;
    }

    public void setDate_delivered(String date_delivered) {
        this.date_delivered = date_delivered;
    }

}
