package ua.yurezcv.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersResult {

    @SerializedName("id")
    private long movieId;

    @SerializedName("results")
    private List<Trailer> trailers;

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }
}
