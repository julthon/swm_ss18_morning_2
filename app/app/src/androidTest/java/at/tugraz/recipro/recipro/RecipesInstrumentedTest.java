package at.tugraz.recipro.recipro;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.helper.FavoritesHelper;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class RecipesInstrumentedTest {
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
    public void searchResultListExist() {
        onView(withId(android.R.id.list)).check(matches(isDisplayed()));
    }

    @Test
    public void searchResultListClickOnFirstItem() {
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
        onView(withId(R.id.tvDescTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void searchResultListEntries() {
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
    public void preparationTimeExists() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.ibFilters)).perform(closeSoftKeyboard());
        onView(withId(R.id.tvMinTime)).check(matches(isDisplayed()));
        onView(withId(R.id.tvMaxTime)).check(matches(isDisplayed()));
        onView(withId(R.id.etMinTime)).check(matches(isDisplayed()));
        onView(withId(R.id.etMaxTime)).check(matches(isDisplayed()));
    }

    @Test
    public void recipeTypeExists() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.ibFilters)).perform(closeSoftKeyboard());
        onView(withId(R.id.tvRecipeType)).check(matches(isDisplayed()));
        onView(withId(R.id.spRecipeType)).check(matches(isDisplayed()));
    }

    @Test
    public void testRecipeTypeReturnsSomething() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.ibFilters)).perform(closeSoftKeyboard());
        onView(withId(R.id.spRecipeType)).perform(click());
        onView(withText(R.string.type_dessert)).perform(click());
        onView(withId(R.id.searchbar)).perform(click());
        onView(withId(R.id.searchbar)).perform(pressKey(KeyEvent.KEYCODE_T), pressKey(KeyEvent.KEYCODE_ENTER));
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).onChildView(withId(R.id.tvTitle)).check(matches(isDisplayed()));
    }


    @Test
    public void specificIngredientExists() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.ibFilters)).perform(closeSoftKeyboard());
        onView(withId(R.id.atIngredientExclude)).check(matches(isDisplayed()));
        onView(withId(R.id.atIngredientInclude)).check(matches(isDisplayed()));
    }

    @Test
    public void specificIngredientSuggests() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.ibFilters)).perform(closeSoftKeyboard());
        onView(withId(R.id.atIngredientExclude)).perform(ViewActions.typeText("Flo"));
        onData(instanceOf(Ingredient.class)).inRoot(RootMatchers.isPlatformPopup()).check(matches(withText("Flour"))).check(matches(isDisplayed()));
        onView(withId(R.id.atIngredientInclude)).perform(ViewActions.typeText("Flo"));
        onData(instanceOf(Ingredient.class)).inRoot(RootMatchers.isPlatformPopup()).check(matches(withText("Flour"))).check(matches(isDisplayed()));
    }

    @Test
    public void specificIngredientHandlesTag() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.ibFilters)).perform(closeSoftKeyboard());
        onView(withId(R.id.atIngredientExclude)).perform(ViewActions.typeText("Flo"));
        onData(instanceOf(Ingredient.class)).inRoot(RootMatchers.isPlatformPopup()).check(matches(withText("Flour"))).perform(click());
        onView(withText("Flour")).perform(click());
        onView(withId(R.id.atIngredientExclude)).perform(ViewActions.typeText("Flo"));
        onData(instanceOf(Ingredient.class)).inRoot(RootMatchers.isPlatformPopup()).check(matches(withText("Flour"))).check(matches(isDisplayed()));
    }

    @Test
    public void filterInclude() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.atIngredientInclude)).perform(ViewActions.typeText("Eg"));
        onData(instanceOf(Ingredient.class)).inRoot(RootMatchers.isPlatformPopup()).check(matches(withText("Eggs"))).perform(click());
        onView(withId(R.id.searchbar)).perform(ViewActions.typeText("kuchen"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(android.R.id.list)).check(matches(not(hasDescendant(withText("Bananenkuchen")))));
        onView(withId(android.R.id.list)).check(matches(not(hasDescendant(withText("Allergencake")))));
    }

    @Test
    public void filterExcludeNoResults() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.atIngredientExclude)).perform(ViewActions.typeText("Flo"));
        onData(instanceOf(Ingredient.class)).inRoot(RootMatchers.isPlatformPopup()).check(matches(withText("Flour"))).perform(click());
        onView(withId(R.id.searchbar)).perform(ViewActions.typeText("kuchen"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(android.R.id.list)).check(matches(not(hasDescendant(withText("Bananenkuchen")))));
    }

    @Test
    public void filterExcludeResults() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.atIngredientExclude)).perform(ViewActions.typeText("Eg"));
        onData(instanceOf(Ingredient.class)).inRoot(RootMatchers.isPlatformPopup()).check(matches(withText("Eggs"))).perform(click());
        onView(withId(R.id.searchbar)).perform(ViewActions.typeText("kuchen"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(android.R.id.list)).check(matches(hasDescendant(withText("Bananenkuchen"))));
    }

    @Test
    public void favoritesCheckboxExists() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.ibFilters)).perform(closeSoftKeyboard());
        onView(withId(R.id.tvFilterFavorites)).check(matches(isDisplayed()));
        onView(withId(R.id.cbFavorites)).check(matches(isDisplayed()));
    }

    @Test
    public void filterFavoritesEmpty() {
        FavoritesHelper favoritesHelper = new FavoritesHelper(mActivityRule.getActivity());
        favoritesHelper.onDowngrade(favoritesHelper.getWritableDatabase(), 0, 1);

        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.ibFilters)).perform(closeSoftKeyboard());
        onView(withId(R.id.cbFavorites)).perform(click());
        onView(withId(R.id.cbFavorites)).check(matches(isChecked()));
        onView(withId(R.id.searchbar)).perform(click());
        onView(withId(R.id.searchbar)).perform(ViewActions.typeTextIntoFocusedView("kuchen"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(android.R.id.list)).check(matches(not(hasDescendant(withId(R.id.tvTitle)))));
    }

    @Test
    public void filterFavorites() {
        FavoritesHelper favoritesHelper = new FavoritesHelper(mActivityRule.getActivity());
        favoritesHelper.clear();
        favoritesHelper.addFavorite(1);

        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.ibFilters)).perform(closeSoftKeyboard());
        onView(withId(R.id.cbFavorites)).perform(click());
        onView(withId(R.id.cbFavorites)).check(matches(isChecked()));
        onView(withId(R.id.searchbar)).perform(click());
        onView(withId(R.id.searchbar)).perform(ViewActions.typeTextIntoFocusedView("kuchen"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(android.R.id.list)).check(matches(hasDescendant(withId(R.id.tvTitle))));

        favoritesHelper.clear();
    }

    @Test
    public void favouriteStarToggle() {
        onView(withId(R.id.searchbar)).perform(click());
        onView(withId(R.id.searchbar)).perform(ViewActions.typeTextIntoFocusedView("kuchen"), pressKey(KeyEvent.KEYCODE_ENTER));
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
        onView(withId(R.id.ibFavourite)).perform(click());
        Espresso.pressBack();
        onView(withTagValue(equalTo(R.drawable.ic_star_yellow_24dp))).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
        onView(withId(R.id.ibFavourite)).perform(click());
        Espresso.pressBack();
        onView(withTagValue(equalTo(R.drawable.ic_star_yellow_24dp))).check(doesNotExist());
    }

    @Test
    public void testSearchEvent() {
        onView(withId(R.id.searchbar)).perform(click());
        onView(withId(R.id.searchbar)).perform(ViewActions.typeTextIntoFocusedView("ku"));

        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).onChildView(withId(R.id.tvTitle)).check(matches(withText(containsString("ku"))));
    }
}
