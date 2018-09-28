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

package com.gjj.avgle.ui.base;

/**
 * Created by janisharali on 27/01/17.
 */

import com.gjj.avgle.net.ApiHelper;
import com.gjj.avgle.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * onAttach() and onDetach(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private static final String TAG = "BasePresenter";

    private final CompositeDisposable mCompositeDisposable;

    private final ApiHelper apiHelper;

    private final SchedulerProvider schedulerProvider;

    private V mMvpView;

    @Inject
    public BasePresenter(CompositeDisposable compositeDisposable, ApiHelper apiHelper, SchedulerProvider schedulerProvider) {
        this.mCompositeDisposable = compositeDisposable;
        this.apiHelper = apiHelper;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mCompositeDisposable.dispose();
        mMvpView = null;
    }


    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }


    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public ApiHelper getAppApiHelper() {
        return apiHelper;
    }

    public SchedulerProvider getSchedulerProvider() {
        return schedulerProvider;
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
