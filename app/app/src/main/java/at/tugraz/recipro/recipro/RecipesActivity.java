package at.tugraz.recipro.recipro;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.tugraz.recipro.Views.OurChipView;
import at.tugraz.recipro.Views.OurChipViewAdapterImplementation;
import at.tugraz.recipro.adapters.RecipesAdapter;
import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.helper.ResourceAccessHelper;
import at.tugraz.recipro.ws.WSConnection;

public class RecipesActivity extends AppCompatActivity {

    private ListView lvSearchResults;
    private EditText etMinTime;
    private EditText etMaxTime;
    private Spinner spRecipeType;
    private TableLayout tlFilters;
    private ImageButton ibFilters;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ResourceAccessHelper.setApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recipes);

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

        lvSearchResults = (ListView) findViewById(android.R.id.list);
        ArrayList<Recipe> recipies = new ArrayList<>();
        final RecipesAdapter recipesAdapter = new RecipesAdapter(this, recipies);
        lvSearchResults.setAdapter(recipesAdapter);

        lvSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RecipesActivity.this, RecipeDescriptionActivity.class);
                intent.putExtra(getResources().getString(R.string.recipe), recipesAdapter.getItem(position));
                startActivity(intent);
            }
        });

        spRecipeType = (Spinner) findViewById(R.id.spRecipeType);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.recipe_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRecipeType.setAdapter(typeAdapter);
        
        // testing
        final OurChipView chipView = (OurChipView) findViewById(R.id.chip_tag_view);
        chipView.setAdapter(new OurChipViewAdapterImplementation(this));
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
                String type = spRecipeType.getSelectedItem().toString().replace(" ", "_");

                queryParams.put(getResources().getString(R.string.request_title), query);
                if(!mintime.isEmpty())
                    queryParams.put(getResources().getString(R.string.min_prep), mintime);
                if(!maxtime.isEmpty())
                    queryParams.put(getResources().getString(R.string.max_prep), maxtime);
                if(type != null && !type.isEmpty())
                    queryParams.put(getResources().getString(R.string.filter_types), type);

                try {
                    return WSConnection.getInstance().requestRecipes(queryParams);
                } catch (RestClientException ex) {
                    RecipesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RecipesActivity.this,
                                    getResources().getString(R.string.error_connect),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    return new ArrayList<>();
                }
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
        adapter.add(new Recipe(1, "Recipe #1", 20, 5.0, recipeIngredients, ""));
        adapter.add(new Recipe(2, "Recipe #2", 40, 4.0, recipeIngredients, ""));
        adapter.add(new Recipe(3, "Recipe #3", 10, 1.0, recipeIngredients, ""));
        adapter.add(new Recipe(4, "Recipe #4", 30, 3.0, recipeIngredients, ""));
    }
}
