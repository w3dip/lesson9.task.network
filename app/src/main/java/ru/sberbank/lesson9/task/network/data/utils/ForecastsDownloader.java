package ru.sberbank.lesson9.task.network.data.utils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import ru.sberbank.lesson9.task.network.domain.mapper.Mapper;

public abstract class ForecastsDownloader<DbResultType, ApiType> {
    private final Mapper<ApiType, DbResultType> mapper;
    private final MediatorLiveData<DbResultType> result = new MediatorLiveData<>();

    @WorkerThread
    protected abstract void saveCallResult(@NonNull DbResultType data);

    @NonNull
    @MainThread
    protected abstract LiveData<DbResultType> loadFromDb();

    @NonNull
    @MainThread
    protected abstract LiveData<ApiType> createCall();

    public final LiveData<DbResultType> getAsLiveData() {
        return result;
    }

    @MainThread
    public ForecastsDownloader(Mapper<ApiType, DbResultType> mapper, boolean isNetworkAvailable) {
        this.mapper = mapper;
        result.setValue(null);
        final LiveData<DbResultType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (isNetworkAvailable) {
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource, newData -> result.setValue(newData));
            }
        });
    }

    private void fetchFromNetwork(final LiveData<DbResultType> dbSource) {
        final LiveData<ApiType> apiResponse = createCall();
        result.addSource(dbSource, newData -> result.setValue(newData));
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);
            saveResultAndReInit(response);
        });
    }

    @MainThread
    private void saveResultAndReInit(final ApiType response) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                saveCallResult(mapper.map(response));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                result.addSource(loadFromDb(), newData -> result.setValue(newData));
            }
        }.execute();
    }
}