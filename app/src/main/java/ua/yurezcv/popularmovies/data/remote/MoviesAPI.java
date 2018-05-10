package ua.yurezcv.popularmovies.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ua.yurezcv.popularmovies.BuildConfig;
import ua.yurezcv.popularmovies.data.model.MoviesResult;
import ua.yurezcv.popularmovies.data.model.ReviewsResult;
import ua.yurezcv.popularmovies.data.model.TrailersResult;

public interface MoviesAPI {

    String API_KEY = BuildConfig.API_KEY;
    String BASE_URL = "https://api.themoviedb.org/3/";

    @GET("movie/popular")
    Call<MoviesResult> getPopularMovies(@Query("page") int page);

    @GET("movie/top_rated")
    Call<MoviesResult> getHighestRatedMovies(@Query("page") int page);

    @GET("movie/{id}/videos")
    Call<TrailersResult> getMovieTrailers(@Path("id") long movieId);

    @GET("movie/{id}/reviews")
    Call<ReviewsResult> getMovieReviews(@Path("id") long movieId);

}
