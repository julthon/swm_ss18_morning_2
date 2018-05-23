package at.tugraz.recipro.ws;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.tugraz.recipro.TestUtils;
import at.tugraz.recipro.data.Recipe;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class WSConnectionTest {

    private WSConnection wsConnection;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
        this.wsConnection = WSConnection.getInstance();
        this.wsConnection.init("http://localhost:8080/recipro-backend/api");
    }

    @Test
    public void preparationTime() {
        int minpreptime = 50;
        int maxpreptime = 200;

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.put(WSConstants.QUERY_MIN_PREP, Arrays.asList(Integer.toString(minpreptime)));
        queryParams.put(WSConstants.QUERY_MAX_PREP, Arrays.asList(Integer.toString(maxpreptime)));
        List<Recipe> recipes = this.wsConnection.requestRecipes(queryParams);

        for (Recipe recipe : recipes) {
            assertTrue(recipe.getTime() > minpreptime);
            assertTrue(recipe.getTime() < maxpreptime);
        }
    }

    @Test
    public void minPreparationTime() {
        int minpreptime = 50;

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.put(WSConstants.QUERY_MIN_PREP, Arrays.asList(Integer.toString(minpreptime)));
        List<Recipe> recipes = this.wsConnection.requestRecipes(queryParams);

        for (Recipe recipe : recipes) {
            assertTrue(recipe.getTime() > minpreptime);
        }
    }

    @Test
    public void maxPreparationTime() {
        int maxpreptime = 200;

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.put(WSConstants.QUERY_MAX_PREP, Arrays.asList(Integer.toString(maxpreptime)));
        List<Recipe> recipes = this.wsConnection.requestRecipes(queryParams);

        for (Recipe recipe : recipes) {
            assertTrue(recipe.getTime() < maxpreptime);
        }
    }

    @Test
    public void testPostAndGetImageSuccessful() throws IOException, URISyntaxException {
        byte[] image = IOUtils.toByteArray(this.getClass().getClassLoader().getResource("taco.jpeg"));
        boolean result = this.wsConnection.postImage(1, image, ImageType.JPEG);
        assertTrue(result);

        Bitmap receivedImage = this.wsConnection.getImage(1);
        Bitmap originalImage = BitmapFactory.decodeStream(this.getClass().getClassLoader().getResourceAsStream("taco.jpeg"));

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
        Bitmap receivedImage = this.wsConnection.getImage(random);
        assertNull(receivedImage);
    }

    @Test
    public void minRating() {
        double minrating = 2;

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.put(WSConstants.QUERY_MIN_RATING, Arrays.asList(Double.toString(minrating)));
        List<Recipe> recipes = this.wsConnection.requestRecipes(queryParams);

        for (Recipe recipe : recipes) {
            assertTrue(recipe.getRating() >= minrating);
        }
    }
}