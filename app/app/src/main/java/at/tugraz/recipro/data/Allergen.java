package at.tugraz.recipro.data;

import com.google.gson.annotations.SerializedName;

public class Allergen {

    @SerializedName("shortName")
    private String shortName;
    @SerializedName("name")
    private String name;

    public Allergen(String shortName, String name) {
        this.shortName = shortName;
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
