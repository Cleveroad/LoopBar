package com.cleveroad.sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cleveroad.loopbar.widget.Orientation;
import com.cleveroad.sample.fragments.CategoriesAdapterLoopBarFragment;

public class MainActivity extends AppCompatActivity implements IFragmentReplacer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            replaceFragment(CategoriesAdapterLoopBarFragment.newInstance(Orientation.ORIENTATION_VERTICAL_LEFT));
        }
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
