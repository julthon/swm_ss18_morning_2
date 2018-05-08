package at.tugraz.recipro.recipro;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.web.client.RestClientException;

import at.tugraz.recipro.adapters.IngredientsAdapter;
import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.ws.WSConnection;

public class RecipeDescriptionActivity extends AppCompatActivity {

    TextView tvDescTitle;
    ImageView ivDescImage;
    TextView tvDescTime;
    RatingBar rbDescRating;
    ListView lvIngredients;
    TextView tvDescription;

    @Override
    @SuppressLint("StaticFieldLeak")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_description);

        tvDescTitle = findViewById(R.id.tvDescTitle);
        ivDescImage = findViewById(R.id.ivDescImage);
        tvDescTime = findViewById(R.id.tvDescTime);
        rbDescRating = findViewById(R.id.rbDescRating);
        lvIngredients = findViewById(R.id.lvIngredients);
        tvDescription = findViewById(R.id.tvDescription);


        Bundle extras = getIntent().getExtras();
        final Recipe recipe;
        if(extras != null) {
            recipe = (Recipe) extras.get(getResources().getString(R.string.recipe));
        }
        else {
            finish();
            return;
        }

        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(this, recipe.getIngredients());
        lvIngredients.setAdapter(ingredientsAdapter);

        tvDescTitle.setText(recipe.getTitle());
        tvDescTime.setText(String.valueOf(recipe.getTime()));
        rbDescRating.setRating(((float) recipe.getRating()));
        tvDescription.setText(recipe.getDescription());


        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    return WSConnection.getImage(recipe.getId());
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
    }
}
