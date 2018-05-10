package at.tugraz.recipro.recipro;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class SearchInstrumentedTest {

    @Rule
    public ActivityTestRule<RecipesActivity> mActivityRule = new ActivityTestRule<>(RecipesActivity.class);

    @Test
    public void searchBarAvailable() {
        onView(withHint(R.string.search_hint)).perform(click());
    }

    @Test
    public void searchSubmitSearch() {
        onView(withHint(R.string.search_hint)).perform(click());
        onView(withHint(R.string.search_hint)).perform(ViewActions.typeTextIntoFocusedView("kuchen"), pressKey(KeyEvent.KEYCODE_ENTER));

        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).onChildView(withId(R.id.tvTitle)).check(matches(isDisplayed()));
    }

    @Before
    public void fillSearchResultList() {
        final RecipesActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                activity.fillWithTestData();
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    @Test
    public void testSearchResultListExist() {
        onView(withId(android.R.id.list)).check(matches(isDisplayed()));
    }

    @Test
    public void testSearchResultListClickOnFirstItem() {
        Intents.init();
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
        intended(hasComponent(RecipeDescriptionActivity.class.getName()));
        Intents.release();
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
    public void testRecipeTypeReturnsSomething() {
        onView(withId(R.id.ibFilters)).perform(click());
        onView(withId(R.id.spRecipeType)).perform(click());
        onView(withText(R.string.type_dessert)).perform(click());
        onView(withHint(R.string.search_hint)).perform(click());
        onView(withHint(R.string.search_hint)).perform(pressKey(KeyEvent.KEYCODE_T), pressKey(KeyEvent.KEYCODE_ENTER));
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).onChildView(withId(R.id.tvTitle)).check(matches(isDisplayed()));
    }
}
