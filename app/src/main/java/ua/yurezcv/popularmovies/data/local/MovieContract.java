package ua.yurezcv.popularmovies.data.local;

import android.net.Uri;
import android.provider.BaseColumns;

public final class MovieContract {

    // Suppress default constructor
    private MovieContract() {
        throw new AssertionError();
    }

    public static final String CONTENT_AUTHORITY = "ua.yurezcv.popularmovies";

    public static final String PATH_MOVIE = "movie";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class MovieEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the Movies table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "movies";

        /* Movie ID from the movie database */
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_OVERVIEW = "overview";

        public static Uri buildUriWithMovieId(long movieId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(movieId))
                    .build();
        }

    }
}
