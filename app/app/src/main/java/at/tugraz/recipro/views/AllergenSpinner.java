package at.tugraz.recipro.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import at.tugraz.recipro.data.Allergen;
import at.tugraz.recipro.ws.WSConnection;

public class AllergenSpinner extends AbstractSpinner<Allergen> {

    public AllergenSpinner(Context context) {
        super(context);
    }

    public AllergenSpinner(Context context, int mode) {
        super(context, mode);
    }

    public AllergenSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AllergenSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AllergenSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public AllergenSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
    }

    @Override
    protected List<Allergen> getValueList() {
        List<Allergen> list = WSConnection.getInstance().requestAllergens();
        ArrayList<Allergen> list1 = new ArrayList<>();
        list1.add(new Allergen("", "", ""));
        list1.addAll(list);
        return list1;
    }

    @Override
    protected OurTagImplementation getTagImplementation(Allergen value) {
        return new OurTagImplementation(value, value.getName(), tagType);
    }
}
