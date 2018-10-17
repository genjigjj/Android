package com.gjj.avgle.ui.favorites;

import android.util.Log;

import com.gjj.avgle.net.ApiHelper;
import com.gjj.avgle.net.model.AvgleResponse;
import com.gjj.avgle.net.model.Video;
import com.gjj.avgle.net.model.VideoDetail;
import com.gjj.avgle.ui.base.BasePresenter;
import com.gjj.avgle.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class FavoritePresenter<V extends FavoriteMvpView> extends BasePresenter<V> implements FavoriteMvpPresenter<V> {

    @Inject
    public FavoritePresenter(CompositeDisposable compositeDisposable, ApiHelper apiHelper, SchedulerProvider schedulerProvider) {
        super(compositeDisposable, apiHelper, schedulerProvider);
    }

    @Override
    public void showVideo() {
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
                .getFavorites(pageNo * 10, (pageNo + 1) * 10)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().io())
                .concatMap(collectionResponse -> {
                    if (!isViewAttached()) {
                        return null;
                    }
                    if (collectionResponse != null && collectionResponse.getStatus() == 1) {
                        Log.d("response", collectionResponse.toString());
                        int[] vidList = collectionResponse.getVidList();
                        List<Observable<VideoDetail>> observableList = new ArrayList<>();
                        for (int id : vidList) {
                            Observable<VideoDetail> detail = getAppApiHelper().getDetail(id);
                            observableList.add(detail);
                        }
                        return Observable.zip(observableList, objects -> {
                            AvgleResponse avgleResponse = new AvgleResponse();
                            List<Video> videoList = new ArrayList<>();
                            for (Object o : objects) {
                                VideoDetail video = (VideoDetail) o;
                                if (video.isSuccess()) {
                                    videoList.add(video.getResponse().getVideo());
                                }
                            }
                            avgleResponse.setTotal_videos(collectionResponse.getTotal());
                            avgleResponse.setVideos(videoList);
                            return avgleResponse;
                        });
                    }
                    return null;
                })
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (!isViewAttached()) {
                        return;
                    }
                    if (response != null) {
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
