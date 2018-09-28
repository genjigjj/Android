package com.gjj.avgle.ui.video;

import com.gjj.avgle.net.model.Video;
import com.gjj.avgle.ui.base.MvpView;

import java.util.List;

public interface VideoMvpView extends MvpView {

    void resetAdapter();

    void addItem(List<Video> videoList);

    void finishLoadMore();

    void finishRefresh();

}
