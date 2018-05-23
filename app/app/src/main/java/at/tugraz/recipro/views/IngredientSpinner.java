package at.tugraz.recipro.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.ws.WSConnection;

public class IngredientSpinner extends AbstractSpinner<Ingredient> {

    public IngredientSpinner(Context context) {
        super(context);
    }

    public IngredientSpinner(Context context, int mode) {
        super(context, mode);
    }

    public IngredientSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IngredientSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public IngredientSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public IngredientSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
    }

    @Override
    protected List<Ingredient> getValueList() {
        List<Ingredient> list = WSConnection.getInstance().requestIngredients();
        ArrayList<Ingredient> list1 = new ArrayList<>();
        list1.add(new Ingredient(-1, ""));
        list1.addAll(list);
        return list1;
    }

    @Override
    protected OurTagImplementation getTagImplementation(Ingredient value) {
        return new OurTagImplementation(0, value.getName(), tagType);
    }
}
