package at.tugraz.recipro.recipro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import at.tugraz.recipro.adapters.IngredientsAdapter;
import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.data.Unit;
import at.tugraz.recipro.helper.MyPantryListHelper;
import at.tugraz.recipro.ws.WSConnection;

public class MyPantryFragment extends Fragment {
    public static final String FRAGMENT_TAG = "MyPantryFragment";

    AutoCompleteTextView tvAutoCompleteIngredients;
    ImageButton btAddIngredient = null;
    ListView lvMyPantryView = null;
    Spinner spUnit = null;
    AutoCompleteTextView tvQuantity = null;

    MyPantryListHelper dbHelper;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private Ingredient selection = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        view = inflater.inflate(R.layout.fragment_mypantry, container, false);

        dbHelper = new MyPantryListHelper(view.getContext());

        lvMyPantryView = view.findViewById(R.id.lvMyPantry);
        btAddIngredient = view.findViewById(R.id.btAddIngredient);
        tvAutoCompleteIngredients = view.findViewById(R.id.tvAutoCompleteIngredients);
        spUnit = view.findViewById(R.id.spUnit);
        tvQuantity = view.findViewById(R.id.npQuantity);

        spUnit.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, Unit.values()));

        initializeIngredients();
        initializeIngredientsFilter();

        fireDbChangedEvent(lvMyPantryView);
        lvMyPantryView.setOnItemClickListener((parent, view1, position, id) -> {
            RecipeIngredient ing = (RecipeIngredient) lvMyPantryView.getAdapter().getItem(position);
            dbHelper.removeIngredient(ing);
            Toast.makeText(this.getActivity(), String.format(getResources().getString(R.string.mypantry_list_remove_message), ing.getIngredient().getName()), Toast.LENGTH_SHORT).show();
            fireDbChangedEvent(lvMyPantryView);
        });

        btAddIngredient.setOnClickListener(v -> {
            if (selection != null && !tvAutoCompleteIngredients.getText().toString().isEmpty()) {
                int quantity = 0;
                try {
                    quantity = Integer.parseInt(tvQuantity.getText().toString());
                } catch (NumberFormatException ex) {
                    return;
                }
                Unit unit = null;
                try {
                    unit = (Unit) spUnit.getSelectedItem();
                } catch (Exception ex) {
                    return;
                }
                if (unit.shortName.isEmpty())
                    return;
                dbHelper.addIngredient(new RecipeIngredient(selection, quantity, unit));
                fireDbChangedEvent(lvMyPantryView);
            }
        });

        return view;
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
                MyPantryFragment.this.ingredients.clear();
                MyPantryFragment.this.ingredients.addAll(ingredients.stream().distinct().collect(Collectors.toList()));
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initializeIngredientsFilter() {
        tvAutoCompleteIngredients.setThreshold(1);
        ArrayAdapter<Ingredient> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_dropdown_item_1line, ingredients);
        tvAutoCompleteIngredients.setAdapter(adapter);
        tvAutoCompleteIngredients.setOnItemClickListener((parent, view, position, id) -> selection = (Ingredient) parent.getItemAtPosition(position));
        tvAutoCompleteIngredients.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    selection = null;
            }
        });
    }

    private void fireDbChangedEvent(ListView listView) {
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(this.getActivity(), dbHelper.getIngredients());
        listView.setAdapter(ingredientsAdapter);
    }
}
