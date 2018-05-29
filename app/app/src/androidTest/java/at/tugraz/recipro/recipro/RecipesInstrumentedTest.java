package at.tugraz.recipro.recipro;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;
import android.view.WindowManager;

import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tugraz.recipro.data.Ingredient;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class RecipesInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void fillSearchResultList() {
        final MainActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(() -> {
            RecipesFragment recipesFragment = (RecipesFragment)activity.getSupportFragmentManager().findFragmentByTag("RecipesFragment");
            if (recipesFragment != null) {
                recipesFragment.fillWithTestData();
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    @Test
    public void searchBarAvailable() {
        onView(withId(R.id.searchbar)).perform(click());
    }

    @Test
    public void searchSubmitSearch() {
        onView(withId(R.id.searchbar)).perform(click());
        onView(withId(R.id.searchbar)).perform(ViewActions.typeTextIntoFocusedView("kuchen"), pressKey(KeyEvent.KEYCODE_ENTER));

        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).onChildView(withId(R.id.tvTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void testSearchResultListExist() {
        onView(withId(android.R.id.list)).check(matches(isDisplayed()));
    }

    @Test
    public void testSearchResultListClickOnFirstItem() {
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
        onView(withId(R.id.tvDescTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void testSearchResultListEntries() {
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).onChildView(withId(R.id.ivThumbnail)).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).onChildView(withId(R.id.tvTitle)).check(matches(withText("Recipe #1")));
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).onChildView(withId(R.id.tvTime)).check(matches(withText("20min")));
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).onChildView(withId(R.id.rbRating)).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(1).onChildView(withId(R.id.ivThumbnail)).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(1).onChildView(withId(R.id.tvTitle)).check(matches(withText("Recipe #2")));
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(1).onChildView(withId(R.id.tvTime)).check(matches(withText("40min")));
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(1).onChildView(withId(R.id.rbRating)).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(2).onChildView(withId(R.id.ivThumbnail)).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(2).onChildView(withId(R.id.tvTitle)).check(matches(withText("Recipe #3")));
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(2).onChildView(withId(R.id.tvTime)).check(matches(withText("10min")));
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(2).onChildView(withId(R.id.rbRating)).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(3).onChildView(withId(R.id.ivThumbnail)).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(3).onChildView(withId(R.id.tvTitle)).check(matches(withText("Recipe #4")));
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(3).onChildView(withId(R.id.tvTime)).check(matches(withText("30min")));
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(3).onChildView(withId(R.id.rbRating)).check(matches(isDisplayed()));
    }

    @Test
    public void testPreparationTimeExists() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.tvMinTime)).check(matches(isDisplayed()));
        onView(withId(R.id.tvMaxTime)).check(matches(isDisplayed()));
        onView(withId(R.id.etMinTime)).check(matches(isDisplayed()));
        onView(withId(R.id.etMaxTime)).check(matches(isDisplayed()));
    }

    @Test
    public void testRecipeTypeExists() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.tvRecipeType)).check(matches(isDisplayed()));
        onView(withId(R.id.spRecipeType)).check(matches(isDisplayed()));
    }

    @Test
    public void testFavoritesCheckbarExists() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.ibFilters)).perform(closeSoftKeyboard());
        onView(withId(R.id.tvFilterFavorites)).check(matches(isDisplayed()));
        onView(withId(R.id.cbFavorites)).check(matches(isDisplayed()));
    }

    @Test
    public void testRecipeTypeReturnsSomething() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.spRecipeType)).perform(click());
        onView(withText(R.string.type_dessert)).perform(click());
        onView(withId(R.id.searchbar)).perform(click());
        onView(withId(R.id.searchbar)).perform(pressKey(KeyEvent.KEYCODE_T), pressKey(KeyEvent.KEYCODE_ENTER));
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).onChildView(withId(R.id.tvTitle)).check(matches(isDisplayed()));
    }


    @Test
    public void testSpecificIngredientExists() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.atIngredientExclude)).check(matches(isDisplayed()));
        onView(withId(R.id.atIngredientInclude)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpecificIngredientSuggests() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.atIngredientExclude)).perform(ViewActions.typeText("Flo"));
        onData(instanceOf(Ingredient.class)).inRoot(RootMatchers.isPlatformPopup()).check(matches(withText("Flour"))).check(matches(isDisplayed()));
        onView(withId(R.id.atIngredientInclude)).perform(ViewActions.typeText("Flo"));
        onData(instanceOf(Ingredient.class)).inRoot(RootMatchers.isPlatformPopup()).check(matches(withText("Flour"))).check(matches(isDisplayed()));
    }

    @Test
    public void testSpecificIngredientHandlesTag() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.atIngredientExclude)).perform(ViewActions.typeText("Flo"));
        onData(instanceOf(Ingredient.class)).inRoot(RootMatchers.isPlatformPopup()).check(matches(withText("Flour"))).perform(click());
        onView(withText("Flour")).perform(click());
        onView(withId(R.id.atIngredientExclude)).perform(ViewActions.typeText("Flo"));
        onData(instanceOf(Ingredient.class)).inRoot(RootMatchers.isPlatformPopup()).check(matches(withText("Flour"))).check(matches(isDisplayed()));
    }

}