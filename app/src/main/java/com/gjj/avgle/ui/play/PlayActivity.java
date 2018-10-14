package com.gjj.avgle.ui.play;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.gjj.avgle.R;
import com.gjj.avgle.exoplayer.widget.ExoVideoControlsMobile;
import com.gjj.avgle.exoplayer.widget.ExoVideoView;
import com.gjj.avgle.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayActivity extends BaseActivity implements PlayMvpView, OnPreparedListener {

    @Inject
    PlayMvpPresenter<PlayMvpView> mvpPresenter;

    @BindView(R.id.video_view)
    ExoVideoView videoView;

    private ExoVideoControlsMobile videoControlsMobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mvpPresenter.onAttach(this);
        setUp();
    }

    @Override
    protected void setUp() {
        videoView.setOnPreparedListener(this);
        videoControlsMobile = (ExoVideoControlsMobile) videoView.getVideoControls();
        videoControlsMobile.setOnBackButtonClickListener(view -> onBackPressed());
        String vid = getIntent().getStringExtra("vid");
        String title = getIntent().getStringExtra("title");
        videoControlsMobile.setTitle(title);
        mvpPresenter.getPlayVideoUrl(vid);
    }

    @Override
    public void onPrepared() {
        videoView.start();
    }

    @Override
    public void playVideo(String url) {
        videoView.setVideoURI(Uri.parse(url));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!videoView.isPlaying()) {
            videoView.start();
        }
    }

    @Override
    protected void onPause() {
        videoView.pause();
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        if (videoControlsMobile.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        videoView.release();
        super.onDestroy();
    }
}
