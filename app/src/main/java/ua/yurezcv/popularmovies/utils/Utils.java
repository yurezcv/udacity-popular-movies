package ua.yurezcv.popularmovies.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    private static final String BASE_IMG_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_IMG_SIZE = "w185/";
    private static final String POSTER_IMG_SIZE_LARGE = "w500/";

    public static String createPosterUrl(String poster) {
        return BASE_IMG_URL + POSTER_IMG_SIZE + poster;
    }

    public static String createLargePosterUrl(String poster) {
        return BASE_IMG_URL + POSTER_IMG_SIZE_LARGE + poster;
    }

    public static String formatReleaseDate(String oldReleaseDate) throws ParseException {
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date newDate = spf.parse(oldReleaseDate);
        spf = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        return spf.format(newDate);
    }

    public static String makeYoutubeLink(String key) {
        return "https://www.youtube.com/watch?v=" + key;
    }

}
