package com.vicshop.vicmakgas;

import android.graphics.drawable.Drawable;

public class view_display_p {
    String itemGroupName, item_price;
    Drawable image;

    public view_display_p(String itemGroupName, String item_price, Drawable image) {
        this.itemGroupName = itemGroupName;
        this.item_price = item_price;
        this.image = image;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public Drawable getImage() {
        return image;
    }

    public String getItemGroupName() {
        return itemGroupName;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public void setItemGroupName(String itemGroupName) {
        this.itemGroupName = itemGroupName;
    }
}
