package at.tugraz.recipro.recipro;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.tugraz.recipro.adapters.RecipesAdapter;
import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.data.RecipeIngredient;

public class MainActivity extends AppCompatActivity {

    private ListView lvSearchResults;
    private EditText etMinTime;
    private EditText etMaxTime;
    private TableLayout tlFilters;
    private ImageButton ibFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tlFilters = findViewById(R.id.tlFilters);
        ibFilters = findViewById(R.id.ibFilters);
        etMinTime = findViewById(R.id.etMinTime);
        etMaxTime = findViewById(R.id.etMaxTime);

        final SearchView searchBar = findViewById(R.id.searchbar);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchBar.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchBar.setSubmitButtonEnabled(true);
        searchBar.setIconifiedByDefault(false);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchFor(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }

        });

        ibFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tlFilters.setVisibility(tlFilters.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchFor(query);
        }

        lvSearchResults = (ListView) findViewById(android.R.id.list);
        ArrayList<Recipe> recipies = new ArrayList<Recipe>();
        final RecipesAdapter adapter = new RecipesAdapter(this, recipies);
        lvSearchResults.setAdapter(adapter);

        lvSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, RecipeDescriptionActivity.class);
                intent.putExtra("Recipe", adapter.getItem(position));

                startActivity(intent);
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private void searchFor(final String query) {
        final RecipesAdapter adapter = (RecipesAdapter) lvSearchResults.getAdapter();
        adapter.clear();

        new AsyncTask<Void, Void, List<Recipe>>() {
            @Override
            protected List<Recipe> doInBackground(Void... voids) {
                Map<String, String> queryParams = new HashMap<>();
                String mintime = etMinTime.getText().toString();
                String maxtime = etMaxTime.getText().toString();

                queryParams.put("title", query);
                if(!mintime.isEmpty())
                    queryParams.put("minpreptime", mintime);
                if(!maxtime.isEmpty())
                    queryParams.put("maxpreptime", maxtime);

                return WSConnection.sendQuery(queryParams);
            }

            @Override
            protected void onPostExecute(List<Recipe> recipes) {
                adapter.addAll(recipes);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public void fillWithTestData() {
        RecipesAdapter adapter = (RecipesAdapter) lvSearchResults.getAdapter();
        adapter.clear();
        ArrayList<RecipeIngredient> recipeIngredients = new ArrayList<>();
        adapter.add(new Recipe("Recipe #1", 20, 5.0, recipeIngredients, ""));
        adapter.add(new Recipe("Recipe #2", 40, 4.0, recipeIngredients, ""));
        adapter.add(new Recipe("Recipe #3", 10, 1.0, recipeIngredients, ""));
        adapter.add(new Recipe("Recipe #4", 30, 3.0, recipeIngredients, ""));
    }
}
