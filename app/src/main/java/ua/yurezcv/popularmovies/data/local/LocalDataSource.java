package ua.yurezcv.popularmovies.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import ua.yurezcv.popularmovies.data.DataSourceContact;
import ua.yurezcv.popularmovies.data.model.Movie;
import ua.yurezcv.popularmovies.utils.threading.AppExecutors;

public class LocalDataSource implements DataSourceContact {

    private final Context mContext;
    private final AppExecutors mAppExecutors;

    public LocalDataSource(Context context, AppExecutors appExecutors) {
        mContext = context;
        mAppExecutors = appExecutors;
    }

    @Override
    public void loadMovies(int filterType, LoadMoviesCallback callback) {
        loadMovies(filterType, INITIAL_LOAD_PAGE, callback);
    }

    @Override
    public void loadMovies(int filterType, int page, final LoadMoviesCallback callback) {
        if(filterType == FILTER_FAVORITES) {
            final Uri uri = MovieContract.MovieEntry.CONTENT_URI;

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Cursor cursor = mContext.getContentResolver()
                            .query(uri, null, null, null, null);
                    final List<Movie> movies = new ArrayList<>();
                    if(cursor != null) {
                        try {
                            while (cursor.moveToNext()) {
                                movies.add(new Movie(cursor));
                            }
                        } finally {
                            cursor.close();
                        }
                    } else {
                        callback.onFailure(new Throwable("There is no favorites"));
                    }
                    mAppExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (movies.isEmpty()) {
                                // This will be called if the table is new or just empty.
                                callback.onFailure(new Throwable("There is no favorites"));
                            } else {
                                callback.onSuccess(movies);
                            }
                        }
                    });
                }
            };

            mAppExecutors.diskIO().execute(runnable);
        }
    }

    @Override
    public void addToFavorites(Movie movie, AddToFavoritesCallback callback) {

        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();

        // Put the movie fields into the ContentValues
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

        // Insert the content values via a ContentResolver
        Uri uri = mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        if(uri != null) {
            callback.onSuccess(uri);
        } else {
            callback.onFailure(new Throwable("Couldn't save a favorite movie"));
        }
    }

    @Override
    public void removeFromFavorites(long movieId, RemoveFromFavoritesCallback callback) {
        Uri uri = MovieContract.MovieEntry.buildUriWithMovieId(movieId);

        // Delete a movie from the favorites
        int deletedRows = mContext.getContentResolver().delete(uri,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                null);
        if(deletedRows > 0) {
            callback.onSuccess(deletedRows);
        } else {
            callback.onFailure(new Throwable("Movie hasn't been found in Favorites"));
        }
    }

    @Override
    public void isMovieInFavorites(long movieId, final IsMovieInFavoritesCallback callback) {
        final Uri uri = MovieContract.MovieEntry.buildUriWithMovieId(movieId);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Cursor cursor = mContext.getContentResolver()
                        .query(uri, null, null, null, null);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (cursor == null) {
                            // This will be called if the table is new or just empty.
                            callback.onFailure(new Throwable("Error in checking if current movie is in favorites"));
                        } else {
                            if(cursor.getCount() != 0) {
                                callback.onSuccess(true);
                            } else {
                                callback.onSuccess(false);
                            }
                            cursor.close();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
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
