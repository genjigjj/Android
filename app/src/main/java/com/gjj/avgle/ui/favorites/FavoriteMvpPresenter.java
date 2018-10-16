package com.gjj.avgle.ui.favorites;

import com.gjj.avgle.ui.base.MvpPresenter;
import com.gjj.avgle.ui.base.MvpView;

public interface FavoriteMvpPresenter<V extends MvpView> extends MvpPresenter<V> {

    void showVideo();

    void loadMoreVideo(int pageNo);

    void refreshVideo();

}
