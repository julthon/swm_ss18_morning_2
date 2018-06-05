package at.tugraz.recipro.recipro;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

public class NavigationDrawerInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void navigationDrawerExists() {
        onView(withId(R.id.tbToolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.flContent)).check(matches(isDisplayed()));
    }

    @Test
    public void openAndCloseNavigationDrawer() {
        onView(withId(R.id.nvNavigation)).check(matches(not(isDisplayed())));
        onView(withId(R.id.dlDrawer)).perform(DrawerActions.open());
        onView(withId(R.id.nvNavigation)).check(matches(isDisplayed()));
        onView(withId(R.id.dlDrawer)).perform(DrawerActions.close());
        onView(withId(R.id.nvNavigation)).check(matches(not(isDisplayed())));
    }

    @Test
    public void openAndCloseNavigationDrawerWithButton() {
        onView(withId(R.id.nvNavigation)).check(matches(not(isDisplayed())));
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        onView(withId(R.id.nvNavigation)).check(matches(isDisplayed()));
        onView(withId(R.id.flContent)).perform(click());
        onView(withId(R.id.nvNavigation)).check(matches(not(isDisplayed())));
    }

    @Test
    public void navigationDrawerEntries() {
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        onView(withText(R.string.nav_home)).check(matches(isDisplayed()));
        onView(withText(R.string.nav_pantry)).check(matches(isDisplayed()));
        onView(withText(R.string.nav_grocery_list)).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToHome() {
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        onView(withText(R.string.nav_home)).perform(click());
        onView(withId(R.id.searchbar)).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToGroceryList() {
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        onView(withText(R.string.nav_grocery_list)).perform(click());
        onView(withId(R.id.lvGroceryList)).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToMyPantry() {
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        onView(withText(R.string.nav_pantry)).perform(click());
        onView(withId(R.id.spUnit)).check(matches(isDisplayed()));
    }
}