package com.gjj.avgle.ui.play;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.error.ANError;
import com.awesapp.isafe.svs.parsers.PSVS21;
import com.gjj.avgle.di.ApplicationContext;
import com.gjj.avgle.net.ApiHelper;
import com.gjj.avgle.ui.base.BasePresenter;
import com.gjj.avgle.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class PlayPresenter<V extends PlayMvpView> extends BasePresenter<V> implements PlayMvpPresenter<V> {

    private static final String TAG = "PlayPresenter";

    private Context context;

    @Inject
    public PlayPresenter(CompositeDisposable compositeDisposable, @ApplicationContext Context context,
                         ApiHelper apiHelper, SchedulerProvider schedulerProvider) {
        super(compositeDisposable, apiHelper, schedulerProvider);
        this.context = context;
    }

    @Override
    public void getPlayVideoUrl(String vid) {
        String ts = String.valueOf(System.currentTimeMillis() / 1000);
        String url = String.format("https://avgle.com//mp4.php?vid=%s&ts=%s&hash=%s&m3u8"
                , vid
                , ts
                , PSVS21.computeHash(new PSVS21.StubContext(context), vid, ts));
        if (url != null && url.length() > 0) {
            getCompositeDisposable().add(getAppApiHelper()
                    .getPlayVideoUrl(url)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(response -> {
                        Log.d("response", response.toString());
                        if (!isViewAttached()) {
                            getMvpView().hideLoading();
                            return;
                        }
                        final String videoUrl = response.raw().request().url().toString();
                        if (videoUrl != null) {
                            Log.d("视频地址", videoUrl);
                            getMvpView().playVideo(videoUrl);
                        }
                    }, throwable -> {
                        if (!isViewAttached()) {
                            return;
                        }
                        // handle the login error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }));
        }
    }
}
