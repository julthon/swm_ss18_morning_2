/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.recipro;

import at.tugraz.recipro.recipes.control.RecipeManager;
import at.tugraz.recipro.recipes.entity.Recipe;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.inject.Inject;
import static org.hamcrest.CoreMatchers.is;

/**
 *
 * @author Edith
 */
public class RecipeTest {
    
    @Inject
    RecipeManager recipeManager;
    
    
    public RecipeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void createAndFindRecipe(){
        String title = "grandmas best cake";
        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipeManager.createRecipe(recipe);
        Recipe result = recipeManager.findByTitle(title);
        assertThat(result.getTitle(), is(recipe.getTitle()));
    }
}
