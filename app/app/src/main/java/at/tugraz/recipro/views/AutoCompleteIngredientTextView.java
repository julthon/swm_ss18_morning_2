package at.tugraz.recipro.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.recipro.R;
import at.tugraz.recipro.ws.WSConnection;

public class AutoCompleteIngredientTextView extends ConstraintLayout {

    public interface OnIngredientSelectedListener {
        void onSelected(Ingredient ingredient, int position, View view);
    }

    private Ingredient currentlySelected;
    ArrayList<OnIngredientSelectedListener> listeners = new ArrayList<>();

    private AutoCompleteTextView tvIngredients;
    private ImageButton ibAddIngredient;

    public AutoCompleteIngredientTextView(Context context) {
        super(context);
        inflate(getContext(), R.layout.view_ingredient_text_view,this);
        addConstantOnSelectedListener();
        tvIngredients = findViewById(R.id.tvAutoCompleteIngredients);
        ibAddIngredient = findViewById(R.id.btAddIngredient);
    }

    private void addConstantOnSelectedListener() {
        ibAddIngredient.setOnClickListener(v -> {
            int selection = tvIngredients.getListSelection();
            if(selection != ListView.INVALID_POSITION) {
                currentlySelected = (Ingredient) tvIngredients.getAdapter().getItem(selection);
                for (OnIngredientSelectedListener list : listeners) {
                    list.onSelected(currentlySelected, selection, v);
                }
            } else
                currentlySelected = null;
        });
    }

    protected List<Ingredient> getValueList() {
        List<Ingredient> list = WSConnection.getInstance().requestIngredients();
        ArrayList<Ingredient> list1 = new ArrayList<>();
        list1.add(new Ingredient(-1, ""));
        list1.addAll(list);
        return list1;
    }

    public void addOnSelectionListener(OnIngredientSelectedListener listener) {
        listeners.add(listener);
    }
}
