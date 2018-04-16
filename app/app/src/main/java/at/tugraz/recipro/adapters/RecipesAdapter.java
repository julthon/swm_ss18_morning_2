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

import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.recipro.R;

public class RecipesAdapter extends ArrayAdapter<Recipe> {
    private static class ViewHolder {
        TextView tvTitle;
        TextView tvTime;
        RatingBar rbRating;
    }

    public RecipesAdapter(@NonNull Context context, ArrayList<Recipe> recipes) {
        super(context, 0 , recipes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        ViewHolder viewHolder; // view holder pattern
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(R.layout.item_recipe, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) rowView.findViewById(R.id.tvTitle);
            viewHolder.tvTime = (TextView) rowView.findViewById(R.id.tvTime);
            viewHolder.rbRating = (RatingBar) rowView.findViewById(R.id.rbRating);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        Recipe recipe = getItem(position);
        viewHolder.tvTitle.setText(recipe.getTitle());
        viewHolder.tvTime.setText(String.valueOf(recipe.getTime()) + "min");
        viewHolder.rbRating.setRating((float) recipe.getRating());

        return rowView;
    }
}


