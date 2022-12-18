package com.example.vicmakmanager;

import java.util.List;

public class vicmak_commodity_p {
    String item_name, total_count;
    int image;

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getTotal_count() {
        return total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public vicmak_commodity_p(String item_name, String total_count, int image) {
        this.item_name = item_name;
        this.total_count = total_count;
        this.image = image;
    }
}
