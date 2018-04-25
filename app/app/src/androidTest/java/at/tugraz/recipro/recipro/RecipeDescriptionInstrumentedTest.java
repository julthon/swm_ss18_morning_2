package at.tugraz.recipro.recipro;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.helper.ResourceAccessHelper;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class RecipeDescriptionInstrumentedTest {

    private Recipe recipe;

    @Rule
    public ActivityTestRule<RecipeDescriptionActivity> mActivityRule = new ActivityTestRule<RecipeDescriptionActivity>(RecipeDescriptionActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            Intent result = new Intent(targetContext, RecipeDescriptionActivity.class);

            ArrayList<RecipeIngredient> recipeIngredients = new ArrayList<>();
            recipeIngredients.add(new RecipeIngredient(new Ingredient("Kalbschnitzel"), "4"));
            recipeIngredients.add(new RecipeIngredient(new Ingredient("Salz"), "eine Prise"));
            recipeIngredients.add(new RecipeIngredient(new Ingredient("Eier"), "3"));

            recipe = new Recipe("Schnitzel", 50, 4.5, recipeIngredients, "Für das Wiener Schnitzel...");
            result.putExtra(ResourceAccessHelper.getStringFromId(R.string.recipe), recipe);
            return result;
        }
    };

    @Test
    public void checkExists() {
        onView(withId(R.id.tvTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.tvTime)).check(matches(isDisplayed()));
        onView(withId(R.id.rbRating)).check(matches(isDisplayed()));
        onView(withId(R.id.lvIngredients)).check(matches(isDisplayed()));
        onView(withId(R.id.tvDescription)).check(matches(isDisplayed()));
    }

    @Test
    public void checkContent() {
        onView(withText(recipe.getTitle())).check(matches(isDisplayed()));
        onView(withText(String.valueOf(recipe.getTime()))).check(matches(isDisplayed()));
        onView(withText(recipe.getDescription())).check(matches(isDisplayed()));

        List<RecipeIngredient> ingredients = recipe.getIngredients();
        for (int position = 0; position < ingredients.size(); position++) {
            RecipeIngredient ingredient = ingredients.get(position);
            onData(anything()).inAdapterView(withId(R.id.lvIngredients)).atPosition(position).onChildView(withId(R.id.tvQuantity)).check(matches(withText(ingredient.getQuantity())));
            onData(anything()).inAdapterView(withId(R.id.lvIngredients)).atPosition(position).onChildView(withId(R.id.tvIngredient)).check(matches(withText(ingredient.getIngredient().getName())));
        }
    }
}
