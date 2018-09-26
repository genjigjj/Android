package com.gjj.avgle.ui.video;

import android.util.Log;

import com.androidnetworking.error.ANError;
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
    public void onViewPrepared() {
        getMvpView().showLoading();
        getData(0);
    }

    @Override
    public void sendMoreRequest(int pageNo) {
        getMvpView().showLoading();
        getData(pageNo);
    }

    @Override
    public void refresh() {
        getMvpView().refreshVideo();
        getData(0);
    }

    private void getData(int pageNo) {
        getCompositeDisposable().add(getAppApiHelper()
                .getVideos(String.valueOf(pageNo))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (!isViewAttached()) {
                        getMvpView().hideLoading();
                        return;
                    }
                    if (response != null && response.isSuccess()) {
                        Log.d("response", response.toString());
                        getMvpView().updateVideo(response.getResponse().getVideos());
                    }
                    getMvpView().hideLoading();
                }, throwable -> {
                    if (!isViewAttached()) {
                        getMvpView().hideLoading();
                        return;
                    }
                    getMvpView().hideLoading();
                    // handle the login error here
                    if (throwable instanceof ANError) {
                        ANError anError = (ANError) throwable;
                        handleApiError(anError);
                    }
                }));
    }
}
