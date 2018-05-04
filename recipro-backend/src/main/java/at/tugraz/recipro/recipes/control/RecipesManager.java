/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.recipro.recipes.control;

import at.tugraz.recipro.recipes.entity.Recipe;
import at.tugraz.recipro.recipes.entity.RecipeType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Dominik
 */
@ApplicationScoped
public class RecipesManager {
    
    @PersistenceContext
    EntityManager entityManager;

    public Recipe save(Recipe recipe) {
        return this.entityManager.merge(recipe);
    }

    public List<Recipe> findByTitle(String title) {
        return this.entityManager.createNamedQuery(Recipe.FIND_BY_TITLE, Recipe.class)
                .setParameter("title", "%" + title.toLowerCase() + "%")
                .getResultList();
    }

    public List<Recipe> findAll() {
        return this.entityManager.createNamedQuery(Recipe.FIND_ALL, Recipe.class)
                .getResultList();
    }

    public Recipe findById(long id) {
        return this.entityManager.find(Recipe.class, id);
    }
    
    public List<RecipeType> findAllTypes() {
        return RecipeType.getAllTypes();
    }
}
