package at.tugraz.recipro.recipro;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.helper.ResourceAccessHelper;

import static org.junit.Assert.assertTrue;

public class MainActivityTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testPreparationTime() {
        Map<String, String> queryParams = new HashMap<>();
        int minpreptime = 50;
        int maxpreptime = 200;

        queryParams.put(ResourceAccessHelper.getStringFromId(R.string.min_prep), Integer.toString(minpreptime));
        queryParams.put(ResourceAccessHelper.getStringFromId(R.string.max_prep), Integer.toString(maxpreptime));
        List<Recipe> recipes = WSConnection.sendQuery(queryParams);

        for (Recipe recipe: recipes) {
            assertTrue(recipe.getTime() > minpreptime);
            assertTrue(recipe.getTime() < maxpreptime);
        }
    }
}