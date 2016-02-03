package cleveroad.com.lib.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cleveroad.com.lib.R;

import static cleveroad.com.lib.adapter.CategoriesAdapter.ICategoryItem;

public class MockedItemsFactory {
    public static List<ICategoryItem> getCategoryItems() {
        List<ICategoryItem> items = new ArrayList<>();
        items.add(new CategoryItem(R.drawable.ic_local_taxi, "auto1"));
        items.add(new CategoryItem(R.drawable.ic_account_balance, "balance1"));
        items.add(new CategoryItem(R.drawable.ic_alarm, "alarm1"));
        items.add(new CategoryItem(R.drawable.ic_local_taxi, "auto2"));
        items.add(new CategoryItem(R.drawable.ic_account_balance, "balance2"));
        items.add(new CategoryItem(R.drawable.ic_alarm, "alarm2"));
        items.add(new CategoryItem(R.drawable.ic_local_taxi, "auto3"));
        items.add(new CategoryItem(R.drawable.ic_account_balance, "balance3"));
        items.add(new CategoryItem(R.drawable.ic_alarm, "alarm3"));
        items.add(new CategoryItem(R.drawable.ic_local_taxi, "auto4"));
        items.add(new CategoryItem(R.drawable.ic_account_balance, "balance4"));
        items.add(new CategoryItem(R.drawable.ic_alarm, "alarm4"));
        return items;
    }

    public static LinkedList<ICategoryItem> getCategoryItemsUniq() {
        LinkedList<ICategoryItem> items = new LinkedList<>();
        items.add(new CategoryItem(R.drawable.ic_local_taxi, "auto"));
        items.add(new CategoryItem(R.drawable.ic_account_balance, "balance"));
        items.add(new CategoryItem(R.drawable.ic_alarm, "alarm"));
        items.add(new CategoryItem(android.R.drawable.ic_delete, "cross"));
        items.add(new CategoryItem(android.R.drawable.ic_menu_camera, "photo"));
        items.add(new CategoryItem(android.R.drawable.ic_search_category_default, "search"));
        items.add(new CategoryItem(android.R.drawable.ic_btn_speak_now, "micro"));
        items.add(new CategoryItem(android.R.drawable.ic_dialog_alert, "alert"));
        items.add(new CategoryItem(android.R.drawable.ic_dialog_email, "email"));
        return items;
    }
}
