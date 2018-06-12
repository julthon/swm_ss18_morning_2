package at.tugraz.recipro.data;

import java.io.Serializable;

public enum Unit implements Serializable {
    NONE(""),
    MILLILITER("ml"),
    GRAM("g");

    public String shortName;

    Unit(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String toString() {
        return shortName;
    }
}
