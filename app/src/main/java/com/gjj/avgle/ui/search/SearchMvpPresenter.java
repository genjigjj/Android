package com.gjj.avgle.ui.search;

import com.gjj.avgle.ui.base.MvpPresenter;
import com.gjj.avgle.ui.base.MvpView;

public interface SearchMvpPresenter<V extends MvpView> extends MvpPresenter<V> {

    void showVideo(String query);

    void loadMoreVideo(String query, int pageNo);

    void refreshVideo(String query);


}
