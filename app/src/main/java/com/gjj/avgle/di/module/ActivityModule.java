/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.gjj.avgle.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.gjj.avgle.di.ActivityContext;
import com.gjj.avgle.di.PerActivity;
import com.gjj.avgle.net.AppApiHelper;
import com.gjj.avgle.net.model.Video;
import com.gjj.avgle.ui.main.MainMvpPresenter;
import com.gjj.avgle.ui.main.MainMvpView;
import com.gjj.avgle.ui.main.MainPresenter;
import com.gjj.avgle.ui.play.PlayMvpPresenter;
import com.gjj.avgle.ui.play.PlayMvpView;
import com.gjj.avgle.ui.play.PlayPresenter;
import com.gjj.avgle.ui.search.SearchMvpPresenter;
import com.gjj.avgle.ui.search.SearchMvpView;
import com.gjj.avgle.ui.search.SearchPresenter;
import com.gjj.avgle.ui.video.VideoAdapter;
import com.gjj.avgle.ui.video.VideoMvpPresenter;
import com.gjj.avgle.ui.video.VideoMvpView;
import com.gjj.avgle.ui.video.VideoPresenter;
import com.gjj.avgle.utils.rx.AppSchedulerProvider;
import com.gjj.avgle.utils.rx.SchedulerProvider;

import java.util.ArrayList;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by janisharali on 27/01/17.
 */

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    @Singleton
    AppApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @PerActivity
    MainMvpPresenter<MainMvpView> provideMainPresenter(
            MainPresenter<MainMvpView> presenter) {
        return presenter;
    }

    @Provides
    VideoMvpPresenter<VideoMvpView> providevideoPresenter(
            VideoPresenter<VideoMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    PlayMvpPresenter<PlayMvpView> providePlayPresenter(PlayPresenter<PlayMvpView> presenter) {
        return presenter;
    }

    @Provides
    VideoAdapter provideVideoAdapter() {
        return new VideoAdapter(new ArrayList<Video>());
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager() {
        return new LinearLayoutManager(mActivity);
    }

    @Provides
    SearchMvpPresenter<SearchMvpView> provideSearchMvpPresenter(
            SearchPresenter<SearchMvpView> presenter) {
        return presenter;
    }

}
