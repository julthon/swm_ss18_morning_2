package at.tugraz.recipro.recipro;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.ws.ImageType;
import at.tugraz.recipro.ws.WSConnection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testPreparationTime() {
        Map<String, String> queryParams = new HashMap<>();
        int minpreptime = 50;
        int maxpreptime = 200;

        queryParams.put(mActivityRule.getActivity().getResources().getString(R.string.min_prep), Integer.toString(minpreptime));
        queryParams.put(mActivityRule.getActivity().getResources().getString(R.string.max_prep), Integer.toString(maxpreptime));
        List<Recipe> recipes = WSConnection.sendQuery(queryParams);

        for (Recipe recipe : recipes) {
            assertTrue(recipe.getTime() > minpreptime);
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
}