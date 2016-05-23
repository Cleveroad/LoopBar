package com.cleveroad.loopbar.model;

import java.util.ArrayList;
import java.util.List;

import com.cleveroad.loopbar.R;

import com.cleveroad.loopbar.adapter.ICategoryItem;

public class MockedItemsFactory {
    public static List<ICategoryItem> getCategoryItems() {
        List<ICategoryItem> items = new ArrayList<>();
        items.add(new CategoryItem(R.drawable.enls_ic_local_taxi, "item1"));
        items.add(new CategoryItem(R.drawable.enls_ic_account_balance, "item2"));
        items.add(new CategoryItem(R.drawable.enls_ic_alarm, "item3"));
        items.add(new CategoryItem(R.drawable.enls_ic_local_taxi, "item4"));
        items.add(new CategoryItem(R.drawable.enls_ic_account_balance, "item5"));
        return items;
    }

    public static List<ICategoryItem> getCategoryItemsUniq() {
        List<ICategoryItem> items = new ArrayList<>();
        items.add(new CategoryItem(R.drawable.enls_ic_local_taxi, "auto"));
        items.add(new CategoryItem(R.drawable.enls_ic_account_balance, "balance"));
        items.add(new CategoryItem(R.drawable.enls_ic_alarm, "alarm"));
        items.add(new CategoryItem(R.drawable.enls_vector_brush_white_24dp, "brush"));
        items.add(new CategoryItem(R.drawable.enls_vector_camera_alt_white_24dp, "camera"));
        items.add(new CategoryItem(R.drawable.enls_vector_landscape_white_24dp, "rock"));
        items.add(new CategoryItem(R.drawable.enls_vector_palette_white_24dp, "palette"));
        items.add(new CategoryItem(R.drawable.enls_vector_moon_white_24dp, "moon"));
        return items;
    }
}
