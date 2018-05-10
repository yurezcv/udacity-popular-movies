package ua.yurezcv.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResult {

    @SerializedName("id")
    private long movieId;

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Review> reviews;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }
}
