package ua.yurezcv.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

public class Review {
    /*
        "author":"elshaarawy",
        "content":"very good movie 9.5/10 محمد الشعراوى",
        "id":"5723a329c3a3682e720005db",
        "url":"https://www.themoviedb.org/review/5723a329c3a3682e720005db"
    */

    @SerializedName("id")
    private String id;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    @SerializedName("url")
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
