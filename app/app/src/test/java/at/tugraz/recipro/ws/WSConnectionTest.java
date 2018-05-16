package at.tugraz.recipro.ws;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.tugraz.recipro.TestUtils;
import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.recipro.R;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class WSConnectionTest {

    @Test
    public void preparationTime() {
        int minpreptime = 50;
        int maxpreptime = 200;

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(WSConstants.QUERY_MIN_PREP, Integer.toString(minpreptime));
        queryParams.put(WSConstants.QUERY_MAX_PREP, Integer.toString(maxpreptime));
        List<Recipe> recipes = WSConnection.getInstance().requestRecipes(queryParams);

        for (Recipe recipe : recipes) {
            assertTrue(recipe.getTime() > minpreptime);
            assertTrue(recipe.getTime() < maxpreptime);
        }
    }

    @Test
    public void minPreparationTime() {
        int minpreptime = 50;

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(WSConstants.QUERY_MIN_PREP, Integer.toString(minpreptime));
        List<Recipe> recipes = WSConnection.getInstance().requestRecipes(queryParams);

        for (Recipe recipe : recipes) {
            assertTrue(recipe.getTime() > minpreptime);
        }
    }

    @Test
    public void maxPreparationTime() {
        int maxpreptime = 200;

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(WSConstants.QUERY_MAX_PREP, Integer.toString(maxpreptime));
        List<Recipe> recipes = WSConnection.getInstance().requestRecipes(queryParams);

        for (Recipe recipe : recipes) {
            assertTrue(recipe.getTime() < maxpreptime);
        }
    }

    @Test
    public void testPostAndGetImageSuccessful() throws IOException {
        byte[] image = IOUtils.toByteArray(mActivityRule.getActivity().getResources().openRawResource(R.drawable.taco));
        boolean result = WSConnection.postImage(1, image, ImageType.JPEG);
        assertTrue(result);

        Bitmap receivedImage = WSConnection.getImage(1);
        byte[] originalImageBytes = IOUtils.toByteArray(mActivityRule.getActivity().getResources().openRawResource(R.drawable.taco));

        Bitmap originalImage = BitmapFactory.decodeByteArray(originalImageBytes, 0, originalImageBytes.length);

        assertThat(receivedImage.getHeight(), is(originalImage.getHeight()));
        assertThat(receivedImage.getWidth(), is(originalImage.getWidth()));

        for (int x = 0; x < receivedImage.getWidth(); ++x) {
            for (int y = 0; y < receivedImage.getHeight(); ++y) {
                assertThat(receivedImage.getPixel(x, y), is(originalImage.getPixel(x, y)));
            }
        }
    }

    @Test
    public void testGetImageFailure() {
        int random = TestUtils.getRandomBetween(5000, 10000);
        Bitmap receivedImage = WSConnection.getImage(random);
        assertNull(receivedImage);
    }

    @Test
    public void minRating() {
        double minrating = 2;

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(WSConstants.QUERY_MIN_PREP, Double.toString(minrating));
        List<Recipe> recipes = WSConnection.getInstance().requestRecipes(queryParams);

        for (Recipe recipe : recipes) {
            assertTrue(recipe.getRating() > minrating);
        }
    }
}