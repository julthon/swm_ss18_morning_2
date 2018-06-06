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

import org.springframework.web.client.RestClientException;

import java.util.List;

import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.helper.FavoritesHelper;
import at.tugraz.recipro.recipro.R;
import at.tugraz.recipro.ws.WSConnection;

public class RecipesAdapter extends ArrayAdapter<Recipe> {
    private static class ViewHolder {
        TextView tvTitle;
        ImageView ivThumbnail;
        TextView tvTime;
        RatingBar rbRating;
        ImageView ivFavourite;
        FavoritesHelper fHelper;
    }

    public RecipesAdapter(@NonNull Context context, List<Recipe> recipes) {
        super(context, 0, recipes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        final ViewHolder viewHolder; // view holder pattern
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(R.layout.item_recipe, parent, false);


            viewHolder = new ViewHolder();
            viewHolder.fHelper = new FavoritesHelper(this.getContext());
            viewHolder.ivFavourite = rowView.findViewById(R.id.ivShowFavourite);
            viewHolder.tvTitle = rowView.findViewById(R.id.tvTitle);
            viewHolder.ivThumbnail = rowView.findViewById(R.id.ivThumbnail);
            viewHolder.tvTime = rowView.findViewById(R.id.tvTime);
            viewHolder.rbRating = rowView.findViewById(R.id.rbRating);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        final Recipe recipe = getItem(position);
        viewHolder.tvTitle.setText(recipe.getTitle());
        viewHolder.tvTime.setText(String.valueOf(recipe.getTime()) + this.getContext().getResources().getString(R.string.minutes));
        viewHolder.rbRating.setRating((float) recipe.getRating());
        if (viewHolder.fHelper.exists(recipe.getId())) {
            viewHolder.ivFavourite.setBackgroundResource(R.drawable.ic_star_yellow_24dp);
            viewHolder.ivFavourite.setTag(R.drawable.ic_star_yellow_24dp);
        }


        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    return WSConnection.getInstance().getImage(recipe.getId());
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


