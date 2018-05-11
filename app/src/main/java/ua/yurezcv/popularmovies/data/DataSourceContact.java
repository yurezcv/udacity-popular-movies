package ua.yurezcv.popularmovies.data;

import java.util.List;

import ua.yurezcv.popularmovies.data.model.Movie;
import ua.yurezcv.popularmovies.data.model.Review;
import ua.yurezcv.popularmovies.data.model.Trailer;


public interface DataSourceContact {

    int INITIAL_LOAD_PAGE = 1;

    int FILTER_MOST_POPULAR = 111;
    int FILTER_HIGHEST_RATED = 222;
    int FILTER_FAVORITES = 333;

    interface LoadMoviesCallback {
        void onSuccess(List<Movie> movies);
        void onFailure(Throwable throwable);
    }

    interface LoadTrailersCallback {
        void onSuccess(List<Trailer> trailers);
        void onFailure(Throwable throwable);
    }

    interface LoadReviewsCallback {
        void onSuccess(List<Review> reviews);
        void onFailure(Throwable throwable);
    }

    void loadMovies(int filterType, LoadMoviesCallback callback);

    void loadMovies(int filterType, int page, LoadMoviesCallback callback);

    void loadMovieTrailers(long movieId, LoadTrailersCallback callback);

    void loadMovieReviews(long movieId, LoadReviewsCallback callback);
}
