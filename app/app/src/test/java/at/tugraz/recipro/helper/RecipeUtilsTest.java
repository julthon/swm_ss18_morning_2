package at.tugraz.recipro.helper;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.data.Unit;

import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertEquals;

public class RecipeUtilsTest {

    @Test
    public void filterByIngredientsAllMatches() {
        RecipeIngredient flour = new RecipeIngredient(new Ingredient(1, "flour"), 120f, Unit.GRAM);
        RecipeIngredient salt = new RecipeIngredient(new Ingredient(2, "salt"), 120f, Unit.GRAM);
        RecipeIngredient sugar = new RecipeIngredient(new Ingredient(3, "sugar"), 120f, Unit.GRAM);
        RecipeIngredient cacao = new RecipeIngredient(new Ingredient(4, "cacao"), 120f, Unit.GRAM);
        RecipeIngredient cheese = new RecipeIngredient(new Ingredient(5, "cheese"), 120f, Unit.GRAM);

        Recipe recipe1 = new Recipe(1, "cake", 120, 4, 5.0, Arrays.asList(flour, cheese, cacao, sugar), "Good cake");
        Recipe recipe2 = new Recipe(2, "cheesecake", 120, 4, 5.0, Arrays.asList(cheese), "Good cheesecake");
        Recipe recipe3 = new Recipe(3, "schnitzel", 120, 4, 5.0, Arrays.asList(salt, flour), "Good schnitzel");
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
        RecipeIngredient flour = new RecipeIngredient(new Ingredient(1, "flour"), 120f, Unit.GRAM);
        RecipeIngredient salt = new RecipeIngredient(new Ingredient(2, "salt"), 120f, Unit.GRAM);
        RecipeIngredient sugar = new RecipeIngredient(new Ingredient(3, "sugar"), 120f, Unit.GRAM);
        RecipeIngredient cacao = new RecipeIngredient(new Ingredient(4, "cacao"), 120f, Unit.GRAM);
        RecipeIngredient cheese = new RecipeIngredient(new Ingredient(5, "cheese"), 120f, Unit.GRAM);

        Recipe recipe1 = new Recipe(1, "cake", 120, 4, 5.0, Arrays.asList(flour, cacao, sugar), "Good cake");
        Recipe recipe2 = new Recipe(2, "cheesecake", 4, 120, 5.0, Arrays.asList(cheese), "Good cheesecake");
        Recipe recipe3 = new Recipe(3, "schnitzel", 4, 120, 5.0, Arrays.asList(salt, flour), "Good schnitzel");
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
        RecipeIngredient flour = new RecipeIngredient(new Ingredient(1, "flour"), 120f, Unit.GRAM);
        RecipeIngredient salt = new RecipeIngredient(new Ingredient(2, "salt"), 120f, Unit.GRAM);
        RecipeIngredient sugar = new RecipeIngredient(new Ingredient(3, "sugar"), 120f, Unit.GRAM);
        RecipeIngredient cacao = new RecipeIngredient(new Ingredient(4, "cacao"), 120f, Unit.GRAM);
        RecipeIngredient cheese = new RecipeIngredient(new Ingredient(5, "cheese"), 120f, Unit.GRAM);

        Recipe recipe1 = new Recipe(1, "cake", 120, 4, 5.0, Arrays.asList(flour, cacao, sugar), "Good cake");
        Recipe recipe2 = new Recipe(2, "cheesecake", 120, 4, 5.0, Arrays.asList(cheese), "Good cheesecake");
        Recipe recipe3 = new Recipe(3, "schnitzel", 120, 4, 5.0, Arrays.asList(flour), "Good schnitzel");
        List<Recipe> recipes = Arrays.asList(recipe1, recipe2, recipe3);

        List<RecipeIngredient> filterIngredients = new ArrayList<>();
        filterIngredients.add(salt);

        List<Recipe> filteredRecipes = RecipeUtils.filterRecipes(recipes, filterIngredients);

        assertThat(filteredRecipes.size(), is(0));
    }

    @Test
    public void filterByIngredientsDifferentReferences() {
        RecipeIngredient flour = new RecipeIngredient(new Ingredient(1, "flour"), 120f, Unit.GRAM);
        RecipeIngredient flour2 = new RecipeIngredient(new Ingredient(2, "flour"), 120f, Unit.GRAM);

        Recipe recipe = new Recipe(1, "cake", 120, 4, 5.0, Arrays.asList(flour), "Good cake");
        List<Recipe> recipes = Arrays.asList(recipe);

        List<RecipeIngredient> filterIngredients = new ArrayList<>();
        filterIngredients.add(flour2);

        List<Recipe> filteredRecipes = RecipeUtils.filterRecipes(recipes, filterIngredients);

        assertThat(filteredRecipes.size(), is(0));
    }

    @Test
    public void filterByFavorites() {
        List<Long> favorites = Arrays.asList(2L, 3L);

        List<Recipe> recipes = Arrays.asList(
                new Recipe(1, "Title1", 30, 4, 3.5, new ArrayList<>(), ""),
                new Recipe(2, "Title2", 50, 4, 4.5, new ArrayList<>(), ""),
                new Recipe(3, "Title3", 10, 4, 5.0, new ArrayList<>(), ""));

        List<Recipe> filteredRecipes = RecipeUtils.filterByFavorites(recipes, favorites);

        assertThat(filteredRecipes.size(), is(favorites.size()));
        for (Recipe recipe : filteredRecipes) {
            assertThat(favorites, hasItem(recipe.getId()));
        }
    }

    @Test
    public void filterByFavoritesEmpty() {
        List<Long> favorites = Arrays.asList();

        List<Recipe> recipes = Arrays.asList(
                new Recipe(1, "Title1", 30, 4, 3.5, new ArrayList<>(), ""),
                new Recipe(2, "Title2", 50, 4, 4.5, new ArrayList<>(), ""),
                new Recipe(3, "Title3", 10, 4, 5.0, new ArrayList<>(), ""));

        List<Recipe> filteredRecipes = RecipeUtils.filterByFavorites(recipes, favorites);

        assertThat(filteredRecipes.size(), is(0));
    }
}