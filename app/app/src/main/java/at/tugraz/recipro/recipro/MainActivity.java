package at.tugraz.recipro.recipro;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import at.tugraz.recipro.data.Ingredient;
import org.springframework.web.client.RestClientException;

import java.util.*;
import java.util.concurrent.Executor;

import at.tugraz.recipro.adapters.RecipesAdapter;
import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.helper.ResourceAccessHelper;

public class MainActivity extends AppCompatActivity {

    private ListView lvSearchResults;
    private EditText etMinTime;
    private EditText etMaxTime;
    private Spinner spRecipeType;
    private TableLayout tlFilters;
    private ImageButton ibFilters;
    private SearchView searchExclude;
    private SearchView searchIngredient;

    private List<Ingredient> ingredients = Collections.emptyList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ResourceAccessHelper.setApp(this);

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

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchFor(query);
        }

        ibFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tlFilters.setVisibility(tlFilters.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });


        searchIngredient = findViewById(R.id.searchbarIngredientContains);
        initializeIngredientSearch(searchManager, searchIngredient);
        searchExclude = findViewById(R.id.searchbarIngredientExclude);
        initializeIngredientSearch(searchManager, searchExclude);

        initializeIngredients();


        lvSearchResults = (ListView) findViewById(android.R.id.list);
        ArrayList<Recipe> recipies = new ArrayList<>();
        final RecipesAdapter recipesAdapter = new RecipesAdapter(this, recipies);
        lvSearchResults.setAdapter(recipesAdapter);

        lvSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, RecipeDescriptionActivity.class);
                intent.putExtra(getResources().getString(R.string.recipe), recipesAdapter.getItem(position));
                startActivity(intent);
            }
        });


        spRecipeType = (Spinner) findViewById(R.id.spRecipeType);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.recipe_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRecipeType.setAdapter(typeAdapter);
        
        
    }

    private void initializeIngredientSearch(SearchManager searchManager, final SearchView searchIngredient) {
        searchIngredient.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchIngredient.setSubmitButtonEnabled(true);
        searchIngredient.setIconifiedByDefault(false);
        final String[] from = new String[] {"ingredientName"};
        final int[] to = new int[] {android.R.id.text1};
        final SimpleCursorAdapter ingredientAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        searchIngredient.setSuggestionsAdapter(ingredientAdapter);
        searchIngredient.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = searchIngredient.getSuggestionsAdapter().getCursor();
                cursor.moveToPosition(position);
                searchIngredient.setQuery(cursor.getString(cursor.getColumnIndex("ingredientName")), true);
                return true;
            }
        });
        searchIngredient.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                searchIngredient.setQuery("", false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                updateIngredientSuggestions(query, ingredientAdapter);
                return true;
            }
        });
        searchIngredient.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    return;
                updateIngredientSuggestions(searchIngredient.getQuery().toString(), ingredientAdapter);
            }
        });
    }

    private void updateIngredientSuggestions(String query, SimpleCursorAdapter ingredientAdapter) {
        MatrixCursor cursor = new MatrixCursor(new String[]{BaseColumns._ID, "ingredientName", "object"});
        query = query.toLowerCase();
        int id = 0;
        for (Ingredient ingredient : ingredients) {
            if(ingredient.getName().toLowerCase().contains(query))
                cursor.addRow(new Object[]{id++, ingredient.getName(), ingredient});
        }
        ingredientAdapter.changeCursor(cursor);
    }

    @SuppressLint("StaticFieldLeak")
    private void initializeIngredients() {
        /*new AsyncTask<Void, Void, List<Ingredient>>() {
            @Override
            protected List<Ingredient> doInBackground(Void... voids) {
                return WSConnection.getIngredients();
            }

            @Override
            protected void onPostExecute(List<Ingredient> ingredients) {
                MainActivity.this.ingredients = ingredients;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/
        ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("Menschenfleisch"));
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
                    return WSConnection.sendQuery(queryParams);
                } catch (RestClientException ex) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,
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
        adapter.add(new Recipe("Recipe #1", 20, 5.0, recipeIngredients, ""));
        adapter.add(new Recipe("Recipe #2", 40, 4.0, recipeIngredients, ""));
        adapter.add(new Recipe("Recipe #3", 10, 1.0, recipeIngredients, ""));
        adapter.add(new Recipe("Recipe #4", 30, 3.0, recipeIngredients, ""));
    }
}
