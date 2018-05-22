package at.tugraz.recipro.recipro;

import android.support.annotation.NonNull;
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
import android.widget.FrameLayout;

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
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = findViewById(R.id.nvNavigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.i("RECIPES", "Open fragment: " + item.toString());

                Fragment fragment = null;
                Class fragmentClass;
                switch(item.getItemId()) {
                    case R.id.navHome:
                        fragmentClass = RecipesFragment.class;
                    default:
                        fragmentClass = RecipesFragment.class;
                }

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                item.setChecked(true);
                setTitle(item.getTitle());
                dlDrawer.closeDrawers();

                return true;
            }
        });

        flContent = findViewById(R.id.flContent);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flContent, new RecipesFragment(), "RecipesFragment")
                .addToBackStack(null)
                .commit();
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
