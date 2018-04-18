package at.tugraz.recipro.recipro;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SearchResultsActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<SearchResultsActivity> activityActivityTestRule = new ActivityTestRule<SearchResultsActivity>(SearchResultsActivity.class);

    @Before
    public void fillSearchResultList() {
        final SearchResultsActivity activity = activityActivityTestRule.getActivity();
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
        onView(withId(R.id.lvSearchResults)).check(matches(isDisplayed()));
    }

    @Test
    public void testSearchResultListClickOnFirstItem() {
        onData(anything()).inAdapterView(withId(R.id.lvSearchResults)).atPosition(0).perform(click());
    }

    @Test
    public void testSearchResultListEntries() {
        onData(anything()).inAdapterView(withId(R.id.lvSearchResults)).atPosition(0).onChildView(withId(R.id.tvTitle)).check(matches(withText("Recipe #1")));
        onData(anything()).inAdapterView(withId(R.id.lvSearchResults)).atPosition(0).onChildView(withId(R.id.tvTime)).check(matches(withText("20min")));
        onData(anything()).inAdapterView(withId(R.id.lvSearchResults)).atPosition(0).onChildView(withId(R.id.rbRating)).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.lvSearchResults)).atPosition(1).onChildView(withId(R.id.tvTitle)).check(matches(withText("Recipe #2")));
        onData(anything()).inAdapterView(withId(R.id.lvSearchResults)).atPosition(1).onChildView(withId(R.id.tvTime)).check(matches(withText("40min")));
        onData(anything()).inAdapterView(withId(R.id.lvSearchResults)).atPosition(1).onChildView(withId(R.id.rbRating)).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.lvSearchResults)).atPosition(2).onChildView(withId(R.id.tvTitle)).check(matches(withText("Recipe #3")));
        onData(anything()).inAdapterView(withId(R.id.lvSearchResults)).atPosition(2).onChildView(withId(R.id.tvTime)).check(matches(withText("10min")));
        onData(anything()).inAdapterView(withId(R.id.lvSearchResults)).atPosition(2).onChildView(withId(R.id.rbRating)).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.lvSearchResults)).atPosition(3).onChildView(withId(R.id.tvTitle)).check(matches(withText("Recipe #4")));
        onData(anything()).inAdapterView(withId(R.id.lvSearchResults)).atPosition(3).onChildView(withId(R.id.tvTime)).check(matches(withText("30min")));
        onData(anything()).inAdapterView(withId(R.id.lvSearchResults)).atPosition(3).onChildView(withId(R.id.rbRating)).check(matches(isDisplayed()));
    }
}
