package ru.sberbank.lesson9.task.network.domain.interactor;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class UseCase<T> {

    private CompositeDisposable disposable = new CompositeDisposable();

    protected abstract Single<T> buildUseCaseObservable();

    public <S extends SingleObserver<T> & Disposable> void execute(S useCaseDisposable) {
        disposable.add(buildUseCaseObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(useCaseDisposable));
    }

    public void dispose() {
        if (!this.disposable.isDisposed()) {
            this.disposable.dispose();
        }
    }
}
