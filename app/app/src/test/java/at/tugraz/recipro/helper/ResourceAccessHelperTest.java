package at.tugraz.recipro.helper;

import android.content.ContextWrapper;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ResourceAccessHelperTest {

    @Before
    public void setup() {
        ResourceAccessHelper.setApp(new MockContext());
    }

    @Test
    public void checkSetApp() {
        ResourceAccessHelper.setApp(null);
        Assert.assertNull(ResourceAccessHelper.getAppContext());
        ResourceAccessHelper.setApp(new MockContext());
        Assert.assertNotNull(ResourceAccessHelper.getAppContext());
    }

    @Test
    public void checkAppContext() {
        Assert.assertNotNull(ResourceAccessHelper.getAppContext());
    }

    @Test
    public void checkResources() {
        Assert.assertNotNull(ResourceAccessHelper.getResourcesObject());
    }

    @Test
    public void checkGetString() {
        Assert.assertEquals("yo", ResourceAccessHelper.getStringFromId(0));
    }

    public static class MockRessources extends Resources {

        MockRessources() {
            super(null, null, null);
        }

        @NonNull
        @Override
        public String getString(int id) throws NotFoundException {
            return "yo";
        }
    }

    public static class MockContext extends ContextWrapper {

        public MockContext() {
            super(null);
        }

        @Override
        public Resources getResources() {
            return new MockRessources();
        }
    }
}
