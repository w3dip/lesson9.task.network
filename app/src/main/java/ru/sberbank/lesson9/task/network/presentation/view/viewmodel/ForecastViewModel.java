package ru.sberbank.lesson9.task.network.presentation.view.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import ru.sberbank.lesson9.task.network.data.repository.ForecastDao;
import ru.sberbank.lesson9.task.network.data.repository.ForecastDataRepository;
import ru.sberbank.lesson9.task.network.data.repository.ForecastDatabase;
import ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient;
import ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi;
import ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;
import ru.sberbank.lesson9.task.network.presentation.model.ForecastItem;

import static ru.sberbank.lesson9.task.network.utils.InternetConnection.checkConnection;

public class ForecastViewModel extends AndroidViewModel {
    private LiveData<List<ForecastEntity>> forecasts;

    public ForecastViewModel(Application application) {
        super(application);
        ForecastDatabase forecastDatabase = ForecastDatabase.getDatabase(application);
        ForecastDao forecastDao = forecastDatabase.forecastDao();
        WeatherApi weatherApi = WeatherApiClient.getApiClient();
        ForecastRepository repository = new ForecastDataRepository(weatherApi, forecastDao);
        boolean isNetworkAvailable = checkConnection(application.getApplicationContext());
        forecasts = repository.getAll(isNetworkAvailable);
    }

    public LiveData<List<ForecastEntity>> getForecasts() {
        return forecasts;
    }

    /*public ForecastEntity getDetailedForecast(long id) {
        return repository.getByDate(id);
    }*/

    //final private MutableLiveData<Request> request = new MutableLiveData();
    /*final private LiveData<List<ForecastItem>> result = Transformations.switchMap(request, new Function<Request, LiveData<Resource<GitHubResponse>>>() {
        @Override
        public LiveData<Resource<GitHubResponse>> apply(final Request input) {
            LiveData<List<ForecastItem>> resourceLiveData = repository.getAll();
            final MediatorLiveData<Resource<GitHubResponse>> mediator = new MediatorLiveData<Resource<GitHubResponse>>();
            mediator.addSource(resourceLiveData, new Observer<Resource<List<GitHubDto>>>() {
                @Override
                public void onChanged(@Nullable Resource<List<GitHubDto>> gitHubDtos) {
                    GitHubResponse resp = new GitHubResponse(input.page, input.limit, gitHubDtos.getData());
                    Resource<GitHubResponse> response = null;
                    switch (gitHubDtos.getStatus()){
                        case LOADING:
                            response =  Resource.<GitHubResponse>loading(resp);
                            break;
                        case SUCCESS:
                            response =  Resource.<GitHubResponse>success(resp);
                            break;
                        case ERROR:
                            response =  Resource.<GitHubResponse>error(gitHubDtos.getException(), null);
                            break;

                    }
                    mediator.setValue(response);
                }
            });
            return mediator;
        }
    });*/

    /*public static ForecastViewModel create(FragmentActivity activity) {
        ForecastViewModel viewModel = ViewModelProviders.of(activity).get(ForecastViewModel.class);
        return viewModel;
    }*/

    //public void load(int page, int limit) {
    //    request.setValue(new Request(page, limit));
    //}

    /*@Inject
    public void setRepository(GitHubRepository2 repository) {
        this.repository = repository;
    }

    public LiveData<Resource<GitHubResponse>> getResult() {
        return result;
    }

    public void clearCache() {
        repository.clearCache();
    }

    public static class Request {
        final private int page, limit;

        public Request(int page, int limit) {
            this.page = page;
            this.limit = limit;
        }

        public int getLimit() {
            return limit;
        }
    }*/

}
