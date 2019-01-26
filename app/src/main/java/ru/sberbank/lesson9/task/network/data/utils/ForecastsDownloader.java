package ru.sberbank.lesson9.task.network.data.utils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import ru.sberbank.lesson9.task.network.domain.mapper.Mapper;

public abstract class ForecastsDownloader<DbResultType, ApiResultType, ApiType> {
    private Mapper<ApiType, DbResultType> mapper;

    private final MediatorLiveData<DbResultType> result = new MediatorLiveData<>();

    @WorkerThread
    protected abstract void saveCallResult(@NonNull DbResultType data);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable DbResultType data);

    @NonNull
    @MainThread
    protected abstract LiveData<DbResultType> loadFromDb();

    @NonNull
    @MainThread
    protected abstract LiveData<ApiType> createCall();

    @MainThread
    protected void onFetchFailed() {
    }

    public final LiveData<DbResultType> getAsLiveData() {
        return result;
    }

    @MainThread
    public ForecastsDownloader(Mapper<ApiType, DbResultType> mapper, boolean isNetworkAvailable) {
        this.mapper = mapper;
        result.setValue(null);
        final LiveData<DbResultType> dbSource = loadFromDb();
        result.addSource(dbSource, new Observer<DbResultType>() {
            @Override
            public void onChanged(@Nullable DbResultType data) {
                result.removeSource(dbSource);
                if (isNetworkAvailable) {
                    fetchFromNetwork(dbSource);
                } else {
                    result.addSource(dbSource, new Observer<DbResultType>() {
                        @Override
                        public void onChanged(@Nullable DbResultType newData) {
                            result.setValue(newData);
                        }
                    });
                }

            }
        });


    }

    private void fetchFromNetwork(final LiveData<DbResultType> dbSource) {
        final LiveData<ApiType> apiResponse = createCall();
        result.addSource(dbSource, new Observer<DbResultType>() {
            @Override
            public void onChanged(@Nullable DbResultType newData) {
                result.setValue(newData);
            }
        });
        result.addSource(apiResponse, new Observer<ApiType>() {
                    @Override
                    public void onChanged(@Nullable final ApiType response) {
                        result.removeSource(apiResponse);
                        result.removeSource(dbSource);
                        //noinspection ConstantConditions
                        //if (response.isSuccessful()) {
                        saveResultAndReInit(response);
                        /*} else {
                            onFetchFailed();
                            result.addSource(dbSource, new Observer<DbResultType>() {
                                @Override
                                public void onChanged(@Nullable DbResultType newData) {
                                    result.setValue(
                                            Resource.error(new AppException(response.getError()), newData));
                                }
                            });
                        }*/
                    }
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
                result.addSource(loadFromDb(), new Observer<DbResultType>() {
                    @Override
                    public void onChanged(@Nullable DbResultType newData) {
                        result.setValue(newData);
                    }
                });
            }
        }.execute();
    }
}