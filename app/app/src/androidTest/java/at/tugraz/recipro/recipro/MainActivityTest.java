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

import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void preparationTime() {
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
    public void minPreparationTime() {
        int minpreptime = 50;

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(mActivityRule.getActivity().getResources().getString(R.string.min_prep), Integer.toString(minpreptime));
        List<Recipe> recipes = WSConnection.sendQuery(queryParams);

        for (Recipe recipe: recipes) {
            assertTrue(recipe.getTime() > minpreptime);
        }
    }

    @Test
    public void maxPreparationTime() {
        int maxpreptime = 200;

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(mActivityRule.getActivity().getResources().getString(R.string.max_prep), Integer.toString(maxpreptime));
        List<Recipe> recipes = WSConnection.sendQuery(queryParams);

        for (Recipe recipe: recipes) {
            assertTrue(recipe.getTime() < maxpreptime);
        }
    }

    @Test
    public void filterByIngredientsAllMatches() {
        RecipeIngredient flour = new RecipeIngredient(new Ingredient("flour"), "120");
        RecipeIngredient salt = new RecipeIngredient(new Ingredient("salt"), "120");
        RecipeIngredient sugar = new RecipeIngredient(new Ingredient("sugar"), "120");
        RecipeIngredient cacao = new RecipeIngredient(new Ingredient("cacao"), "120");
        RecipeIngredient cheese = new RecipeIngredient(new Ingredient("cheese"), "120");

        Recipe recipe1 = new Recipe(1, "cake", 120, 5.0, Arrays.asList(flour, cheese, cacao, sugar), "Good cake");
        Recipe recipe2 = new Recipe(2, "cheesecake", 120, 5.0, Arrays.asList(cheese), "Good cheesecake");
        Recipe recipe3 = new Recipe(3, "schnitzel", 120, 5.0, Arrays.asList(salt, flour), "Good schnitzel");
        List<Recipe> recipes = Arrays.asList(recipe1, recipe2, recipe3);

        List<RecipeIngredient> filterIngredients = new ArrayList<>();
        filterIngredients.add(flour);
        filterIngredients.add(cheese);
        filterIngredients.add(salt);
        filterIngredients.add(sugar);

        List<Recipe> filteredRecipes = mActivityRule.getActivity().filterRecipes(recipes, filterIngredients);

        assertThat(filteredRecipes.size(), is(3));

        for (Recipe recipe : filteredRecipes) {
            assertThat(recipe.getIngredients(), either(hasItem(flour)).or(hasItem(cheese)).or(hasItem(salt)).or(hasItem(sugar)));
        }

        assertThat(filteredRecipes, contains(recipe1, recipe3, recipe2));
    }

    @Test
    public void filterByIngredientsPartialMatches() {
        RecipeIngredient flour = new RecipeIngredient(new Ingredient("flour"), "120");
        RecipeIngredient salt = new RecipeIngredient(new Ingredient("salt"), "120");
        RecipeIngredient sugar = new RecipeIngredient(new Ingredient("sugar"), "120");
        RecipeIngredient cacao = new RecipeIngredient(new Ingredient("cacao"), "120");
        RecipeIngredient cheese = new RecipeIngredient(new Ingredient("cheese"), "120");

        Recipe recipe1 = new Recipe(1, "cake", 120, 5.0, Arrays.asList(flour, cacao, sugar), "Good cake");
        Recipe recipe2 = new Recipe(2, "cheesecake", 120, 5.0, Arrays.asList(cheese), "Good cheesecake");
        Recipe recipe3 = new Recipe(3, "schnitzel", 120, 5.0, Arrays.asList(salt, flour), "Good schnitzel");
        List<Recipe> recipes = Arrays.asList(recipe1, recipe2, recipe3);

        List<RecipeIngredient> filterIngredients = new ArrayList<>();
        filterIngredients.add(flour);
        filterIngredients.add(cacao);

        List<Recipe> filteredRecipes = mActivityRule.getActivity().filterRecipes(recipes, filterIngredients);

        assertThat(filteredRecipes.size(), is(2));

        for (Recipe recipe : filteredRecipes) {
            assertThat(recipe.getIngredients(), either(hasItem(flour)).or(hasItem(cacao)));
        }

        assertThat(filteredRecipes, contains(recipe1, recipe3));
    }

    @Test
    public void filterByIngredientsNoMatches() {
        RecipeIngredient flour = new RecipeIngredient(new Ingredient("flour"), "120");
        RecipeIngredient salt = new RecipeIngredient(new Ingredient("salt"), "120");
        RecipeIngredient sugar = new RecipeIngredient(new Ingredient("sugar"), "120");
        RecipeIngredient cacao = new RecipeIngredient(new Ingredient("cacao"), "120");
        RecipeIngredient cheese = new RecipeIngredient(new Ingredient("cheese"), "120");

        Recipe recipe1 = new Recipe(1, "cake", 120, 5.0, Arrays.asList(flour, cacao, sugar), "Good cake");
        Recipe recipe2 = new Recipe(2, "cheesecake", 120, 5.0, Arrays.asList(cheese), "Good cheesecake");
        Recipe recipe3 = new Recipe(3, "schnitzel", 120, 5.0, Arrays.asList(flour), "Good schnitzel");
        List<Recipe> recipes = Arrays.asList(recipe1, recipe2, recipe3);

        List<RecipeIngredient> filterIngredients = new ArrayList<>();
        filterIngredients.add(salt);

        List<Recipe> filteredRecipes = mActivityRule.getActivity().filterRecipes(recipes, filterIngredients);

        assertThat(filteredRecipes.size(), is(0));
    }

    @Test
    public void filterByIngredientsDifferentReferences() {
        RecipeIngredient flour = new RecipeIngredient(new Ingredient("flour"), "120");
        RecipeIngredient flour2 = new RecipeIngredient(new Ingredient("flour"), "120");

        Recipe recipe = new Recipe(1, "cake", 120, 5.0, Arrays.asList(flour), "Good cake");
        List<Recipe> recipes = Arrays.asList(recipe);

        List<RecipeIngredient> filterIngredients = new ArrayList<>();
        filterIngredients.add(flour2);

        List<Recipe> filteredRecipes = mActivityRule.getActivity().filterRecipes(recipes, filterIngredients);

        assertThat(filteredRecipes.size(), is(1));
    }
}