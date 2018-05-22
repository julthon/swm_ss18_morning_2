package at.tugraz.recipro.recipro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import at.tugraz.recipro.adapters.IngredientsAdapter;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.helper.GroceryListHelper;

public class GroceryListActivity extends AppCompatActivity {

    ListView lvGroceryListView = null;
    GroceryListHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        dbHelper = new GroceryListHelper(this.getApplicationContext());

        lvGroceryListView = findViewById(android.R.id.list);
        fireDbChangedEvent();
        lvGroceryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dbHelper.removeIngredient((RecipeIngredient) lvGroceryListView.getAdapter().getItem(position));
                fireDbChangedEvent();
            }
        });
    }

    private void fireDbChangedEvent() {
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(this, dbHelper.getIngredients());
        lvGroceryListView.setAdapter(ingredientsAdapter);
    }
}
