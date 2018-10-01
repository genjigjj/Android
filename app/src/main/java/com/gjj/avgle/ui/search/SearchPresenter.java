package com.gjj.avgle.ui.search;

import android.util.Log;

import com.gjj.avgle.net.ApiHelper;
import com.gjj.avgle.ui.base.BasePresenter;
import com.gjj.avgle.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class SearchPresenter<V extends SearchMvpView> extends BasePresenter<V> implements SearchMvpPresenter<V> {

    @Inject
    public SearchPresenter(CompositeDisposable compositeDisposable, ApiHelper apiHelper, SchedulerProvider schedulerProvider) {
        super(compositeDisposable, apiHelper, schedulerProvider);
    }


    @Override
    public void showVideo(String query) {
        getMvpView().showLoading();
        getData(query, 0);
    }

    @Override
    public void loadMoreVideo(String query, int pageNo) {
        getData(query, pageNo);
        getMvpView().finishLoadMore();
    }

    @Override
    public void refreshVideo(String query) {
        getMvpView().resetAdapter();
        getData(query, 0);
    }

    private void getData(String query, int page) {
        getCompositeDisposable().add(getAppApiHelper()
                .searchVideo(query, page)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (!isViewAttached()) {
                        getMvpView().hideLoading();
                        return;
                    }
                    if (response != null && response.isSuccess()) {
                        Log.d("response", response.toString());
                        getMvpView().addResponse(response);
                        getMvpView().finishRefresh();
                    }
                    getMvpView().hideLoading();
                }, throwable -> {
                    if (!isViewAttached()) {
                        getMvpView().hideLoading();
                        return;
                    }
                    getMvpView().hideLoading();
                    getMvpView().onError("网络错误");
                }));
    }
}
