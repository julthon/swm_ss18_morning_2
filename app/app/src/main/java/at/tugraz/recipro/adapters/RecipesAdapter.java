package at.tugraz.recipro.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.recipro.R;
import at.tugraz.recipro.recipro.RecipeDescriptionActivity;
import at.tugraz.recipro.recipro.WSConnection;

public class RecipesAdapter extends ArrayAdapter<Recipe> {
    private static class ViewHolder {
        TextView tvTitle;
        ImageView ivThumbnail;
        TextView tvTime;
        RatingBar rbRating;
    }

    public RecipesAdapter(@NonNull Context context, List<Recipe> recipes) {
        super(context, 0 , recipes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        final ViewHolder viewHolder; // view holder pattern
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(R.layout.item_recipe, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) rowView.findViewById(R.id.tvTitle);
            viewHolder.ivThumbnail = (ImageView) rowView.findViewById(R.id.ivThumbnail);
            viewHolder.tvTime = (TextView) rowView.findViewById(R.id.tvTime);
            viewHolder.rbRating = (RatingBar) rowView.findViewById(R.id.rbRating);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        final Recipe recipe = getItem(position);
        viewHolder.tvTitle.setText(recipe.getTitle());
        viewHolder.tvTime.setText(String.valueOf(recipe.getTime()) + "min");
        viewHolder.rbRating.setRating((float) recipe.getRating());

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    return WSConnection.getImage(recipe.getId());
                } catch (RestClientException ex) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap image) {
                if (image != null) {
                    viewHolder.ivThumbnail.setImageBitmap(image);
                } else {
                    viewHolder.ivThumbnail.setImageResource(R.drawable.placeholder);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return rowView;
    }
}


