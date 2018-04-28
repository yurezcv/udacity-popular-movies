package ua.yurezcv.popularmovies.movies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import ua.yurezcv.popularmovies.BuildConfig;
import ua.yurezcv.popularmovies.R;
import ua.yurezcv.popularmovies.data.model.Movie;

public class MoviesActivity extends AppCompatActivity implements MoviesContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String apiKey = BuildConfig.API_KEY;

        Log.d("API_KEY", apiKey);
    }

    @Override
    public void setProgressIndicator(boolean active) {

    }

    @Override
    public void showMovies(List<Movie> movies) {

    }
}
