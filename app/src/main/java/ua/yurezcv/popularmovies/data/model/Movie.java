package ua.yurezcv.popularmovies.data.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;

import ua.yurezcv.popularmovies.utils.Utils;

public class Movie implements Parcelable {

    public Movie() {
    }

    public Movie(Cursor cursor) {
        this.setId(cursor.getLong(1));
        this.setTitle(cursor.getString(2));
        this.setOverview(cursor.getString(3));
        this.setPosterPath(cursor.getString(4));
        this.setBackdropPath(cursor.getString(5));
        this.setReleaseDate(cursor.getString(6));
        this.setVoteAverage(cursor.getFloat(7));
    }

    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("vote_average")
    private float voteAverage;

    @SerializedName("overview")
    private String overview;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getFormattedReleaseDate() throws ParseException {
        return Utils.formatReleaseDate(getReleaseDate());
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +  '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.releaseDate);
        dest.writeString(this.posterPath);
        dest.writeFloat(this.voteAverage);
        dest.writeString(this.overview);
    }

    protected Movie(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.voteAverage = in.readFloat();
        this.overview = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }
}
