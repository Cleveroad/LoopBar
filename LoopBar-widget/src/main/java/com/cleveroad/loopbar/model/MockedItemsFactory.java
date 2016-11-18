package com.cleveroad.loopbar.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.cleveroad.loopbar.R;
import com.cleveroad.loopbar.adapter.ICategoryItem;

import java.util.ArrayList;
import java.util.List;

public class MockedItemsFactory {

    public static List<ICategoryItem> getCategoryItems(Context context) {
        List<ICategoryItem> items = new ArrayList<>();
        // TODO: 17.11.16 just for debug - remove
        int count = 0;
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.enls_ic_local_taxi), "auto" + " " + count++));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.enls_ic_account_balance), "balance" + " " + count++));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.enls_ic_alarm), "alarm" + " " + count++));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.enls_vector_brush_white_24dp), "brush" + " " + count++));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.enls_vector_camera_alt_white_24dp), "camera" + " " + count++));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.enls_vector_landscape_white_24dp), "rock" + " " + count++));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.enls_vector_palette_white_24dp), "palette" + " " + count++));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.enls_vector_moon_white_24dp), "moon" + " " + count++));
        return items;
    }
}
