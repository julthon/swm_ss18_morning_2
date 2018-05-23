package at.tugraz.recipro.views;

import com.plumillonforge.android.chipview.Chip;

public class OurTagImplementation implements Chip {

    public enum TagType {INGREDIENT_INCLUDE, INGREDIENT_EXCLUDE, RECIPE_TYPE, ALLERGEN_EXCLUDE}

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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof OurTagImplementation) {
            OurTagImplementation objTag = (OurTagImplementation) obj;
            return this.title.equals(objTag.getText()) && this.getTagType() == objTag.getTagType();
        }
        return false;
    }

    public static TagType getEnumFromString(String name) {
        for(TagType tt : TagType.class.getEnumConstants()) {
            if(tt.name().equals(name))
                return tt;
        }
        return null;
    }
}
