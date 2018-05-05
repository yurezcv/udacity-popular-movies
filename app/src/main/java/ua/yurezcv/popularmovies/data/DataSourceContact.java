package ua.yurezcv.popularmovies.data;

import java.util.List;

import ua.yurezcv.popularmovies.data.model.Movie;
import ua.yurezcv.popularmovies.movies.MoviesFilterType;


public interface DataSourceContact {

    interface LoadMoviesCallback {

        void onSuccess(List<Movie> movies);

        void onFailure(Throwable throwable);
    }

    void loadMovies(MoviesFilterType filterType, LoadMoviesCallback callback);
}
