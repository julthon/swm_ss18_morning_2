package at.tugraz.recipro.helper;

import android.support.test.rule.ActivityTestRule;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.tugraz.recipro.helper.FavoritesHelper;
import at.tugraz.recipro.recipro.MainActivity;

public class FavoritesHelperTest {
    private FavoritesHelper helper;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        this.helper = new FavoritesHelper(mActivityRule.getActivity());
        this.helper.clear();
    }

    @After
    public void tearDown() throws Exception {
        this.helper.clear();
    }

    @Test
    public void insertFavorites() {
        List<Long> favorites = new ArrayList<Long>(Arrays.asList(1L, 2L, 3L, 4L));

        for (Long favorite : favorites) {
            this.helper.addFavorite(favorite);
        }

        Assert.assertEquals(this.helper.getFavorites().size(), favorites.size());

        for (Long favorite : favorites) {
            Assert.assertEquals(this.helper.exists(favorite), true);
        }
    }

    @Test
    public void removeFavorites() {
        List<Long> favorites = new ArrayList<Long>(Arrays.asList(1L, 2L, 3L, 4L));

        for (Long favorite : favorites) {
            this.helper.addFavorite(favorite);
        }

        Assert.assertEquals(this.helper.getFavorites().size(), favorites.size());

        Long removedId = favorites.remove(2);
        this.helper.removeFavorite(removedId);

        Assert.assertEquals(this.helper.getFavorites().size(), favorites.size());

        for (Long favorite : favorites) {
            Assert.assertEquals(this.helper.exists(favorite), true);
        }
    }
}