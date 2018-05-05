package ua.yurezcv.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResult {

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Movie> listOfMovies;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getListOfMovies() {
        return listOfMovies;
    }

    public void setListOfMovies(List<Movie> listOfMovies) {
        this.listOfMovies = listOfMovies;
    }
}
