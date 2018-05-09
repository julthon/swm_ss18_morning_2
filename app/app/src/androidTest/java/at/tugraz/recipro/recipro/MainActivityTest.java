package at.tugraz.recipro.recipro;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.data.RecipeIngredient;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
        int minpreptime = 50;
        int maxpreptime = 200;

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(mActivityRule.getActivity().getResources().getString(R.string.min_prep), Integer.toString(minpreptime));
        queryParams.put(mActivityRule.getActivity().getResources().getString(R.string.max_prep), Integer.toString(maxpreptime));
        List<Recipe> recipes = WSConnection.sendQuery(queryParams);

        for (Recipe recipe: recipes) {
            assertTrue(recipe.getTime() > minpreptime);
            assertTrue(recipe.getTime() < maxpreptime);
        }
    }

    @Test
    public void testMinPreparationTime() {
        int minpreptime = 50;

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(mActivityRule.getActivity().getResources().getString(R.string.min_prep), Integer.toString(minpreptime));
        List<Recipe> recipes = WSConnection.sendQuery(queryParams);

        for (Recipe recipe: recipes) {
            assertTrue(recipe.getTime() > minpreptime);
        }
    }

    @Test
    public void testMaxPreparationTime() {
        int maxpreptime = 200;

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(mActivityRule.getActivity().getResources().getString(R.string.max_prep), Integer.toString(maxpreptime));
        List<Recipe> recipes = WSConnection.sendQuery(queryParams);

        for (Recipe recipe: recipes) {
            assertTrue(recipe.getTime() < maxpreptime);
        }
    }

    @Test
    public void filterByIngredients() {

        List<RecipeIngredient> ingredients = new ArrayList<>();
        RecipeIngredient flour = new RecipeIngredient(new Ingredient("flour"), "120");
        RecipeIngredient salt = new RecipeIngredient(new Ingredient("salt"), "120");
        RecipeIngredient sugar = new RecipeIngredient(new Ingredient("sugar"), "120");
        RecipeIngredient cacao = new RecipeIngredient(new Ingredient("cacao"), "120");
        RecipeIngredient cheese = new RecipeIngredient(new Ingredient("cheese"), "120");

        List<Recipe> recipes = new ArrayList<>();

        recipes.add(new Recipe(1, "cake", 120, 5.0, Arrays.asList(flour, cacao, sugar), "Good cake"));
        recipes.add(new Recipe(2, "cheesecake", 120, 5.0, Arrays.asList(cheese), "Good cheesecake"));
        recipes.add(new Recipe(3, "schnitzel", 120, 5.0, Arrays.asList(salt, flour), "Good schnitzel"));

        List<RecipeIngredient> filterIngredients = new ArrayList<>();
        filterIngredients.add(flour);
        filterIngredients.add(cacao);

        List<Recipe> filteredRecipes = mActivityRule.getActivity().filterRecipes(recipes, filterIngredients);

        assertThat(filteredRecipes.size(), is(2));

        for (Recipe recipe : filteredRecipes) {
            //recipe.getIngredients().stream().anyMatch(x -> x.getIngredient().getName());
            //for (RecipeIngredient recipeIngredients : )
        }
    }
}