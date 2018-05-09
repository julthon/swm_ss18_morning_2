package at.tugraz.recipro.recipro;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.plumillonforge.android.chipview.ChipView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import at.tugraz.recipro.Views.OurChipView;
import at.tugraz.recipro.Views.OurTagImplementation;

@RunWith(AndroidJUnit4.class)
public class ChipViewTest {
    private OurChipView cv;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        cv = new OurChipView(mActivityRule.getActivity());
        cv.add(new OurTagImplementation(1, "Test Tag", OurTagImplementation.TagType.RECIPE_TYPE));
        cv.add(new OurTagImplementation(2, "Test Tag", OurTagImplementation.TagType.INGREDIENT_INCLUDE));
        cv.add(new OurTagImplementation(3, "Test Tag", OurTagImplementation.TagType.INGREDIENT_EXCLUDE));
        cv.add(new OurTagImplementation(4, "Test Tag", OurTagImplementation.TagType.ALLERGEN));
    }

    @Test
    public void testTagging() {
        Assert.assertEquals(cv.getListOfType(OurTagImplementation.TagType.RECIPE_TYPE).size(), 1);
        Assert.assertEquals(cv.getListOfType(OurTagImplementation.TagType.INGREDIENT_INCLUDE).size(), 1);
        Assert.assertEquals(cv.getListOfType(OurTagImplementation.TagType.INGREDIENT_EXCLUDE).size(), 1);
        Assert.assertEquals(cv.getListOfType(OurTagImplementation.TagType.ALLERGEN).size(), 1);
        Assert.assertEquals(cv.getListOfType(null).size(), 4);
    }

    @Test
    public void testTagRemove() {
        OurTagImplementation tag1 = cv.getListOfType(OurTagImplementation.TagType.RECIPE_TYPE).get(1);
        Assert.assertEquals(cv.getListOfType(OurTagImplementation.TagType.RECIPE_TYPE).size(), 1);
        cv.remove(tag1);
        Assert.assertEquals(cv.getListOfType(OurTagImplementation.TagType.RECIPE_TYPE).size(), 0);
        cv.add(tag1);
        Assert.assertEquals(cv.getListOfType(OurTagImplementation.TagType.RECIPE_TYPE).size(), 1);
    }

    @Test
    public void testRemoveAll() {
        HashMap<Integer, OurTagImplementation> listOfType = cv.getListOfType(null);
        cv.removeAllChips();
        Assert.assertEquals(cv.getListOfType(null).size(), 0);
        for(Map.Entry<Integer, OurTagImplementation> entry : listOfType.entrySet())
            cv.add(entry.getValue());
        Assert.assertEquals(cv.getListOfType(null).size(), listOfType.entrySet().size());
    }
}
