package at.tugraz.recipro.recipro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import at.tugraz.recipro.adapters.IngredientsAdapter;
import at.tugraz.recipro.data.Recipe;

public class RecipeDescriptionActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvTime;
    RatingBar rbRating;
    ListView lvIngredients;
    TextView tvDescription;
    EditText etPortions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_description);

        tvTitle = findViewById(R.id.tvTitle);
        tvTime = findViewById(R.id.tvTime);
        rbRating = findViewById(R.id.rbRating);
        lvIngredients = findViewById(R.id.lvIngredients);
        tvDescription = findViewById(R.id.tvDescription);
        etPortions = findViewById(R.id.etNumberOfPortions);


        Bundle extras = getIntent().getExtras();
        Recipe recipe;
        if(extras != null) {
            recipe = (Recipe) extras.get(getResources().getString(R.string.recipe));
        }
        else {
            finish();
            return;
        }

        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(this, recipe.getIngredients());
        lvIngredients.setAdapter(ingredientsAdapter);

        tvTitle.setText(recipe.getTitle());
        tvTime.setText(String.valueOf(recipe.getTime()) + "min");
        rbRating.setRating(((float) recipe.getRating()));
        tvDescription.setText(recipe.getDescription());
        etPortions.setText(recipe.getServings());
    }
}
