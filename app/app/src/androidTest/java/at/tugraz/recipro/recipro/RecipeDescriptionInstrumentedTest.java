package at.tugraz.recipro.recipro;

import android.os.Bundle;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.data.Unit;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class RecipeDescriptionInstrumentedTest {

    private Recipe recipe;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private List<RecipeIngredient> recipeIngredients = null;

    @Before
    public void setUp() throws Exception {
        recipeIngredients = new ArrayList<>();
        recipeIngredients.add(new RecipeIngredient(new Ingredient(1, "Kalbschnitzel"), 4.5f));
        recipeIngredients.add(new RecipeIngredient(new Ingredient(2, "Salz"), 1000f, Unit.GRAM));
        recipeIngredients.add(new RecipeIngredient(new Ingredient(2, "Bier"), 2000f, Unit.MILLILITER));
        recipeIngredients.add(new RecipeIngredient(new Ingredient(2, "Zucker"), 200f, Unit.GRAM));
        recipeIngredients.add(new RecipeIngredient(new Ingredient(2, "Milch"), 150f, Unit.MILLILITER));
        recipeIngredients.add(new RecipeIngredient(new Ingredient(3, "Eier"), 3f));
        recipeIngredients = Collections.unmodifiableList(recipeIngredients);

        String description = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.   \n" +
                "\n" +
                "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.   \n" +
                "\n" +
                "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.   \n" +
                "\n" +
                "Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.   \n" +
                "\n" +
                "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis.   \n" +
                "\n" +
                "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, At accusam aliquyam diam diam dolore dolores duo eirmod eos erat, et nonumy sed tempor et et invidunt justo labore Stet clita ea et gubergren, kasd magna no rebum. sanctus sea sed takimata ut vero voluptua. est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat.   \n" +
                "\n" +
                "Consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus.   \n" +
                "\n" +
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.   \n" +
                "\n" +
                "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.   \n" +
                "\n" +
                "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.   \n" +
                "\n" +
                "Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo";

        this.recipe = new Recipe(1, "Schnitzel", 50, 4, 4.5, recipeIngredients, description);

        Fragment fragmentDescription = new RecipeDescriptionFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable("Recipe", this.recipe);
        fragmentDescription.setArguments(arguments);

        final MainActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(() -> {
            RecipesFragment recipesFragment = (RecipesFragment)activity.getSupportFragmentManager().findFragmentByTag("RecipesFragment");
            if (recipesFragment != null) {
                recipesFragment.addRecipes(Arrays.asList(this.recipe));
            }
        });

        getInstrumentation().waitForIdleSync();

        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
    }

    @Test
    public void exists() {
        onView(withId(R.id.tvDescTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.tvDescTime)).check(matches(isDisplayed()));
        onView(withId(R.id.rbDescRating)).check(matches(isDisplayed()));
        onView(withId(R.id.lvIngredients)).check(matches(isDisplayed()));
        onView(withId(R.id.tvDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.ivGroup)).check(matches(isDisplayed()));
        onView(withId(R.id.etServings)).check(matches(isDisplayed()));
        onView(withId(R.id.ibFavourite)).check(matches(isDisplayed()));
    }

    private void checkIngredients(int position, String ingredient, String quantity, String unit) {
        onData(anything()).inAdapterView(withId(R.id.lvIngredients)).atPosition(position).onChildView(withId(R.id.tvQuantity)).check(matches(withText(quantity)));
        onData(anything()).inAdapterView(withId(R.id.lvIngredients)).atPosition(position).onChildView(withId(R.id.tvUnit)).check(matches(withText(unit)));
        onData(anything()).inAdapterView(withId(R.id.lvIngredients)).atPosition(position).onChildView(withId(R.id.tvIngredient)).check(matches(withText(ingredient)));
    }

    @Test
    public void recipeContent() {
        onView(withText(this.recipe.getTitle())).check(matches(isDisplayed()));
        onView(withText(String.valueOf(this.recipe.getTime()) + "min")).check(matches(isDisplayed()));
        onView(withText(this.recipe.getDescription())).check(matches(isDisplayed()));
        onView(withText(String.valueOf(this.recipe.getServings()))).check(matches(isDisplayed()));

        checkIngredients(0, "Kalbschnitzel", "4.5", "");
        checkIngredients(1, "Salz", "1", "kg");
        checkIngredients(2, "Bier", "2", "l");
        checkIngredients(3, "Zucker", "200", "g");
        checkIngredients(4, "Milch", "150", "ml");
        checkIngredients(5, "Eier", "3", "");
    }

    @Test
    public void scrollability() {
        onView(withText(R.string.ingredients_header)).perform(swipeUp());
        int scrollY = mActivityRule.getActivity().findViewById(R.id.scrollView).getScrollY();
        Assert.assertNotEquals(0, scrollY);
    }

    @Test
    public void favouriteButtonDefault() {
        onView(withTagValue(equalTo(R.drawable.ic_star_border_black_24dp))).check(matches(isDisplayed()));
    }

    @Test
    public void favouriteButtonWorking() {
        onView(withId(R.id.ibFavourite)).perform(click());
        onView(withTagValue(equalTo(R.drawable.ic_star_yellow_24dp))).check(matches(isDisplayed()));
        onView(withId(R.id.ibFavourite)).perform(click());
        onView(withTagValue(equalTo(R.drawable.ic_star_border_black_24dp))).check(matches(isDisplayed()));
    }

    @Test
    public void servingsHalf() {
        onView(withId(R.id.etServings)).perform(click());
        onView(withId(R.id.etServings)).perform(replaceText("2"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.etServings)).perform(closeSoftKeyboard());

        checkIngredients(0, "Kalbschnitzel", "2.25", "");
        checkIngredients(1, "Salz", "500", "g");
        checkIngredients(2, "Bier", "1", "l");
        checkIngredients(3, "Zucker", "100", "g");
        checkIngredients(4, "Milch", "75", "ml");
        checkIngredients(5, "Eier", "1.5", "");
    }

    @Test
    public void servingsZero() {
        onView(withId(R.id.etServings)).perform(click());
        onView(withId(R.id.etServings)).perform(replaceText("0"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.etServings)).perform(closeSoftKeyboard());

        checkIngredients(0, "Kalbschnitzel", "4.5", "");
        checkIngredients(1, "Salz", "1", "kg");
        checkIngredients(2, "Bier", "2", "l");
        checkIngredients(3, "Zucker", "200", "g");
        checkIngredients(4, "Milch", "150", "ml");
        checkIngredients(5, "Eier", "3", "");
    }

    @Test
    public void servingsTooHigh() {
        onView(withId(R.id.etServings)).perform(click());
        onView(withId(R.id.etServings)).perform(replaceText("2500"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.etServings)).perform(closeSoftKeyboard());

        checkIngredients(0, "Kalbschnitzel", "4.5", "");
        checkIngredients(1, "Salz", "1", "kg");
        checkIngredients(2, "Bier", "2", "l");
        checkIngredients(3, "Zucker", "200", "g");
        checkIngredients(4, "Milch", "150", "ml");
        checkIngredients(5, "Eier", "3", "");
    }

    @Test
    public void addRecipeToGroceryList() {
        onView(withId(R.id.ibGroceryList)).perform(click());
        onView(withId(R.id.dlDrawer)).perform(DrawerActions.open());
        onView(withText("Grocery List")).perform(click());
        recipeIngredients.forEach(ri -> {
            onView(withText(ri.getIngredient().getName())).check(matches(isDisplayed()));
        });

    }
}
