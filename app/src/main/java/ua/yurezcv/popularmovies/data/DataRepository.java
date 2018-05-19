package ua.yurezcv.popularmovies.data;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import ua.yurezcv.popularmovies.PopularMoviesApp;
import ua.yurezcv.popularmovies.data.local.LocalDataSource;
import ua.yurezcv.popularmovies.data.model.Movie;
import ua.yurezcv.popularmovies.data.model.Review;
import ua.yurezcv.popularmovies.data.model.Trailer;
import ua.yurezcv.popularmovies.data.remote.RemoteDataSource;
import ua.yurezcv.popularmovies.utils.threading.AppExecutors;

/*
 * Singleton class for data repository (remote only for now) and caching acquired data
 */
public class DataRepository implements DataSourceContact {

    private static volatile DataRepository instance;

    private final LocalDataSource mLocalDataSource;
    private final RemoteDataSource mRemoteDataSource;

    private List<Movie> mMoviesCache;
    private int mLastFilterType;

    private int mSelectedPosition;
    private boolean mIsFavoritesUpdated;

    private DataRepository(Context context, AppExecutors appExecutors) {

        // Prevent form the reflection api.
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        mLocalDataSource = new LocalDataSource(context, appExecutors);
        mRemoteDataSource = new RemoteDataSource();
        mMoviesCache = new ArrayList<>();
        mSelectedPosition = -1;
        mIsFavoritesUpdated = false;
    }

    public static DataRepository getInstance(Context context, AppExecutors appExecutors) {
        // making instance thread safe
        if (instance == null) {
            synchronized (DataRepository.class) {
                if (instance == null) instance = new DataRepository(context, appExecutors);
            }
        }

        return instance;
    }

    @Override
    public void loadMovies(int filterType, final LoadMoviesCallback callback) {
        // clear cache for an initial movie load
        mMoviesCache.clear();
        loadMovies(filterType, INITIAL_LOAD_PAGE, callback);
    }

    @Override
    public void loadMovies(int filterType, int page, final LoadMoviesCallback callback) {
        if(filterType == mLastFilterType && !mMoviesCache.isEmpty() && page == INITIAL_LOAD_PAGE) {
            callback.onSuccess(mMoviesCache);
        } else {
            mLastFilterType = filterType;
            LoadMoviesCallback loadMoviesCallback = new LoadMoviesCallback() {
                @Override
                public void onSuccess(List<Movie> movies) {
                    mMoviesCache.addAll(movies);
                    callback.onSuccess(movies);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            };

            if(mLastFilterType != FILTER_FAVORITES) {
                if(PopularMoviesApp.isOnline()) {
                    mRemoteDataSource.loadMovies(filterType, page, loadMoviesCallback);
                } else {
                    callback.onFailure(new Throwable("Please connect to Internet"));
                }
            } else {
                mLocalDataSource.loadMovies(filterType, loadMoviesCallback);
            }
        }
    }

    // get last selected movies value from the saved in this class variable
    public void loadMovieTrailers(LoadTrailersCallback callback) {
        loadMovieTrailers(getLastSelectedMovie().getId(), callback);
    }

    @Override
    public void loadMovieTrailers(long movieId, final LoadTrailersCallback callback) {
        mRemoteDataSource.loadMovieTrailers(movieId, new LoadTrailersCallback() {
            @Override
            public void onSuccess(List<Trailer> trailers) {
                callback.onSuccess(trailers);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    // get last selected movies value from the saved in this class variable
    public void loadMovieReviews(LoadReviewsCallback callback) {
        loadMovieReviews(getLastSelectedMovie().getId(), callback);
    }

    @Override
    public void loadMovieReviews(long movieId, final LoadReviewsCallback callback) {
        mRemoteDataSource.loadMovieReviews(movieId, new LoadReviewsCallback() {
            @Override
            public void onSuccess(List<Review> reviews) {
                callback.onSuccess(reviews);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void addToFavorites(Movie movie, final AddToFavoritesCallback callback) {
        mLocalDataSource.addToFavorites(movie, new AddToFavoritesCallback() {
            @Override
            public void onSuccess(Uri uri) {
                callback.onSuccess(uri);
                // drop flag to false, in case a user tries several times
                // to add/delete a movie from favorites
                mIsFavoritesUpdated = false;
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    public void removeFromFavorites(final RemoveFromFavoritesCallback callback) {
        removeFromFavorites(getLastSelectedMovie().getId(), callback);
    }

    @Override
    public void removeFromFavorites(long movieId, final RemoveFromFavoritesCallback callback) {
        mLocalDataSource.removeFromFavorites(movieId, new RemoveFromFavoritesCallback() {
            @Override
            public void onSuccess(int rowsDeleted) {
                callback.onSuccess(rowsDeleted);
                // only set the flag when last action was removing
                // the movie from favorites
                mIsFavoritesUpdated = true;
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    public void isMovieInFavorites(IsMovieInFavoritesCallback callback) {
        isMovieInFavorites(getLastSelectedMovie().getId(), callback);
    }

    @Override
    public void isMovieInFavorites(long movieId, final IsMovieInFavoritesCallback callback) {
        mLocalDataSource.isMovieInFavorites(movieId, new IsMovieInFavoritesCallback() {
            @Override
            public void onSuccess(boolean isInFavorites) {
                callback.onSuccess(isInFavorites);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
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

    public boolean isFavoritesUpdated() {
        return mIsFavoritesUpdated;
    }

    public void clearIsFavoritesUpdatedFlag() {
        mIsFavoritesUpdated = false;
    }
}
