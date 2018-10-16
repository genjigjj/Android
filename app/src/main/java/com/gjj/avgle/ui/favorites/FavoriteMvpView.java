package com.gjj.avgle.ui.favorites;

import com.gjj.avgle.net.model.AvgleResponse;
import com.gjj.avgle.ui.base.MvpView;

public interface FavoriteMvpView extends MvpView {

    void resetAdapter();

    void addResponse(AvgleResponse response);

    void finishLoadMore();

    void finishRefresh();

}
