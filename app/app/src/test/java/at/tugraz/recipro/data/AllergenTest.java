package at.tugraz.recipro.data;

import org.junit.Test;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AllergenTest {

    @Test
    public void testAllergenGetter() {
        String shortName = "allergenShortName";
        String name = "allergenName";
        String description = "allergenDescription";
        Allergen allergen = new Allergen(shortName, name, description);

        assertThat(allergen.getShortName(), is(shortName));
        assertThat(allergen.getName(), is(name));
        assertThat(allergen.getDescription(), is(description));
    }

    @Test
    public void testAllergenSetter() {
        Allergen allergen = new Allergen("random", "random", "random");
        String shortName = "newShortName";
        String name = "newName";
        String description = "newDescription";

        allergen.setName(name);
        allergen.setShortName(shortName);
        allergen.setDescription(description);

        assertThat(allergen.getShortName(), is(shortName));
        assertThat(allergen.getName(), is(name));
        assertThat(allergen.getDescription(), is(description));
    }

    @Test
    public void testAllergenToString() {
        String allergenName = "allergenName";
        Allergen allergen = new Allergen("allergenShortName", allergenName, "allergenDescription");
        assertThat(allergen.toString(), is(allergenName));
    }

    @Test
    public void testAllergenEqual() {
        String shortName1 = "shortName1";
        Allergen allergen1 = new Allergen("shortName1", shortName1, "description1");
        Allergen allergen2 = new Allergen("shortName2", "shortName2", "description2");

        assertThat(allergen1.hashCode(), is(Objects.hash(shortName1)));
        assertThat(allergen1.equals(allergen1), is(true));
        assertThat(allergen1.equals(null), is(false));
        assertThat(allergen1.equals(allergen2), is(false));
    }
}