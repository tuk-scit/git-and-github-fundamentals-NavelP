package com.vicshop.vicmakgas;

public class vicmak_display_p {
    String itemGroupName, image_url, selling_price;
    int image = 0;
    String availability = "1";

    public vicmak_display_p(String itemGroupName, String image_url, String selling_price) {
        this.itemGroupName = itemGroupName;
        this.image_url = image_url;
        this.selling_price = selling_price;

    }

    public vicmak_display_p(String itemGroupName,  int image, String selling_price) {
        this.itemGroupName = itemGroupName;
        this.image = image;
        this.selling_price = selling_price;
    }

    public vicmak_display_p(String itemGroupName, String image_url, String selling_price, String availability) {
        this.itemGroupName = itemGroupName;
        this.image_url = image_url;
        this.selling_price = selling_price;
        this.availability = availability;
    }

    public vicmak_display_p(String itemGroupName, int image, String selling_price, String availability) {
        this.itemGroupName = itemGroupName;
        this.selling_price = selling_price;
        this.image = image;
        this.availability = availability;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getItemGroupName() {
        return itemGroupName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setItemGroupName(String itemGroupName) {
        this.itemGroupName = itemGroupName;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }
}
