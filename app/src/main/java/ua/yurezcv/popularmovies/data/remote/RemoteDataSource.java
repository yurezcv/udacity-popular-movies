package ua.yurezcv.popularmovies.data.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.yurezcv.popularmovies.data.DataSourceContact;
import ua.yurezcv.popularmovies.data.model.Movie;
import ua.yurezcv.popularmovies.data.model.MoviesResult;
import ua.yurezcv.popularmovies.data.model.Review;
import ua.yurezcv.popularmovies.data.model.ReviewsResult;
import ua.yurezcv.popularmovies.data.model.Trailer;
import ua.yurezcv.popularmovies.data.model.TrailersResult;
import ua.yurezcv.popularmovies.movies.MoviesFilterType;

public class RemoteDataSource implements DataSourceContact {

    private static final int DEFAULT_PAGE = 1;

    private MoviesAPI mMoviesAPI;

    public RemoteDataSource() {
        mMoviesAPI = RetrofitMoviesClient.getInstance().getMoviesApiService();
    }

    @Override
    public void loadMovies(MoviesFilterType filterType, LoadMoviesCallback callback) {
        loadMovies(filterType, INITIAL_LOAD_PAGE, callback);
    }

    @Override
    public void loadMovies(MoviesFilterType filterType, int page, final LoadMoviesCallback callback) {
        Call<MoviesResult> call = null;
        switch (filterType) {
            case POPULAR_MOVIES:
                call = mMoviesAPI.getPopularMovies(page);
                break;
            case HIGHEST_RATED_MOVIES:
                call = mMoviesAPI.getHighestRatedMovies(page);
                break;
        }

        call.enqueue(new Callback<MoviesResult>() {
            @Override
            public void onResponse(Call<MoviesResult> call, Response<MoviesResult> response) {
                if (!response.isSuccessful()) {
                    // Handle http error
                    callback.onFailure(new Throwable("HTTP error message " + response.message()));
                    return;
                }

                MoviesResult moviesResult = response.body();
                List<Movie> movieList = moviesResult.getListOfMovies();

                if(movieList != null) {
                    callback.onSuccess(movieList);
                }
            }

            @Override
            public void onFailure(Call<MoviesResult> call, Throwable t) {
                // handle retrofit callback failure
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void loadMovieTrailers(long movieId, final LoadTrailersCallback callback) {
        Call<TrailersResult> call = mMoviesAPI.getMovieTrailers(movieId);

        call.enqueue(new Callback<TrailersResult>() {
            @Override
            public void onResponse(Call<TrailersResult> call, Response<TrailersResult> response) {
                if (!response.isSuccessful()) {
                    // Handle http error
                    callback.onFailure(new Throwable("HTTP error message " + response.message()));
                    return;
                }

                TrailersResult trailersResult = response.body();
                List<Trailer> trailers = trailersResult.getTrailers();

                if(trailers != null) {
                    callback.onSuccess(trailers);
                }
            }

            @Override
            public void onFailure(Call<TrailersResult> call, Throwable t) {
                // handle retrofit callback failure
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void loadMovieReviews(long movieId, final LoadReviewsCallback callback) {
        Call<ReviewsResult> call = mMoviesAPI.getMovieReviews(movieId);

        call.enqueue(new Callback<ReviewsResult>() {
            @Override
            public void onResponse(Call<ReviewsResult> call, Response<ReviewsResult> response) {
                if (!response.isSuccessful()) {
                    // Handle http error
                    callback.onFailure(new Throwable("HTTP error message " + response.message()));
                    return;
                }

                ReviewsResult reviewsResult = response.body();
                List<Review> reviews = reviewsResult.getReviews();

                if(reviews != null) {
                    callback.onSuccess(reviews);
                }
            }

            @Override
            public void onFailure(Call<ReviewsResult> call, Throwable t) {
                // handle retrofit callback failure
                callback.onFailure(t);
            }
        });
    }


}
