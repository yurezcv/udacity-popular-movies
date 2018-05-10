package ua.yurezcv.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

public class Trailer {
    /*
        "id":"533ec653c3a3685448000249",
        "iso_639_1":"en",
        "iso_3166_1":"US",
        "key":"K_tLp7T6U1c",
        "name":"The Shawshank Redemption - Trailer",
        "site":"YouTube",
        "size":480,
        "type":"Trailer"
    */

    @SerializedName("id")
    private String id;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("site")
    private Site site;

    @SerializedName("type")
    private Type type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", site='" + site + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public enum Type {
        @SerializedName("Trailer") TRAILER,
        @SerializedName("Teaser") TEASER,
        @SerializedName("Clip") CLIP,
        @SerializedName("Featurette") FEATURETTE
    }

    public enum Site {
        @SerializedName("YouTube") YOUTUBE
    }
}
