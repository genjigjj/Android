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
    public void showVideo(String c) {
        getData(0, c);
    }

    @Override
    public void loadMoreVideo(int pageNo, String c) {
        getData(pageNo, c);
        getMvpView().finishLoadMore();
    }

    @Override
    public void refreshVideo(String c) {
        getMvpView().resetAdapter();
        getData(0, c);
    }

    private void getData(int pageNo, String c) {
        getCompositeDisposable().add(getAppApiHelper()
                .getVideos(pageNo, c)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (!isViewAttached()) {
                        return;
                    }
                    if (response != null && response.isSuccess()) {
                        Log.d("response", response.toString());
                        getMvpView().addResponse(response);
                    }
                    getMvpView().finishRefresh();
                }, throwable -> {
                    if (!isViewAttached()) {
                        return;
                    }
                    getMvpView().finishRefresh();
                    getMvpView().onError("网络错误");
                }));
    }
}
