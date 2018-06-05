package at.tugraz.recipro.recipro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.web.client.RestClientException;

import java.util.HashSet;
import java.util.List;

import at.tugraz.recipro.adapters.IngredientsAdapter;
import at.tugraz.recipro.data.Allergen;
import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.helper.FavoritesHelper;
import at.tugraz.recipro.helper.GroceryListHelper;
import at.tugraz.recipro.views.OurChipView;
import at.tugraz.recipro.views.OurChipViewAdapterImplementation;
import at.tugraz.recipro.views.OurTagImplementation;
import at.tugraz.recipro.ws.WSConnection;

public class RecipeDescriptionFragment extends Fragment {
    public static final String FRAGMENT_TAG = "RecipeDescriptionFragment";

    TextView tvDescTitle;
    ImageView ivDescImage;
    TextView tvDescTime;
    RatingBar rbDescRating;
    ListView lvIngredients;
    TextView tvDescription;
    EditText etServings;
    ImageButton ibFavourites;
    ImageButton ibGrocery;
    OurChipView ocvAllergens;

    Recipe recipe;

    GroceryListHelper dbHelper;
    FavoritesHelper fHelper;

    int currentServings;

    @SuppressLint("StaticFieldLeak")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        view = inflater.inflate(R.layout.fragment_recipe_description, container, false);

        this.dbHelper = new GroceryListHelper(this.getContext());
        this.fHelper = new FavoritesHelper(this.getContext());
        this.tvDescTitle = view.findViewById(R.id.tvDescTitle);
        this.ivDescImage = view.findViewById(R.id.ivDescImage);
        this.tvDescTime = view.findViewById(R.id.tvDescTime);
        this.rbDescRating = view.findViewById(R.id.rbDescRating);
        this.lvIngredients = view.findViewById(R.id.lvIngredients);
        this.tvDescription = view.findViewById(R.id.tvDescription);
        this.etServings = view.findViewById(R.id.etServings);
        this.ibFavourites = view.findViewById(R.id.ibFavourite);
        this.ocvAllergens = view.findViewById(R.id.ocvAllergens);
        this.ibGrocery = view.findViewById(R.id.ibGroceryList);

        if (fHelper.exists(this.getId())){
            this.ibFavourites.setBackgroundResource(R.drawable.ic_star_yellow_24dp);
            this.ibFavourites.setTag(R.drawable.ic_star_yellow_24dp);
        } else {
            this.ibFavourites.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
            this.ibFavourites.setTag(R.drawable.ic_star_border_black_24dp);
        }

        Bundle arguments = getArguments();
        if (arguments != null) {
            this.recipe = (Recipe) arguments.get(getResources().getString(R.string.recipe));
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStackImmediate();
            return view;
        }

        this.currentServings = this.recipe.getServings();

        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(getContext(), this.recipe.getIngredients());
        this.lvIngredients.setAdapter(ingredientsAdapter);

        this.tvDescTitle.setText(this.recipe.getTitle());
        this.tvDescTime.setText(String.valueOf(this.recipe.getTime()) + getResources().getString(R.string.minutes));
        this.rbDescRating.setRating(((float) this.recipe.getRating()));
        this.tvDescription.setText(this.recipe.getDescription());
        this.etServings.setText(Integer.toString(this.currentServings));


        etServings.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView tvServings, int i, KeyEvent keyEvent) {
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    view.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                if (tvServings.getText().toString().isEmpty()) {
                    tvServings.setText(Integer.toString(currentServings));
                    return true;
                }

                int servings = Integer.parseInt(tvServings.getText().toString());

                if (servings <= 0 || servings > 1000) {
                    tvServings.setText(Integer.toString(currentServings));
                    return true;
                }

                float factor = (float)servings / (float)currentServings;

                List<RecipeIngredient> ingredients = recipe.getIngredients();
                for (RecipeIngredient ingredient : ingredients) {
                    ingredient.setQuantity(ingredient.getQuantity() * factor);
                }

                IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(getContext(), ingredients);
                lvIngredients.setAdapter(ingredientsAdapter);

                currentServings = servings;

                return true;
            }
        });

        if (fHelper.exists(recipe.getId())){
            ibFavourites.setBackgroundResource(R.drawable.ic_star_yellow_24dp);
            ibFavourites.setTag(R.drawable.ic_star_yellow_24dp);
        } else {
            ibFavourites.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
            ibFavourites.setTag(R.drawable.ic_star_border_black_24dp);
        }


        this.ocvAllergens.setAdapter(new OurChipViewAdapterImplementation(getContext()));
        this.ocvAllergens.setOnChipClickListener(chip -> {
            Allergen allergen = ((OurTagImplementation<Allergen>) chip).getValue();

            showPopup(String.format("%s (%s)", allergen.getName(),
                    allergen.getShortName()),
                    allergen.getDescription());
        });

        HashSet<Allergen> allergens = new HashSet<>();
        this.recipe.getIngredients()
                .stream()
                .map(x -> x.getIngredient())
                .map(x -> x.getAllergens())
                .forEach(x -> allergens.addAll(x));

        allergens.forEach(allergen -> ocvAllergens.add(new OurTagImplementation(allergen, allergen.getName(), OurTagImplementation.TagType.ALLERGEN_EXCLUDE)));
        if (this.ocvAllergens.getAdapter().count() == 0) {
            this.ocvAllergens.setVisibility(View.GONE);
        }

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    return WSConnection.getInstance().getImage(recipe.getId());
                } catch (RestClientException ex) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap image) {
                if (image != null) {
                    ivDescImage.setImageBitmap(image);
                } else {
                    ivDescImage.setImageResource(R.drawable.placeholder);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        this.addListeners();

        return view;
    }

    private void addListeners() {
        lvIngredients.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            RecipeIngredient ingredient = (RecipeIngredient) lvIngredients.getAdapter().getItem(position);
            dbHelper.addIngredient(ingredient);

            Toast.makeText(RecipeDescriptionFragment.this.getActivity(), String.format(getResources().getString(R.string.grocery_list_add_message),
                    IngredientsAdapter.getConvertedQuantityHumanreadable(ingredient.getQuantity()) + "" + IngredientsAdapter.getConvertedUnitHumanreadable(ingredient.getUnit(), ingredient.getQuantity())
                    + " " + ingredient.getIngredient().getName()), Toast.LENGTH_SHORT).show();
        });

        ibFavourites.setOnClickListener((View view) -> {
                if (fHelper.exists(recipe.getId())){
                    fHelper.removeFavorite(recipe.getId());
                    ibFavourites.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
                    ibFavourites.setTag(R.drawable.ic_star_border_black_24dp);
                }
                else {
                    fHelper.addFavorite(recipe.getId());
                    ibFavourites.setBackgroundResource(R.drawable.ic_star_yellow_24dp);
                    ibFavourites.setTag(R.drawable.ic_star_yellow_24dp);
                }
        });
        ibGrocery.setOnClickListener((View view) -> {
            recipe.getIngredients().forEach(i -> dbHelper.addIngredient(i));
            Toast.makeText(getActivity(), getResources().getString(R.string.grocery_list_add_recipe_message), Toast.LENGTH_SHORT).show();
        });
    }

    private void showPopup(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .show();
    }
}



