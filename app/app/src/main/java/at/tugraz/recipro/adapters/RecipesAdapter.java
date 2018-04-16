package at.tugraz.recipro.adapters;

import android.content.Context;
import android.media.Rating;
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

/**
 * Created by Richard on 16.04.2018.
 */

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

        Recipe recipe = getItem(position);

        ViewHolder viewHolder; // view holder pattern
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_recipe, parent, false);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.rbRating = (RatingBar) convertView.findViewById(R.id.rbRating);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(recipe.getTitle());
        viewHolder.tvTime.setText(String.valueOf(recipe.getTime()) + "min");
        viewHolder.rbRating.setRating((float) recipe.getRating());

        return convertView;
    }
}


