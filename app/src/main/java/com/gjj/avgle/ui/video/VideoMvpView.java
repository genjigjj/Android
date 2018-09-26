package com.gjj.avgle.ui.video;

import com.gjj.avgle.net.model.Video;
import com.gjj.avgle.ui.base.MvpView;

import java.util.List;

public interface VideoMvpView extends MvpView {

    void updateVideo(List<Video> videoList);

    void refreshVideo();

}
