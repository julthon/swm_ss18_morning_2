package at.tugraz.recipro.recipro;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.plumillonforge.android.chipview.Chip;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import at.tugraz.recipro.adapters.RecipesAdapter;
import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.views.OurChipView;
import at.tugraz.recipro.views.OurChipViewAdapterImplementation;
import at.tugraz.recipro.views.OurTagImplementation;
import at.tugraz.recipro.ws.WSConnection;
import at.tugraz.recipro.ws.WSConstants;

public class RecipesFragment extends Fragment {
    public static final String FRAGMENT_TAG = "RecipesFragment";

    private ListView lvSearchResults;
    private EditText etMinTime;
    private EditText etMaxTime;
    private Spinner spRecipeType;
    private TableLayout tlFilters;
    private ImageButton ibFilters;
    private RatingBar rbMinRating;
    private OurChipView ocvTagView;

    private String lastQuery = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);

        tlFilters = view.findViewById(R.id.tlFilters);
        ibFilters = view.findViewById(R.id.ibFilters);
        etMinTime = view.findViewById(R.id.etMinTime);
        etMaxTime = view.findViewById(R.id.etMaxTime);
        rbMinRating = view.findViewById(R.id.rbMinRating);
        ocvTagView = view.findViewById(R.id.ocvTagView);

        final SearchView searchBar = view.findViewById(R.id.searchbar);
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        searchBar.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchBar.setSubmitButtonEnabled(true);
        searchBar.setIconifiedByDefault(false);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    view.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

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

        lvSearchResults = (ListView) view.findViewById(android.R.id.list);
        ArrayList<Recipe> recipes = new ArrayList<>();
        final RecipesAdapter recipesAdapter = new RecipesAdapter(getContext(), recipes);
        lvSearchResults.setAdapter(recipesAdapter);

        lvSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragmentDescription = new RecipeDescriptionFragment();

                Bundle arguments = new Bundle();
                arguments.putSerializable(getResources().getString(R.string.recipe), recipesAdapter.getItem(position));
                fragmentDescription.setArguments(arguments);
                getFragmentManager().beginTransaction()
                        .replace(R.id.flContent, fragmentDescription, "RecipeDescription")
                        .addToBackStack(null)
                        .commit();
            }
        });

        spRecipeType = (Spinner) view.findViewById(R.id.spRecipeType);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.recipe_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRecipeType.setAdapter(typeAdapter);

        // testing
        final OurChipView chipView = (OurChipView) view.findViewById(R.id.ocvTagView);
        chipView.setAdapter(new OurChipViewAdapterImplementation(getContext()));

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private void searchFor(final String query) {
        lastQuery = query;

        new AsyncTask<Void, Void, List<Recipe>>() {
            @Override
            protected List<Recipe> doInBackground(Void... voids) {
                MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
                String mintime = etMinTime.getText().toString();
                String maxtime = etMaxTime.getText().toString();
                String type = spRecipeType.getSelectedItem().toString().replace(" ", "_");
                String rating = Float.toString(rbMinRating.getRating());
                List<String> allergenes = ocvTagView.getListOfType(OurTagImplementation.TagType.ALLERGEN_EXCLUDE)
                        .stream()
                        .map(x -> x.getText())
                        .collect(Collectors.toList());

                queryParams.put(WSConstants.QUERY_TITLE, Arrays.asList(query));
                if(!mintime.isEmpty())
                    queryParams.put(WSConstants.QUERY_MIN_PREP, Arrays.asList(mintime));
                if(!maxtime.isEmpty())
                    queryParams.put(WSConstants.QUERY_MAX_PREP, Arrays.asList(maxtime));
                if(type != null && !type.isEmpty())
                    queryParams.put(WSConstants.QUERY_TYPES, Arrays.asList(type));
                if(!rating.isEmpty())
                    queryParams.put(WSConstants.QUERY_MIN_RATING, Arrays.asList(rating));
                if(!allergenes.isEmpty())
                    queryParams.put(WSConstants.QUERY_ALLERGENS, allergenes);

                try {
                    return WSConnection.getInstance().requestRecipes(queryParams);
                } catch (RestClientException ex) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),
                                    getResources().getString(R.string.error_connect),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    return new ArrayList<>();
                }
            }

            @Override
            protected void onPostExecute(List<Recipe> recipes) {
                addRecipes(recipes);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (lastQuery != null) {
            searchFor(lastQuery);
        }
    }

    public void addRecipes(List<Recipe> recipes) {
        RecipesAdapter adapter = (RecipesAdapter) lvSearchResults.getAdapter();
        adapter.clear();
        adapter.addAll(recipes);
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
