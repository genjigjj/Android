package com.gjj.avgle.ui.video;

import com.gjj.avgle.net.model.VideoResponse;
import com.gjj.avgle.ui.base.MvpView;

public interface VideoMvpView extends MvpView {

    void resetAdapter();

    void addResponse(VideoResponse response);

    void finishLoadMore();

    void finishRefresh();

}
