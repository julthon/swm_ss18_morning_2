package at.tugraz.recipro.recipro;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import at.tugraz.recipro.helper.ResourceAccessHelper;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout dlDrawer;
    private FrameLayout flContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ResourceAccessHelper.setApp(this);
        setContentView(R.layout.activity_main);

        dlDrawer = findViewById(R.id.dlDrawer);

        Toolbar toolBar = findViewById(R.id.tbToolbar);
        setSupportActionBar(toolBar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayUseLogoEnabled(false);
        }

        NavigationView navigationView = findViewById(R.id.nvNavigation);
        navigationView.setNavigationItemSelectedListener(item -> {
            Log.i("RECIPES", "Open fragment: " + item.toString());

            FragmentManager fragmentManager = getSupportFragmentManager();

            Fragment fragment = null;
            String tag = "";
            if (item.getItemId() == R.id.navHome) {
                tag = RecipesFragment.FRAGMENT_TAG;
                fragment = fragmentManager.findFragmentByTag(RecipesFragment.FRAGMENT_TAG);

                if (fragment == null) {
                    fragment = new RecipesFragment();
                }
            } else if (item.getItemId() == R.id.navGroceryList) {
                tag = GroceryListFragment.FRAGMENT_TAG;
                fragment = fragmentManager.findFragmentByTag(GroceryListFragment.FRAGMENT_TAG);

                if (fragment == null) {
                    fragment = new GroceryListFragment();
                }
            }

            boolean ret = false;
            if (fragment != null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, fragment, tag)
                        .addToBackStack(null)
                        .commit();

                item.setChecked(true);
                setTitle(item.getTitle());
                ret = true;
            }

            dlDrawer.closeDrawers();

            return ret;
        });

        flContent = findViewById(R.id.flContent);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flContent, new RecipesFragment(), RecipesFragment.FRAGMENT_TAG)
                .commitNow();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dlDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
