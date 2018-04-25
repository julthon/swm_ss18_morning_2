package at.tugraz.recipro.recipro;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.tugraz.recipro.data.Recipe;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testPreparationTime() {
        Map<String, String> queryParams = new HashMap<>();
        int minpreptime = 50;
        int maxpreptime = 200;

        queryParams.put("minpreptime", Integer.toString(minpreptime));
        queryParams.put("maxpreptime", Integer.toString(maxpreptime));
        List<Recipe> recipes = WSConnection.sendQuery(queryParams);

        for (Recipe recipe: recipes) {
            assertTrue(recipe.getTime() > minpreptime);
            assertTrue(recipe.getTime() < maxpreptime);
        }
    }
}