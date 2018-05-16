package at.tugraz.recipro.helper;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.data.RecipeIngredient;

import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class RecipeUtilsTest {

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

        List<Recipe> filteredRecipes = RecipeUtils.filterRecipes(recipes, filterIngredients);

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

        List<Recipe> filteredRecipes = RecipeUtils.filterRecipes(recipes, filterIngredients);

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

        List<Recipe> filteredRecipes = RecipeUtils.filterRecipes(recipes, filterIngredients);

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

        List<Recipe> filteredRecipes = RecipeUtils.filterRecipes(recipes, filterIngredients);

        assertThat(filteredRecipes.size(), is(1));
    }
}