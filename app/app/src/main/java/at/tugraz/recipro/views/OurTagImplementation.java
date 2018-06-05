package at.tugraz.recipro.views;

import com.plumillonforge.android.chipview.Chip;

public class OurTagImplementation<T> implements Chip {

    public enum TagType {INGREDIENT_INCLUDE, INGREDIENT_EXCLUDE, RECIPE_TYPE, ALLERGEN_EXCLUDE}

    private T value;
    private String text;
    private TagType tagType;

    public OurTagImplementation(T value, String text, TagType tagType) {
        this.value = value;
        this.text = text;
        this.tagType = tagType;
    }

    @Override
    public String getText() {
        return text;
    }

    public TagType getTagType() {
        return this.tagType;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OurTagImplementation) {
            OurTagImplementation objTag = (OurTagImplementation) obj;
            return this.text.equals(objTag.getText()) && this.getTagType() == objTag.getTagType();
        }
        return false;
    }

    public static TagType getEnumFromString(String name) {
        for (TagType tt : TagType.class.getEnumConstants()) {
            if (tt.name().equals(name))
                return tt;
        }
        return null;
    }
}
