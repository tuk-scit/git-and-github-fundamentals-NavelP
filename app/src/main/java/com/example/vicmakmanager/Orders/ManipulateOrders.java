package com.example.vicmakmanager.Orders;

public interface ManipulateOrders {
    void DeliverOrder(int order_number, String phoneNumber, String date_time, String payment_shop,
                      String payment_status, String delivery_guy, String item_name, String deposit, String item_price);

    void confirmOrder(String phone);

}
