package at.tugraz.recipro.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.data.Unit;
import at.tugraz.recipro.helper.ResourceAccessHelper;
import at.tugraz.recipro.recipro.R;

public class IngredientsAdapter extends ArrayAdapter<RecipeIngredient> {
    private static class ViewHolder {
        TextView tvQuantity;
        TextView tvUnit;
        TextView tvIngredient;
    }

    public IngredientsAdapter(@NonNull Context context, List<RecipeIngredient> recipes) {
        super(context, 0 , recipes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        ViewHolder viewHolder; // view holder pattern
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(R.layout.item_ingredient, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvQuantity = (TextView) rowView.findViewById(R.id.tvQuantity);
            viewHolder.tvUnit = (TextView) rowView.findViewById(R.id.tvUnit);
            viewHolder.tvIngredient = (TextView) rowView.findViewById(R.id.tvIngredient);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        RecipeIngredient ingredient = getItem(position);
        viewHolder.tvQuantity.setText(getConvertedQuantityHumanreadable(ingredient.getQuantity()));
        viewHolder.tvUnit.setText(getConvertedUnitHumanreadable(ingredient.getUnit(), ingredient.getQuantity()));
        viewHolder.tvIngredient.setText(ingredient.getIngredient().getName());

        return rowView;
    }

    public static String getConvertedUnitHumanreadable(Unit unit, float quantity) {
        switch (unit) {
            case NONE:
                return "";
            case GRAM:
                if (quantity >= 1000f) return ResourceAccessHelper.getAppContext().getResources().getString(R.string.unit_kilogram);
                else return ResourceAccessHelper.getAppContext().getResources().getString(R.string.unit_gram);
            case MILLILITER:
                if (quantity >= 1000f) return ResourceAccessHelper.getAppContext().getResources().getString(R.string.unit_liter);
                else return ResourceAccessHelper.getAppContext().getResources().getString(R.string.unit_milliliter);
            default:
                return "";
        }
    }

    public static float getConvertedQuantity(float quantity) {
        if (quantity >= 1000f)
            return quantity / 1000f;
        else
            return quantity;
    }
  
    public static String getConvertedQuantityHumanreadable(float quantity) {
        float convertedQuantity = quantity;

        if (quantity >= 1000f) {
            convertedQuantity = quantity / 1000f;
        }

        if (convertedQuantity == (long) convertedQuantity) {
            return String.format("%d", (long) convertedQuantity);
        } else {
            return String.format("%s", convertedQuantity);
        }
    }
}


