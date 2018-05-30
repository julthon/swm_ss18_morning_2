package at.tugraz.recipro.data;

public enum Unit {
    NONE(""),
    MILLILITER("ml"),
    GRAM("g");

    public String shortName;

    Unit(String shortName) {
        this.shortName = shortName;
    }

}
