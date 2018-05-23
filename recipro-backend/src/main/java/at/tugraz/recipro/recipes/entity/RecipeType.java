package at.tugraz.recipro.recipes.entity;

/**
 *
 * @author Dominik, Julian
 */
public enum RecipeType {    
    DESSERT("DESSERT"),
    SNACK("SNACK"),
    MAIN_COURSE("MAIN_COURSE");

    private String name;
    
    RecipeType(String name) {
        this.name = name;
    }

    public static RecipeType fromString(String type) {
    for (RecipeType t : RecipeType.values())
        if (t.name.equalsIgnoreCase(type))
          return t;
    return null;
    }
}