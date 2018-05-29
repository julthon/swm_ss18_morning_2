package at.tugraz.recipro.helper;

import android.content.Context;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.ws.WSConnection;

public abstract class RecipeUtils {
    public static List<Recipe> filterRecipes(List<Recipe> recipes, List<RecipeIngredient> ingredients) {
        Map<Recipe, Long> ingredientMatchings = new HashMap<>();

        recipes.stream().forEach(r -> ingredientMatchings.put(r,
                ingredients.stream().filter(i -> r.getIngredients().contains(i)).count()));

        List<Recipe> sortedRecipes = ingredientMatchings.entrySet().stream()
                .filter(e -> e.getValue() != 0L)
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return sortedRecipes;
    }

    public static List<Recipe> filterByFavorites(List<Recipe> recipes, List<Long> favorites) {
        return recipes.stream()
                .filter(r -> favorites.contains(r.getId()))
                .collect(Collectors.toList());
    }
}
