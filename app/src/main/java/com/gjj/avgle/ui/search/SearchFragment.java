package com.gjj.avgle.ui.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gjj.avgle.R;
import com.gjj.avgle.adapter.VideoAdapter;
import com.gjj.avgle.di.component.ActivityComponent;
import com.gjj.avgle.net.model.VideoResponse;
import com.gjj.avgle.ui.base.BaseFragment;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class SearchFragment extends BaseFragment implements SearchMvpView, VideoAdapter.Callback {

    public static final String TAG = "SearchFragment";

    private String query;

    @Inject
    VideoAdapter videoAdapter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.video_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    protected RefreshLayout refreshLayout;

    @Inject
    SearchMvpPresenter<SearchMvpView> searchMvpPresenter;

    public static SearchFragment newInstance(Bundle bundle) {
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        if (getArguments() != null) {
            query = getArguments().getString("query");
        }
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            searchMvpPresenter.onAttach(this);
            videoAdapter.setCallback(this);
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
        refreshLayout.setOnRefreshListener(refreshLayout -> searchMvpPresenter.refreshVideo(query));
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (videoAdapter.getResponse().isHas_more()) {
                searchMvpPresenter.loadMoreVideo(query, videoAdapter.getResponse().getCurrent_offset() / 10 + 1);
            } else {
                finishLoadMore();
                showMessage("暂无更多视频");
            }
        });
        searchMvpPresenter.showVideo(query);
    }

    @Override
    public void onDestroyView() {
        searchMvpPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onBlogEmptyViewRetryClick() {
        showLoading();
        searchMvpPresenter.refreshVideo(query);
    }

    @Override
    public void resetAdapter() {
        videoAdapter.reset();
    }

    @Override
    public void addResponse(VideoResponse response) {
        videoAdapter.addItems(response.getResponse().getVideos());
        videoAdapter.setResponse(response.getResponse());
    }

    @Override
    public void finishLoadMore() {
        refreshLayout.finishLoadMore();
    }

    @Override
    public void finishRefresh() {
        if (refreshLayout.getState() == RefreshState.Refreshing) {
            refreshLayout.finishRefresh();
        }
    }

    @Override
    public void onError(String message) {
        super.onError(message);
        videoAdapter.reset();
        videoAdapter.notifyDataSetChanged();
        refreshLayout.finishRefresh();
    }

}
