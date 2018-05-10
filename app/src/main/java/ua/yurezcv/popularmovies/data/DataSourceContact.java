package ua.yurezcv.popularmovies.data;

import java.util.List;

import ua.yurezcv.popularmovies.data.model.Movie;
import ua.yurezcv.popularmovies.data.model.Review;
import ua.yurezcv.popularmovies.data.model.Trailer;
import ua.yurezcv.popularmovies.movies.MoviesFilterType;


public interface DataSourceContact {

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

    void loadMovies(MoviesFilterType filterType, LoadMoviesCallback callback);

    void loadMovieTrailers(long movieId, LoadTrailersCallback callback);

    void loadMovieReviews(long movieId, LoadReviewsCallback callback);
}
