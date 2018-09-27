package com.gjj.avgle.ui.video;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gjj.avgle.R;
import com.gjj.avgle.di.component.ActivityComponent;
import com.gjj.avgle.net.model.Video;
import com.gjj.avgle.ui.base.BaseFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoFragment extends BaseFragment implements VideoMvpView {

    public static final String TAG = "VideoFragment";

    @Inject
    VideoAdapter videoAdapter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.video_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    protected SwipeRefreshLayout mRefreshLayout;

    @Inject
    VideoMvpPresenter<VideoMvpView> videoMvpPresenter;

    private int mLastVisibleItemPosition;

    public static VideoFragment newInstance() {
        Bundle args = new Bundle();
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            videoMvpPresenter.onAttach(this);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this.getContext(), R.color.google_blue),
                ContextCompat.getColor(this.getContext(), R.color.google_green),
                ContextCompat.getColor(this.getContext(), R.color.google_red),
                ContextCompat.getColor(this.getContext(), R.color.google_yellow)
        );
    }

    @Override
    protected void setUp(View view) {
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(videoAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }
                if (videoAdapter != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                            && mLastVisibleItemPosition + 1 == videoAdapter.getItemCount()) {
                        //发送网络请求获取更多数据
                        Log.d(TAG, "position: "+(mLastVisibleItemPosition + 1) / 10 );
                        videoMvpPresenter.sendMoreRequest((mLastVisibleItemPosition + 1) / 10);
                    }
                }
            }
        });
        mRefreshLayout.setOnRefreshListener(() -> videoMvpPresenter.refresh());
        videoMvpPresenter.onViewPrepared();
    }


    @Override
    public void onDestroyView() {
        videoMvpPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void updateVideo(List<Video> videoList) {
        videoAdapter.addItems(videoList);
    }

    @Override
    public void refreshVideo() {
        videoAdapter.reset();
        mRefreshLayout.setRefreshing(false);
    }
}
