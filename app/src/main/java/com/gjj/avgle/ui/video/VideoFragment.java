package com.gjj.avgle.ui.video;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gjj.avgle.R;
import com.gjj.avgle.di.component.ActivityComponent;
import com.gjj.avgle.net.model.Video;
import com.gjj.avgle.ui.base.BaseFragment;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoFragment extends BaseFragment implements VideoMvpView,VideoAdapter.Callback {

    public static final String TAG = "VideoFragment";

    @Inject
    VideoAdapter videoAdapter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.video_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    protected RefreshLayout refreshLayout;

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
            videoAdapter.setCallback(this);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setRefreshHeader(new BezierRadarHeader(getContext()).setEnableHorizontalDrag(true));
        //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(new BallPulseFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale));
    }

    @Override
    protected void setUp(View view) {
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(videoAdapter);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            videoAdapter.reset();
            videoMvpPresenter.refreshVideo();
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> videoMvpPresenter.loadMoreVideo((videoAdapter.getItemCount()) / 10));
        videoMvpPresenter.showVideo();
    }


    @Override
    public void onDestroyView() {
        videoMvpPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void resetAdapter() {
        videoAdapter.reset();
    }

    @Override
    public void addItem(List<Video> videoList) {
        videoAdapter.addItems(videoList);
    }

    @Override
    public void finishLoadMore() {
        refreshLayout.finishLoadMore();
    }

    @Override
    public void finishRefresh() {
        refreshLayout.finishRefresh();
    }

    @Override
    public void onBlogEmptyViewRetryClick() {
        videoMvpPresenter.refreshVideo();
    }

    @Override
    public void onError(String message) {
        super.onError(message);
        videoAdapter.reset();
        videoAdapter.notifyDataSetChanged();
        refreshLayout.finishRefresh();
    }
}
