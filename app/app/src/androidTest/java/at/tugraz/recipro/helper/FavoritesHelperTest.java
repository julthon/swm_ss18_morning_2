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

import at.tugraz.recipro.recipro.MainActivity;

public class FavoritesHelperTest {
    private FavoritesHelper favoritesHelper;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        favoritesHelper = new FavoritesHelper(mActivityRule.getActivity());
    }

    @After
    public void tearDown() throws Exception {
        favoritesHelper.onDowngrade(favoritesHelper.getWritableDatabase(), 0, 1);
    }

    @Test
    public void insertFavorites() {
        List<Long> favorites = new ArrayList<Long>(Arrays.asList(1L, 2L, 3L, 4L));

        for (Long favorite : favorites) {
            favoritesHelper.addFavorite(favorite);
        }

        Assert.assertEquals(favoritesHelper.getFavorites().size(), favorites.size());

        for (Long favorite : favorites) {
            Assert.assertEquals(favoritesHelper.exists(favorite), true);
        }
    }

    @Test
    public void removeFavorites() {
        List<Long> favorites = new ArrayList<Long>(Arrays.asList(1L, 2L, 3L, 4L));

        for (Long favorite : favorites) {
            favoritesHelper.addFavorite(favorite);
        }

        Assert.assertEquals(favoritesHelper.getFavorites().size(), favorites.size());

        Long removedId = favorites.remove(2);
        favoritesHelper.removeFavorite(removedId);

        Assert.assertEquals(favoritesHelper.getFavorites().size(), favorites.size());

        for (Long favorite : favorites) {
            Assert.assertEquals(favoritesHelper.exists(favorite), true);
        }
    }
}