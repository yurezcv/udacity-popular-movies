package ua.yurezcv.popularmovies;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class PopularMoviesApp extends Application {

    // Single instance Application object
    private static PopularMoviesApp sInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static PopularMoviesApp getInstance() {
        return sInstance;
    }

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
