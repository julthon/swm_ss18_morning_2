package at.tugraz.recipro.recipro;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import at.tugraz.recipro.adapters.RecipesAdapter;
import at.tugraz.recipro.data.Allergen;
import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.helper.FavoritesHelper;
import at.tugraz.recipro.helper.RecipeUtils;
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
    private AutoCompleteTextView atIngredientExclude;
    private AutoCompleteTextView atIngredientInclude;
    private CheckBox cbFavorites;

    private String lastQuery = null;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();

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
        atIngredientExclude = view.findViewById(R.id.atIngredientExclude);
        atIngredientInclude = view.findViewById(R.id.atIngredientInclude);
        cbFavorites = view.findViewById(R.id.cbFavorites);

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
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                searchFor(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchFor(s);
                return true;
            }
        });

        ibFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tlFilters.setVisibility(tlFilters.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchFor(searchBar.getQuery().toString());
            }
        };

        etMaxTime.addTextChangedListener(textWatcher);
        etMinTime.addTextChangedListener(textWatcher);

        cbFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFor(searchBar.getQuery().toString());
            }
        });

        rbMinRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                searchFor(searchBar.getQuery().toString());
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
        spRecipeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchFor(searchBar.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // testing
        final OurChipView chipView = (OurChipView) view.findViewById(R.id.ocvTagView);
        chipView.setAdapter(new OurChipViewAdapterImplementation(getContext()));

        initializeIngredients();
        initializeIngredientsFilter();
        return view;
    }

    private void initializeIngredientsFilter() {
        atIngredientExclude.setThreshold(1);
        atIngredientInclude.setThreshold(1);
        ArrayAdapter<Ingredient> adapter = new ArrayAdapter<Ingredient>(Objects.requireNonNull(getContext()), android.R.layout.simple_dropdown_item_1line, ingredients);
        atIngredientExclude.setAdapter(adapter);
        atIngredientInclude.setAdapter(adapter);

        atIngredientExclude.setOnItemClickListener((parent, view, position, id) -> handleIngredientSelected(position, atIngredientExclude, OurTagImplementation.TagType.INGREDIENT_EXCLUDE));
        atIngredientInclude.setOnItemClickListener((parent, view, position, id) -> handleIngredientSelected(position, atIngredientInclude, OurTagImplementation.TagType.INGREDIENT_INCLUDE));

        ocvTagView.setOnChipClickListener(chip -> {
            ocvTagView.remove(chip);
            ocvTagView.notifyOnSomethingChangedListeners();
        });

        ocvTagView.addOnSomethingChangedListener(() -> {
            List<OurTagImplementation<Ingredient>> ingredientChips = ocvTagView.getListOfType(OurTagImplementation.TagType.INGREDIENT_EXCLUDE);
            ingredientChips.addAll(ocvTagView.getListOfType(OurTagImplementation.TagType.INGREDIENT_INCLUDE));
            List<Ingredient> ingredientSuggestions = ingredients.stream().filter(i -> ingredientChips.stream().noneMatch(e -> e.getText().equals(i.getName()))).collect(Collectors.toList());
            adapter.clear();
            adapter.addAll(ingredientSuggestions);
            searchFor(lastQuery);
        });
    }

    private void handleIngredientSelected(int position, AutoCompleteTextView atIngredient, OurTagImplementation.TagType type) {
        Ingredient ingredient = (Ingredient) atIngredient.getAdapter().getItem(position);
        ocvTagView.add(new OurTagImplementation(ingredient, ingredient.getName(), type));
        atIngredient.setText("");
        searchFor(lastQuery);
    }

    @SuppressLint("StaticFieldLeak")
    private void initializeIngredients() {
        new AsyncTask<Void, Void, List<Ingredient>>() {
            @Override
            protected List<Ingredient> doInBackground(Void... voids) {
                return WSConnection.getInstance().requestIngredients();
            }

            @Override
            protected void onPostExecute(List<Ingredient> ingredients) {
                RecipesFragment.this.ingredients.clear();
                RecipesFragment.this.ingredients.addAll(ingredients.stream().distinct().collect(Collectors.toList()));
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                List<OurTagImplementation<Allergen>> allergensTags = ocvTagView.getListOfType(OurTagImplementation.TagType.ALLERGEN_EXCLUDE);
                List<String> allergens = allergensTags
                        .stream()
                        .map(x -> x.getValue().getShortName())
                        .collect(Collectors.toList());

                List<String> ingredientsExclude = ocvTagView.getListOfType(OurTagImplementation.TagType.INGREDIENT_EXCLUDE).stream().map(x -> x.getText()).collect(Collectors.toList());
                List<String> ingredientsInclude = ocvTagView.getListOfType(OurTagImplementation.TagType.INGREDIENT_INCLUDE).stream().map(x -> x.getText()).collect(Collectors.toList());

                queryParams.put(WSConstants.QUERY_TITLE, Arrays.asList(query));
                if (!mintime.isEmpty())
                    queryParams.put(WSConstants.QUERY_MIN_PREP, Arrays.asList(mintime));
                if (!maxtime.isEmpty())
                    queryParams.put(WSConstants.QUERY_MAX_PREP, Arrays.asList(maxtime));
                if (type != null && !type.isEmpty())
                    queryParams.put(WSConstants.QUERY_TYPES, Arrays.asList(type));
                if (!rating.isEmpty())
                    queryParams.put(WSConstants.QUERY_MIN_RATING, Arrays.asList(rating));
                if (!allergens.isEmpty())
                    queryParams.put(WSConstants.QUERY_ALLERGENS, allergens);
                if (!ingredientsExclude.isEmpty())
                    queryParams.put(WSConstants.QUERY_INGREDIENT_EXCLUDE, ingredientsExclude);
                if (!ingredientsInclude.isEmpty())
                    queryParams.put(WSConstants.QUERY_INGREDIENT_INCLUDE, ingredientsInclude);

                try {
                    if (queryParams.isEmpty()) {
                        return null;
                    }
                    List<Recipe> recipes = WSConnection.getInstance().requestRecipes(queryParams);

                    if (cbFavorites.isChecked())
                        recipes = RecipeUtils.filterByFavorites(recipes, new FavoritesHelper(getActivity()).getFavorites());

                    return recipes;

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
                if (recipes != null)
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
        adapter.add(new Recipe(1, "Recipe #1", 20, 4, 5.0, recipeIngredients, ""));
        adapter.add(new Recipe(2, "Recipe #2", 40, 4, 4.0, recipeIngredients, ""));
        adapter.add(new Recipe(3, "Recipe #3", 10, 4, 1.0, recipeIngredients, ""));
        adapter.add(new Recipe(4, "Recipe #4", 30, 4, 3.0, recipeIngredients, ""));
    }
}
