package at.tugraz.recipro.recipro;

import android.graphics.Bitmap;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.tugraz.recipro.data.Recipe;

import static at.tugraz.recipro.recipro.WSConnection.*;
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
        List<Recipe> recipes = sendQuery(queryParams);

        for (Recipe recipe: recipes) {
            assertTrue(recipe.getTime() > minpreptime);
            assertTrue(recipe.getTime() < maxpreptime);
        }
    }

    @Test
    public void postImageTest() {

        postImage(100);
    }
}