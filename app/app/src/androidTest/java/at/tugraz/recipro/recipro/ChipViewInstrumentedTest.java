package at.tugraz.recipro.recipro;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tugraz.recipro.views.OurChipView;
import at.tugraz.recipro.views.OurTagImplementation;

@RunWith(AndroidJUnit4.class)
public class ChipViewInstrumentedTest {
    private OurChipView cv;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        cv = new OurChipView(mActivityRule.getActivity());
        cv.add(new OurTagImplementation(1, "Test Tag", OurTagImplementation.TagType.RECIPE_TYPE));
        cv.add(new OurTagImplementation(2, "Test Tag", OurTagImplementation.TagType.INGREDIENT_INCLUDE));
        cv.add(new OurTagImplementation(3, "Test Tag", OurTagImplementation.TagType.INGREDIENT_EXCLUDE));
        cv.add(new OurTagImplementation(4, "Test Tag", OurTagImplementation.TagType.ALLERGEN_EXCLUDE));
    }

    @Test
    public void testTagging() {
        Assert.assertEquals(cv.getListOfType(OurTagImplementation.TagType.RECIPE_TYPE).size(), 1);
        Assert.assertEquals(cv.getListOfType(OurTagImplementation.TagType.INGREDIENT_INCLUDE).size(), 1);
        Assert.assertEquals(cv.getListOfType(OurTagImplementation.TagType.INGREDIENT_EXCLUDE).size(), 1);
        Assert.assertEquals(cv.getListOfType(OurTagImplementation.TagType.ALLERGEN_EXCLUDE).size(), 1);
        Assert.assertEquals(cv.getListOfType(null).size(), 4);
    }
}
