package at.tugraz.recipro.data;

import java.io.Serializable;

public class Ingredient implements Serializable{
    private String name;

    public Ingredient(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
