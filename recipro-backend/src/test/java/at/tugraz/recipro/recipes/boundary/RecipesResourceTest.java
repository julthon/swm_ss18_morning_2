/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.recipro.recipes.boundary;

import at.tugraz.recipro.recipes.control.RecipesManager;
import at.tugraz.recipro.recipes.entity.Recipe;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Dominik
 */
public class RecipesResourceTest {
    
    RecipesResource recipesResource;
    
    @Before
    public void init() {
        this.recipesResource = new RecipesResource();
        this.recipesResource.recipesManager = mock(RecipesManager.class);
    }
    
    @Test
    public void createRecipe() {
        Recipe recipe = new Recipe("Testkuchen", 120);
        //recipesResource.create(recipe, null);
        //verify(this.recipesResource.recipesManager, times(1)).save(recipe);
    }
    
    @Test
    public void findAllRecipes() {
        when(this.recipesResource.recipesManager.findAll()).thenReturn(new ArrayList<>());
        recipesResource.findAll();
        
    }
    
}
