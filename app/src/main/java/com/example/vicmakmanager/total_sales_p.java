package com.example.vicmakmanager;

public class total_sales_p {
    String item_name, item_commission;

    public total_sales_p(String item_name, String item_commission) {
        this.item_name = item_name;
        this.item_commission = item_commission;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_commission() {
        return item_commission;
    }

    public void setItem_commission(String item_commission) {
        this.item_commission = item_commission;
    }

}
