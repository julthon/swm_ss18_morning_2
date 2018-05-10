package at.tugraz.recipro.Views;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.recipro.WSConnection;

public class IngredientSpinner extends AppCompatSpinner {

    private static ArrayAdapter<Ingredient> adapter = null;

    private void setupArrayAdapter() {
        if(adapter == null) {
            adapter = new ArrayAdapter<Ingredient>(super.getContext(), 0, new ArrayList<>());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    adapter.addAll(WSConnection.getInstance().requestIngredients());
                }
            }).start();
        }
    }

    public IngredientSpinner(Context context) {
        super(context);
        setupArrayAdapter();
    }

    public IngredientSpinner(Context context, int mode) {
        super(context, mode);
        setupArrayAdapter();
    }

    public IngredientSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupArrayAdapter();
    }

    public IngredientSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupArrayAdapter();
    }

    public IngredientSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        setupArrayAdapter();
    }

    public IngredientSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
        setupArrayAdapter();
    }
}
