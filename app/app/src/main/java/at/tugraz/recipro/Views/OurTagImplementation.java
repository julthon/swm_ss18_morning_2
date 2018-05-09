package at.tugraz.recipro.Views;

import com.plumillonforge.android.chipview.Chip;

public class OurTagImplementation implements Chip {

    public enum TagType {INGREDIENT_INCLUDE, INGREDIENT_EXCLUDE, RECIPE_TYPE, ALLERGEN}

    private int id;
    private String title;
    private TagType tagType;

    public OurTagImplementation(int id, String title, TagType tagType) {
        this.id = id;
        this.title = title;
        this.tagType = tagType;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getText() {
        return title;
    }

    public TagType getTagType() {
        return this.tagType;
    }


}
