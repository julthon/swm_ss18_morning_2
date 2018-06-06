package at.tugraz.recipro.recipro;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.data.Unit;
import at.tugraz.recipro.helper.MyPantryListHelper;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4.class)
public class MyPantryInstrumentedTest {
    private MyPantryListHelper helper;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void fillSearchResultList() {
        final MainActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(() -> {
            RecipesFragment recipesFragment = (RecipesFragment) activity.getSupportFragmentManager().findFragmentByTag("RecipesFragment");
            if (recipesFragment != null) {
                recipesFragment.fillWithTestData();
            }
        });

        this.helper = new MyPantryListHelper(mActivityRule.getActivity());
        this.helper.clear();
        this.helper.addIngredient(new RecipeIngredient(new Ingredient(1, "Predefined 1"), 100, Unit.GRAM));
        this.helper.addIngredient(new RecipeIngredient(new Ingredient(2, "Predefined 2"), 200, Unit.GRAM));

        getInstrumentation().waitForIdleSync();
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        onView(withText(R.string.nav_pantry)).perform(click());
        onView(withId(R.id.spUnit)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        this.helper.clear();
    }

    @Test
    public void insertIngredient() {
        Assert.assertEquals(2, this.helper.getIngredients().size());

        onView(withId(R.id.tvAutoCompleteIngredients)).perform(ViewActions.typeText("Flour"));
        onData(instanceOf(Ingredient.class)).inRoot(RootMatchers.isPlatformPopup()).check(matches(withText("Flour"))).perform(click());
        onView(withText("Flour")).perform(click());

        onView(withId(R.id.npQuantity)).perform(click());
        onView(withId(R.id.npQuantity)).perform(ViewActions.typeTextIntoFocusedView("100"), pressKey(KeyEvent.KEYCODE_ENTER));

        onView(withId(R.id.spUnit)).perform(click());
        onView(withText("g")).perform(click());

        onView(withId(R.id.btAddIngredient)).perform(click());

        Assert.assertEquals(3, this.helper.getIngredients().size());
    }

    @Test
    public void removeIngredients() {
        Assert.assertEquals(2, this.helper.getIngredients().size());
        onView(withText("Predefined 1")).perform(click());
        Assert.assertEquals(1, this.helper.getIngredients().size());
        onView(withText("Predefined 2")).perform(click());
        Assert.assertEquals(0, this.helper.getIngredients().size());
    }
}
