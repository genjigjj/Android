package com.gjj.avgle.ui.play;

import com.gjj.avgle.ui.base.MvpPresenter;

public interface PlayMvpPresenter<V extends PlayMvpView> extends MvpPresenter<V> {
    void getPlayVideoUrl(String vid);
}
