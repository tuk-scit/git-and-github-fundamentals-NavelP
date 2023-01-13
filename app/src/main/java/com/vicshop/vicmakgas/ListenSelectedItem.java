package com.vicshop.vicmakgas;

import android.widget.ImageView;
import android.widget.TextView;

public interface ListenSelectedItem {
    void ListenSelectedItem_CartIncrement(TextView item_name, TextView item_price, ImageView item_image, int add_remove);
    void addSimilarItem(TextView item_name, TextView item_price, ImageView item_image);
    void removeSimilarItem(int adapter_position, TextView item_name, TextView item_price, ImageView item_image);
}
