package at.tugraz.recipro.helper;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class FavoritesHelperTest {
    private FavoritesHelper helper;

    @Before
    public void setUp() throws Exception {
        this.helper = new FavoritesHelper(RuntimeEnvironment.application);
        this.helper.onDowngrade(this.helper.getWritableDatabase(), 0, 1);
    }

    @After
    public void tearDown() {
        this.helper.onDowngrade(this.helper.getWritableDatabase(), 0, 1);
    }

    @Test
    public void insertFavorites() {
        List<Long> favorites = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L));

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
        List<Long> favorites = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L));

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