package ua.yurezcv.popularmovies.data;

import java.util.ArrayList;
import java.util.List;

import ua.yurezcv.popularmovies.data.model.Movie;
import ua.yurezcv.popularmovies.data.remote.RemoteDataSource;
import ua.yurezcv.popularmovies.movies.MoviesFilterType;

/*
 * Singleton class for data repository (remote only for now) and caching acquired data
 */
public class DataRepository implements DataSourceContact {

    private static DataRepository instance;

    // TODO add local repository for stage 2
    private final RemoteDataSource mRemoteDataSource;

    private List<Movie> mMoviesCache;
    private MoviesFilterType mLastFilterType;

    private int mSelectedPosition;

    private DataRepository() {
        mRemoteDataSource = new RemoteDataSource();
        mMoviesCache = new ArrayList<>();
        mSelectedPosition = -1;
    }

    public static DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }

        return instance;
    }


    @Override
    public void loadMovies(MoviesFilterType filterType, final LoadMoviesCallback callback) {

        if(filterType == mLastFilterType && !mMoviesCache.isEmpty()) {
            callback.onSuccess(mMoviesCache);
        } else {
            mLastFilterType = filterType;
            mRemoteDataSource.loadMovies(filterType, new LoadMoviesCallback() {
                @Override
                public void onSuccess(List<Movie> movies) {
                    mMoviesCache = movies;
                    callback.onSuccess(movies);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }

    public Movie getLastSelectedMovie() {
        if(mSelectedPosition != -1 && !mMoviesCache.isEmpty()) {
            return mMoviesCache.get(mSelectedPosition);
        }
        return null;
    }
}
