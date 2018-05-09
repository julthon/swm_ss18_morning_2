package at.tugraz.recipro.data;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RecipeTest {
    private Recipe recipe;
    private List<RecipeIngredient> recipeIngredients;

    private final String TITLE = "TestRecipe";
    private final String DESC = "Do something";
    private final String INGREDIENT1_NAME = "Potatoes";
    private final String INGREDIENT1_QUANTITY = "2";
    private final String INGREDIENT2_NAME = "Eggs";
    private final String INGREDIENT2_QUANTITY = "3";
    private final int TIME = 50;
    private final double RATING = 4.5;

    @Before
    public void setUp() throws Exception {
        this.recipeIngredients = new ArrayList<>();
        this.recipeIngredients.add(new RecipeIngredient(new Ingredient(INGREDIENT1_NAME), INGREDIENT1_QUANTITY));
        this.recipeIngredients.add(new RecipeIngredient(new Ingredient(INGREDIENT2_NAME), INGREDIENT2_QUANTITY));

        this.recipe = new Recipe(1, TITLE, TIME, RATING, this.recipeIngredients, DESC);
    }

    @Test
    public void testRecipeGetters() {
        assertEquals(this.recipe.getTitle(), TITLE);
        assertEquals(this.recipe.getTime(), TIME);
        assertEquals(this.recipe.getRating(), RATING, 0.05);
        assertEquals(this.recipe.getIngredients().size(), this.recipeIngredients.size());
        assertEquals(this.recipe.getDescription(), DESC);
    }

    @Test
    public void testIngredientGetters() {
        List<RecipeIngredient> ingredients = this.recipe.getIngredients();

        assertEquals(ingredients.size(), 2);
        assertEquals(ingredients.get(0).getIngredient().getName(), INGREDIENT1_NAME);
        assertEquals(ingredients.get(0).getQuantity(), INGREDIENT1_QUANTITY);
        assertEquals(ingredients.get(1).getIngredient().getName(), INGREDIENT2_NAME);
        assertEquals(ingredients.get(1).getQuantity(), INGREDIENT2_QUANTITY);
    }

    @Test
    public void testRecipeSetters() {
        String title = "New Title";
        this.recipe.setTitle(title);
        assertEquals(this.recipe.getTitle(), title);

        int time = 25;
        this.recipe.setTime(time);
        assertEquals(this.recipe.getTime(), time);

        double rating = 3.0;
        this.recipe.setRating(rating);
        assertEquals(this.recipe.getRating(), rating, 0.05);

        String description = "Do something else";
        this.recipe.setDescription(description);
        assertEquals(this.recipe.getDescription(), description);

        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        recipeIngredients.add(new RecipeIngredient(new Ingredient(INGREDIENT1_NAME), INGREDIENT1_QUANTITY));
        this.recipe.setIngredients(recipeIngredients);
        assertEquals(this.recipe.getIngredients().size(), recipeIngredients.size());
    }

    @Test
    public void testIngredientSetters() {
        String ingredientName = "Rice";
        String quantity = "1 cup";

        List<RecipeIngredient> recipeIngredients = this.recipe.getIngredients();
        RecipeIngredient firstIngredient = recipeIngredients.get(0);
        firstIngredient.getIngredient().setName(ingredientName);
        firstIngredient.setQuantity(quantity);

        Ingredient secondIngredient = new Ingredient("Apples");
        recipeIngredients.get(1).setIngredient(secondIngredient);

        List<RecipeIngredient> ingredients = this.recipe.getIngredients();
        assertEquals(ingredients.size(), 2);
        assertEquals(ingredients.get(0).getIngredient().getName(), ingredientName);
        assertEquals(ingredients.get(0).getQuantity(), quantity);
        assertEquals(ingredients.get(1).getIngredient().getName(), secondIngredient.getName());
        assertEquals(ingredients.get(1).getQuantity(), INGREDIENT2_QUANTITY);
    }
}