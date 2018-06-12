package at.tugraz.recipro.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Allergen implements Serializable {
    @SerializedName("shortName")
    private String shortName;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    public Allergen(String shortName, String name, String description) {
        this.shortName = shortName;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return Objects.equals(shortName, ((Allergen) o).shortName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortName);
    }

    @Override
    public String toString() {
        return name;
    }
}
