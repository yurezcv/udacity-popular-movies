package ua.yurezcv.popularmovies.data.remote;

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

    private static volatile RetrofitMoviesClient instance;

    private MoviesAPI moviesAPI;

    private RetrofitMoviesClient() {

        // Prevent form the reflection api.
        if (instance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

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

        // making instance thread safe
        if (instance == null) {
            synchronized (RetrofitMoviesClient.class) {
                if (instance == null) instance = new RetrofitMoviesClient();
            }
        }

        return instance;
    }

    public MoviesAPI getMoviesApiService() {
        return moviesAPI;
    }
}
