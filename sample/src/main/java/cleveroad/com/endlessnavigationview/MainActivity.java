package cleveroad.com.endlessnavigationview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import cleveroad.com.lib.adapter.ICategoryItem;
import cleveroad.com.lib.adapter.IOperationItem;
import cleveroad.com.lib.adapter.SimpleCategoriesAdapter;
import cleveroad.com.lib.model.MockedItemsFactory;
import cleveroad.com.lib.widget.EndlessNavigationView;
import cleveroad.com.lib.widget.OnItemClickListener;

public class MainActivity extends AppCompatActivity implements OnItemClickListener<ICategoryItem>{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EndlessNavigationView endlessNavigationView = (EndlessNavigationView) findViewById(R.id.endlessView);

        SimpleCategoriesAdapter adapter = new SimpleCategoriesAdapter(MockedItemsFactory.getCategoryItems());
        adapter.addOnItemClickListener(this);
        endlessNavigationView.setCategoriesAdapter(adapter);
    }

    @Override
    public void onItemClicked(ICategoryItem item, int position) {

    }
}
