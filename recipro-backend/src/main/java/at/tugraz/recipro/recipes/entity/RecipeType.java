package at.tugraz.recipro.recipes.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dominik, Julian
 */
public class RecipeType {
    public static final RecipeType DESSERT = new RecipeType("DESSERT", "Dessert");
    public static final RecipeType SNACK = new RecipeType("SNACK", "Snack");
    public static final RecipeType MAIN_COURSE = new RecipeType("MAIN_COURSE", "Main Course");

    private String identifier;
    private String name;
    
    RecipeType(String identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public static RecipeType fromString(String identifier) {
        try {
            for (Field field : RecipeType.class.getDeclaredFields()) {
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    RecipeType type = (RecipeType) field.get(null);
                    if(type.getIdentifier().equalsIgnoreCase(identifier))
                        return type;
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public static List<RecipeType> getAllTypes() {
        ArrayList<RecipeType> types = new ArrayList<>();
        try {
            for (Field field : RecipeType.class.getDeclaredFields())
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                    types.add((RecipeType) field.get(null));
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return types;
    }
    
    public static String getNameFromEnum(RecipeType rt) {
        return rt.name;
    }
    
    public static String getIdentifierFromEnum(RecipeType rt) {
        return rt.identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }
}
