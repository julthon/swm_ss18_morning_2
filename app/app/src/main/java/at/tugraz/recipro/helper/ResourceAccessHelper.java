package at.tugraz.recipro.helper;

import android.content.Context;
import android.content.res.Resources;

public class ResourceAccessHelper {
    private static Context appContext;

    public static void setApp(Context app) {
        appContext = app;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static Resources getResourcesObject() {
        return appContext.getResources();
    }

    public static String getStringFromId(int id) {
        return appContext.getResources().getString(id);
    }
}
