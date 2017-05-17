package com.cleveroad.loopbar.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.cleveroad.loopbar.R;
import com.cleveroad.loopbar.adapter.ICategoryItem;

import java.util.ArrayList;
import java.util.List;

public class MockedItemsFactory {

    private MockedItemsFactory() {}

    public static List<ICategoryItem> getCategoryItems(Context context) {
        List<ICategoryItem> items = new ArrayList<>();
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.enls_ic_local_taxi), "auto"));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.enls_ic_account_balance), "balance"));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.enls_ic_alarm), "alarm"));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.enls_vector_brush_white_24dp), "brush"));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.enls_vector_camera_alt_white_24dp), "camera"));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.enls_vector_landscape_white_24dp), "rock"));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.enls_vector_palette_white_24dp), "palette"));
        items.add(new CategoryItem(ContextCompat.getDrawable(context, R.drawable.enls_vector_moon_white_24dp), "moon"));
        return items;
    }
}
