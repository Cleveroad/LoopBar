package cleveroad.com.endlessnavigationview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import cleveroad.com.lib.adapter.IOperationItem;
import cleveroad.com.lib.adapter.SimpleCategoriesAdapter;
import cleveroad.com.lib.model.MockedItemsFactory;
import cleveroad.com.lib.widget.EndlessNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EndlessNavigationView endlessNavigationView = (EndlessNavigationView) findViewById(R.id.endlessView);
        endlessNavigationView.setCategoriesAdapter(new SimpleCategoriesAdapter(new ArrayList<IOperationItem>(MockedItemsFactory.getCategoryItems())));
    }
}
