package at.tugraz.recipro.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jeremias on 09.05.18.
 */

public class RecipeIngredientTest {

        private RecipeIngredient recipeIngredient;
        private Ingredient ingredient;
        private String quantity = "siebzehn";


        @Before
        public void setUp() throws Exception {
            this.ingredient = new Ingredient("Milk");
            recipeIngredient = new RecipeIngredient(ingredient, quantity);
        }

        @Test
        public void testRecipeIngredientGetters() {
            assertEquals(recipeIngredient.getIngredient().getName(),"Milk");
            assertEquals(recipeIngredient.getQuantity(), quantity);
        }

        @Test
        public void testRecipeIngredientSetters() {
            Ingredient newIngredient = new Ingredient("Eier");
            recipeIngredient.setIngredient(newIngredient);
            recipeIngredient.setQuantity("400");
            assertEquals(recipeIngredient.getIngredient().getName(),"Eier");
            assertEquals(recipeIngredient.getQuantity(), "400");
        }

    }
