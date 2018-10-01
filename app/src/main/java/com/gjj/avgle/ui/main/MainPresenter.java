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

package com.gjj.avgle.ui.main;

import com.gjj.avgle.net.ApiHelper;
import com.gjj.avgle.ui.base.BasePresenter;
import com.gjj.avgle.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


/**
 * Created by janisharali on 27/01/17.
 */

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V>
        implements MainMvpPresenter<V> {

    private static final String TAG = "MainPresenter";

    @Inject
    public MainPresenter(CompositeDisposable compositeDisposable, ApiHelper apiHelper, SchedulerProvider schedulerProvider) {
        super(compositeDisposable, apiHelper, schedulerProvider);
    }

    @Override
    public void onDrawerOptionAboutClick() {
        getMvpView().closeNavigationDrawer();
        getMvpView().showVideoFragment();
    }

    @Override
    public void onDrawerRateUsClick() {
        getMvpView().closeNavigationDrawer();
        getMvpView().showRateUsDialog();
    }

    @Override
    public void onDrawerMyFeedClick() {
        getMvpView().closeNavigationDrawer();
        getMvpView().openMyFeedActivity();
    }

    @Override
    public void onDrawerHomeClick() {
        getMvpView().closeNavigationDrawer();
        getMvpView().showVideoFragment();
    }

}
