package ua.yurezcv.popularmovies.movies;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ua.yurezcv.popularmovies.R;

public class MoviesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int fragmentLayoutId = R.id.movies_fragment_container;

        MoviesFragment moviesFragment =
                (MoviesFragment) getSupportFragmentManager().findFragmentById(fragmentLayoutId);

        // check if fragment exists, init otherwise
        if (moviesFragment == null) {
            // init the movies fragment with 2 columns
            moviesFragment = MoviesFragment.newInstance(2);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(fragmentLayoutId, moviesFragment);
            transaction.commit();
        }
    }
}
