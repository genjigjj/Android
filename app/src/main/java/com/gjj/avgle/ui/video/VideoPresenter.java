package com.gjj.avgle.ui.video;

import android.util.Log;

import com.gjj.avgle.net.ApiHelper;
import com.gjj.avgle.ui.base.BasePresenter;
import com.gjj.avgle.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class VideoPresenter<V extends VideoMvpView> extends BasePresenter<V> implements VideoMvpPresenter<V> {

    @Inject
    public VideoPresenter(CompositeDisposable compositeDisposable, ApiHelper apiHelper, SchedulerProvider schedulerProvider) {
        super(compositeDisposable, apiHelper, schedulerProvider);
    }

    @Override
    public void showVideo() {
        getMvpView().showLoading();
        getData(0);
    }

    @Override
    public void loadMoreVideo(int pageNo) {
        getData(pageNo);
        getMvpView().finishLoadMore();
    }

    @Override
    public void refreshVideo() {
        getMvpView().resetAdapter();
        getData(0);
    }

    private void getData(int pageNo) {
        getCompositeDisposable().add(getAppApiHelper()
                .getVideos(pageNo)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (!isViewAttached()) {
                        getMvpView().hideLoading();
                        return;
                    }
                    if (response != null && response.isSuccess()) {
                        Log.d("response", response.toString());
                        getMvpView().addItem(response.getResponse().getVideos());
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
