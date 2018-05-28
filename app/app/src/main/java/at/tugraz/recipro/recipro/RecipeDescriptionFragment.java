package at.tugraz.recipro.recipro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.web.client.RestClientException;

import at.tugraz.recipro.adapters.IngredientsAdapter;
import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.helper.GroceryListHelper;
import at.tugraz.recipro.ws.WSConnection;

public class RecipeDescriptionFragment extends Fragment {
    public static final String FRAGMENT_TAG = "RecipeDescriptionFragment";

    TextView tvDescTitle;
    ImageView ivDescImage;
    TextView tvDescTime;
    RatingBar rbDescRating;
    ListView lvIngredients;
    TextView tvDescription;

    GroceryListHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        view = inflater.inflate(R.layout.fragment_recipe_description, container, false);

        dbHelper = new GroceryListHelper(this.getContext());
        tvDescTitle = view.findViewById(R.id.tvDescTitle);
        ivDescImage = view.findViewById(R.id.ivDescImage);
        tvDescTime = view.findViewById(R.id.tvDescTime);
        rbDescRating = view.findViewById(R.id.rbDescRating);
        lvIngredients = view.findViewById(R.id.lvIngredients);
        tvDescription = view.findViewById(R.id.tvDescription);

        Bundle arguments = getArguments();
        final Recipe recipe;
        if(arguments != null) {
            recipe = (Recipe) arguments.get(getResources().getString(R.string.recipe));
        }
        else {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStackImmediate();
            return view;
        }

        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(getContext(), recipe.getIngredients());
        lvIngredients.setAdapter(ingredientsAdapter);

        tvDescTitle.setText(recipe.getTitle());
        tvDescTime.setText(String.valueOf(recipe.getTime()) + getResources().getString(R.string.minutes));
        rbDescRating.setRating(((float) recipe.getRating()));
        tvDescription.setText(recipe.getDescription());


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
        addListeners();

        return view;
    }

    private void addListeners() {
        lvIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecipeIngredient ingredient = (RecipeIngredient) lvIngredients.getAdapter().getItem(position);
                //if(!dbHelper.addIngredient(ingredient))
                dbHelper.addIngredient(ingredient);
                Toast.makeText(RecipeDescriptionFragment.this.getActivity(), String.format(getResources().getString(R.string.grocery_list_add_message), ingredient.getIngredient().getName()), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
