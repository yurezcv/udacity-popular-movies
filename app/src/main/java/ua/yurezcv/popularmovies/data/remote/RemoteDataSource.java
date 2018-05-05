package ua.yurezcv.popularmovies.data.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.yurezcv.popularmovies.data.DataSourceContact;
import ua.yurezcv.popularmovies.data.model.Movie;
import ua.yurezcv.popularmovies.data.model.MoviesResult;
import ua.yurezcv.popularmovies.movies.MoviesFilterType;

public class RemoteDataSource implements DataSourceContact {

    private MoviesAPI mMoviesAPI;

    public RemoteDataSource() {
        mMoviesAPI = RetrofitMoviesClient.getInstance().getMoviesApiService();
    }

    @Override
    public void loadMovies(MoviesFilterType filterType, final LoadMoviesCallback callback) {
        Call<MoviesResult> call = null;
        switch (filterType) {
            case POPULAR_MOVIES:
                call = mMoviesAPI.getPopularMovies();
                break;
            case HIGHEST_RATED_MOVIES:
                call = mMoviesAPI.getHighestRatedMovies();
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
}
