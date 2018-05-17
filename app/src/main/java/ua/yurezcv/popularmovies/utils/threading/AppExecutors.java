package ua.yurezcv.popularmovies.utils.threading;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

public class AppExecutors {

    public static final int THREAD_COUNT = 3;

    private static volatile AppExecutors instance;

    private final Executor diskIO;

    private final Executor networkIO;

    private final Executor mainThread;

    private AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {

        // Prevent from the reflection api.
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

    public static AppExecutors getInstance(Executor diskIO, Executor networkIO, Executor mainThread) {

        // making instance thread safe
        if (instance == null) {
            synchronized (AppExecutors.class) {
                if (instance == null) instance = new AppExecutors(diskIO, networkIO, mainThread);
            }
        }

        return instance;
    }
}
