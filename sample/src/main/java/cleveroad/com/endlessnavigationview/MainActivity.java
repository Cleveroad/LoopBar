package cleveroad.com.endlessnavigationview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import cleveroad.com.lib.adapter.SimpleCategoriesAdapter;
import cleveroad.com.lib.model.MockedItemsFactory;
import cleveroad.com.lib.widget.EndlessNavigationView;
import cleveroad.com.lib.widget.OnItemClickListener;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private EndlessNavigationView endlessNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        endlessNavigationView = (EndlessNavigationView) findViewById(R.id.endlessView);

        SimpleCategoriesAdapter adapter = new SimpleCategoriesAdapter(MockedItemsFactory.getCategoryItems());
        endlessNavigationView.setCategoriesAdapter(adapter);
        endlessNavigationView.addOnItemClickListener(this);
    }

    @Override
    public void onItemClicked(int position) {
        Toast.makeText(this, getString(R.string.mask_on_item_clicked, position), Toast.LENGTH_SHORT).show();
    }
}
