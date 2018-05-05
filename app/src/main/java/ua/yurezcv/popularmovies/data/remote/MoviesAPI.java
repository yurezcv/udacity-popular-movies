package ua.yurezcv.popularmovies.data.remote;


import retrofit2.Call;
import retrofit2.http.GET;
import ua.yurezcv.popularmovies.BuildConfig;
import ua.yurezcv.popularmovies.data.model.MoviesResult;

public interface MoviesAPI {

    String API_KEY = BuildConfig.API_KEY;
    String BASE_URL = "https://api.themoviedb.org/";

    @GET("3/movie/popular")
    Call<MoviesResult> getPopularMovies();

    @GET("3/movie/top_rated")
    Call<MoviesResult> getHighestRatedMovies();
}
