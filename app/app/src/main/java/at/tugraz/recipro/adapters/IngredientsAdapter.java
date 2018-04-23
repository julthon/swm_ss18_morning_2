package at.tugraz.recipro.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.recipro.R;

public class IngredientsAdapter extends ArrayAdapter<RecipeIngredient> {
    private static class ViewHolder {
        TextView tvQuantity;
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
            viewHolder.tvIngredient = (TextView) rowView.findViewById(R.id.tvIngredient);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        RecipeIngredient ingredient = getItem(position);
        viewHolder.tvQuantity.setText(ingredient.getQuantity());
        viewHolder.tvIngredient.setText(ingredient.getIngredient().getName());

        return rowView;
    }
}


