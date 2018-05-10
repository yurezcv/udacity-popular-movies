package ua.yurezcv.popularmovies.data.local;

import ua.yurezcv.popularmovies.data.DataSourceContact;
import ua.yurezcv.popularmovies.movies.MoviesFilterType;

public class LocalDataSource implements DataSourceContact {

    public LocalDataSource() {
    }

    @Override
    public void loadMovies(MoviesFilterType filterType, LoadMoviesCallback callback) {
        loadMovies(filterType, INITIAL_LOAD_PAGE, callback);
    }

    @Override
    public void loadMovies(MoviesFilterType filterType, int page, LoadMoviesCallback callback) {
        // TODO load movies from Content Provider
    }

    @Override
    public void loadMovieTrailers(long movieId, LoadTrailersCallback callback) {
        // implementation in RemoteDataSource.class only
    }

    @Override
    public void loadMovieReviews(long movieId, LoadReviewsCallback callback) {
        // implementation in RemoteDataSource.class only
    }
}
