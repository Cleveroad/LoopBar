package com.cleveroad.loopbar.adapter;

import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.cleveroad.loopbar.model.CategoryItem;

import java.util.ArrayList;
import java.util.List;

public class SimpleCategoriesMenuAdapter extends SimpleCategoriesAdapter {

    public SimpleCategoriesMenuAdapter(@NonNull Menu menu) {
        super(convertMenuToCategoriesList(menu));
    }

    @NonNull
    private static List<ICategoryItem> convertMenuToCategoriesList(@NonNull Menu menu) {
        List<ICategoryItem> result = new ArrayList<>(menu.size());

        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem menuItem = menu.getItem(i);
            result.add(new CategoryItem(menuItem.getIcon(), String.valueOf(menuItem.getTitle())));
        }

        return result;
    }
}
