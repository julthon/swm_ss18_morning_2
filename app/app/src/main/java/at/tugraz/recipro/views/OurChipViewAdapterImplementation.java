package at.tugraz.recipro.views;

import android.content.Context;
import android.view.View;

import com.plumillonforge.android.chipview.ChipViewAdapter;

import at.tugraz.recipro.recipro.R;

public class OurChipViewAdapterImplementation extends ChipViewAdapter {

    public OurChipViewAdapterImplementation(Context context) {
        super(context);
    }

    @Override
    public int getLayoutRes(int position) {
        return 0;
    }

    @Override
    public int getBackgroundRes(int position) {
        OurTagImplementation c = (OurTagImplementation) getChip(position);
        switch(c.getTagType()) {
            case INGREDIENT_INCLUDE:
                return R.color.tag_ingredient_include;
            case INGREDIENT_EXCLUDE:
                return R.color.tag_ingredient_exclude;
            case ALLERGEN:
                return R.color.tag_allergen_exclude;
            case RECIPE_TYPE:
                return R.color.tag_recipe_type;
            default:
                return R.color.tag_ingredient_include;
        }
    }

    @Override
    public int getBackgroundColor(int position) {
        return 0;
    }

    @Override
    public int getBackgroundColorSelected(int position) {
        return 0;
    }

    @Override
    public void onLayout(View view, int position) {
    }
}
