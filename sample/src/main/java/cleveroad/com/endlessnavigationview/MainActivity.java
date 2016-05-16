package cleveroad.com.endlessnavigationview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cleveroad.com.lib.adapter.ICategoryItem;
import cleveroad.com.lib.adapter.SimpleCategoriesAdapter;
import cleveroad.com.lib.model.MockedItemsFactory;
import cleveroad.com.lib.widget.EndlessNavigationView;
import cleveroad.com.lib.widget.OnItemClickListener;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    private static final String EXTRA_ORIENTATION = "orientation";
    private static final int ORIENTATION_VERTICAL = 0;
    private static final int ORIENTATION_HORIZONTAL = 1;

    private EndlessNavigationView endlessNavigationView;

    private SimpleCategoriesAdapter categoriesAdapter;
    private SimpleFragmentStatePagerAdapter pagerAdapter;

    private ViewPager viewPager;

    //args
    private int orientation;

    @Nullable
    private Toast toast;

    public static void start(Context context, int orientation) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.putExtra(EXTRA_ORIENTATION, orientation);
        context.startActivity(starter);
    }

    public void onBtnOrientationClicked(View btn) {
        int nextOrientation = orientation == ORIENTATION_VERTICAL ? ORIENTATION_HORIZONTAL : ORIENTATION_VERTICAL;
        start(this, nextOrientation);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orientation = getIntent().getIntExtra(EXTRA_ORIENTATION, ORIENTATION_HORIZONTAL);

        setContentView(orientation == ORIENTATION_VERTICAL ? R.layout.activity_main_vertical : R.layout.activity_main_horizontal);

        endlessNavigationView = (EndlessNavigationView) findViewById(R.id.endlessView);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        categoriesAdapter = new SimpleCategoriesAdapter(MockedItemsFactory.getCategoryItemsUniq());
        endlessNavigationView.setCategoriesAdapter(categoriesAdapter);
        endlessNavigationView.addOnItemClickListener(this);

        List<Fragment> list = new ArrayList<>(9);
        list.add(ColorFragment.newInstance(android.R.color.holo_red_dark));
        list.add(ColorFragment.newInstance(android.R.color.black));
        list.add(ColorFragment.newInstance(android.R.color.holo_blue_dark));
        list.add(ColorFragment.newInstance(android.R.color.holo_green_dark));
        list.add(ColorFragment.newInstance(android.R.color.holo_orange_dark));
        list.add(ColorFragment.newInstance(android.R.color.holo_blue_light));
        list.add(ColorFragment.newInstance(android.R.color.holo_green_light));
        list.add(ColorFragment.newInstance(android.R.color.holo_purple));
        list.add(ColorFragment.newInstance(android.R.color.white));

        pagerAdapter = new SimpleFragmentStatePagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new AbstractPageChangedListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                endlessNavigationView.setCurrentItem(position);
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        if (toast != null) {
            toast.cancel();
        }

        ICategoryItem categoryItem = categoriesAdapter.getItem(position);
        viewPager.setCurrentItem(position);

        toast = Toast.makeText(this, getString(R.string.mask_on_item_clicked, position, categoryItem.getCategoryName()), Toast.LENGTH_SHORT);
        toast.show();
    }
}
