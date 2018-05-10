package ua.yurezcv.popularmovies.data.local;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static ua.yurezcv.popularmovies.data.local.MovieContract.MovieEntry.TABLE_NAME;

public class MoviesProvider extends ContentProvider {

    public static final int CODE_FAVORITE_MOVIES = 111;
    public static final int CODE_FAVORITE_MOVIE_DETAILS = 222;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mDbHelper;

    /* Simple URI matcher for custom codes */
    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        /* This URI is content://ua.yurezcv.popularmovies/movie/ */
        matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_FAVORITE_MOVIES);

        /* URI sample content://ua.yurezcv.popularmovies/movie/278 */
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", CODE_FAVORITE_MOVIE_DETAILS);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        // get an instance of the DB Helper class
        mDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE_MOVIES:
                cursor = mDbHelper.getReadableDatabase().query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_FAVORITE_MOVIE_DETAILS:
                // get movieId from the passed URI and add it to the selectionArgs array
                String movieId = uri.getLastPathSegment();
                String[] selArgs = new String[]{movieId};

                cursor = mDbHelper.getReadableDatabase().query(
                        TABLE_NAME,
                        projection,
                        // add an SQL selection to get results by specific movieId
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selArgs,
                        null,
                        null,
                        sortOrder);

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("This method hasn't been implementer yet.");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // get an instance of writable DB
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case CODE_FAVORITE_MOVIES:
                // Inserting values into movies table
                long id = db.insert(TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("This method hasn't been implementer yet.");
    }

    @Override
    public void shutdown() {
        mDbHelper.close();
        super.shutdown();
    }
}
