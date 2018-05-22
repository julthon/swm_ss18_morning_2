package at.tugraz.recipro.recipro;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import at.tugraz.recipro.adapters.IngredientsAdapter;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.helper.GroceryListHelper;

public class GroceryListFragment extends Fragment {

    ListView lvGroceryListView = null;
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
        view = inflater.inflate(R.layout.fragment_grocery_list, container, false);

        dbHelper = new GroceryListHelper(view.getContext());

        lvGroceryListView = view.findViewById(R.id.lvGroceryList);
        fireDbChangedEvent(lvGroceryListView);
        lvGroceryListView.setOnItemClickListener((parent, view1, position, id) -> {
            dbHelper.removeIngredient((RecipeIngredient) lvGroceryListView.getAdapter().getItem(position));
            fireDbChangedEvent(lvGroceryListView);
        });
        return view;
    }

    private void fireDbChangedEvent(ListView listView) {
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(this.getActivity(), dbHelper.getIngredients());
        listView.setAdapter(ingredientsAdapter);
    }
}
