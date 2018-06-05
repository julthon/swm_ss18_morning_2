package at.tugraz.recipro.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AllergenTest {

    private Allergen allergen;

    private final String NAME = "Lupin";
    private final String SHORT_NAME = "L";

    @Before
    public void setUpAllergen() {
        this.allergen = new Allergen("Test", "Test", "Test");
    }

    @Test
    public void testAllergenesGetterStuff() {
        String allergene_name = this.allergen.getName();
        String allergene_shortname = this.allergen.getShortName();

        assertEquals("Test", allergene_name);
        assertEquals("Test", allergene_shortname);
    }

    @Test
    public void testAllergenesSetterStuff() {
        this.allergen.setName(NAME);
        this.allergen.setShortName(SHORT_NAME);

        String allergene_name = this.allergen.getName();
        String allergene_shortname = this.allergen.getShortName();

        assertEquals("Lupin", allergene_name);
        assertEquals("L", allergene_shortname);
    }

    @Test
    public void testAllergenesToStringStuff() {
        assertEquals("Test", allergen.toString());
    }
}