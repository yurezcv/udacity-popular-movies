package ua.yurezcv.popularmovies.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ua.yurezcv.popularmovies.data.local.MovieContract.MovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "movies.db";

    private static final int DB_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our weather data.
         */
        final String SQL_CREATE_WEATHER_TABLE =

                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +

                        MovieEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieEntry.COLUMN_MOVIE_ID       + " INTEGER NOT NULL, "                 +
                        MovieEntry.COLUMN_TITLE + " TEXT,"                  +
                        MovieEntry.COLUMN_OVERVIEW   + " TEXT, "                    +
                        MovieEntry.COLUMN_POSTER_PATH   + " TEXT, "                    +
                        MovieEntry.COLUMN_BACKDROP_PATH   + " TEXT, "                    +
                        MovieEntry.COLUMN_RELEASE_DATE   + " TEXT, "                    +
                        MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, "             +

                        /*
                         * To ensure this table can only contain one movie entry per movieId, we declare
                         * the date column to be unique.
                         */
                        " UNIQUE (" + MovieEntry.COLUMN_MOVIE_ID+ ") ON CONFLICT REPLACE);";

        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */
        db.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop and recreate DB on Upgrade for now
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
