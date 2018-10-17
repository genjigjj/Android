package com.gjj.avgle.ui.favorites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gjj.avgle.R;
import com.gjj.avgle.adapter.VideoAdapter;
import com.gjj.avgle.di.component.ActivityComponent;
import com.gjj.avgle.net.model.AvgleResponse;
import com.gjj.avgle.ui.base.BaseFragment;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class FavoriteFragment extends BaseFragment implements FavoriteMvpView {

    public static final String TAG = "FavoriteFragment";

    @Inject
    VideoAdapter videoAdapter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.video_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    protected RefreshLayout refreshLayout;

    @Inject
    FavoriteMvpPresenter<FavoriteMvpView> mvpPresenter;

    public static FavoriteFragment newInstance() {
        Bundle args = new Bundle();
        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mvpPresenter.onAttach(this);
        }
        return view;
    }

    @Override
    protected void setUp(View view) {
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemAnimator animator = new SlideInUpAnimator();
        animator.setAddDuration(300);
        mRecyclerView.setItemAnimator(animator);
        mRecyclerView.setAdapter(videoAdapter);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(refreshLayout -> mvpPresenter.refreshVideo());
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (videoAdapter.getItemCount() == videoAdapter.getResponse().getTotal_videos()) {
                finishLoadMore();
                showMessage("暂无更多视频");
            } else {
                mvpPresenter.loadMoreVideo(videoAdapter.getItemCount() / 10);
            }
        });
        mvpPresenter.showVideo();
    }

    @Override
    public void onDestroyView() {
        mvpPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void resetAdapter() {
        videoAdapter.reset();
    }

    @Override
    public void addResponse(AvgleResponse response) {
        videoAdapter.addItems(response.getVideos());
        videoAdapter.setResponse(response);
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
    public void onError(String message) {
        super.onError(message);
        videoAdapter.reset();
        videoAdapter.notifyDataSetChanged();
        refreshLayout.finishRefresh();
    }

}
