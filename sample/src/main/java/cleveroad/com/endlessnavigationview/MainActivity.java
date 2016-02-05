package cleveroad.com.endlessnavigationview;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cleveroad.com.lib.adapter.ICategoryItem;
import cleveroad.com.lib.adapter.IOperationItem;
import cleveroad.com.lib.adapter.SimpleCategoriesAdapter;
import cleveroad.com.lib.model.MockedItemsFactory;
import cleveroad.com.lib.widget.EndlessNavigationView;
import cleveroad.com.lib.widget.OnItemClickListener;

public class MainActivity extends AppCompatActivity implements OnItemClickListener<ICategoryItem>{

    private ViewPager viewPager;
    private EndlessNavigationView endlessNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        endlessNavigationView = (EndlessNavigationView) findViewById(R.id.endlessView);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                endlessNavigationView.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        List<ICategoryItem> items = MockedItemsFactory.getCategoryItems();

        SimpleCategoriesAdapter adapter = new SimpleCategoriesAdapter(items);
        adapter.addOnItemClickListener(this);
        endlessNavigationView.setCategoriesAdapter(adapter);

        viewPager.setAdapter(new ViewPagerAdapter(items));
    }

    private static class ViewPagerAdapter extends PagerAdapter {
        private List<ICategoryItem> items;

        public ViewPagerAdapter(List<ICategoryItem> items){
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView itemView = new TextView(container.getContext());
            ViewPager.LayoutParams params = new ViewPager.LayoutParams();
            params.width = ViewPager.LayoutParams.MATCH_PARENT;
            params.height = ViewPager.LayoutParams.MATCH_PARENT;
            itemView.setLayoutParams(params);
            itemView.setGravity(Gravity.CENTER);
            itemView.setText(items.get(position).toString());
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    public void onItemClicked(ICategoryItem item, int position) {
        viewPager.setCurrentItem(position);
    }
}
