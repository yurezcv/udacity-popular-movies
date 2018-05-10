package ua.yurezcv.popularmovies.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * Singleton class for Retrofit and MoviesResult API service
 * since we need only one instance.
 */
public class RetrofitMoviesClient {

    private static RetrofitMoviesClient instance = null;

    private MoviesAPI moviesAPI;

    private RetrofitMoviesClient() {

        // add http client interceptor to add an API key to every query
        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", MoviesAPI.API_KEY)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MoviesAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        moviesAPI = retrofit.create(MoviesAPI.class);
    }

    public static RetrofitMoviesClient getInstance() {
        if (instance == null) {
            instance = new RetrofitMoviesClient();
        }

        return instance;
    }

    public MoviesAPI getMoviesApiService() {
        return moviesAPI;
    }
}
