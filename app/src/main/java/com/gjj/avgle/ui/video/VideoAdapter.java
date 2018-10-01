/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.gjj.avgle.ui.video;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.avgle.R;
import com.gjj.avgle.net.model.AvgleResponse;
import com.gjj.avgle.net.model.Video;
import com.gjj.avgle.ui.base.BaseViewHolder;
import com.gjj.avgle.ui.play.PlayActivity;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Janisharali on 25-05-2017.
 */

public class VideoAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;

    private Callback mCallback;

    private List<Video> videoList;

    private AvgleResponse response;

    private Context context;

    public VideoAdapter(List<Video> videoList) {
        this.videoList = videoList;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_view, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (videoList != null && videoList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (videoList != null && videoList.size() > 0) {
            return videoList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<Video> videoList) {
        if (videoList != null && videoList.size() > 0) {
            int position = getItemCount();
            this.videoList.addAll(videoList);
            notifyItemInserted(position);
        }
    }

    public void reset() {
        this.videoList.clear();
    }

    public interface Callback {
        void onBlogEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.cover_image_view)
        ImageView coverImageView;

        @BindView(R.id.title_text_view)
        TextView titleTextView;

        @BindView(R.id.duration_text_view)
        TextView durationTextView;

        @BindView(R.id.date_text_view)
        TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
            coverImageView.setImageDrawable(null);
            titleTextView.setText("");
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            if (videoList != null && videoList.size() != 0) {
                final Video video = videoList.get(position);
                if (video != null) {
                    Glide.with(itemView.getContext())
                            .load(video.getPreview_url())
                            .into(coverImageView);
                    if (video.getTitle() != null) {
                        titleTextView.setText(video.getTitle());
                    }

                    durationTextView.setText(String.valueOf(video.getDuration()));

                    dateTextView.setText(String.valueOf(video.getAddtime()));

                }

                itemView.setOnClickListener((View v) -> {
                    if (Objects.requireNonNull(video).getVid() != null) {
                        try {
                            Intent intent = new Intent(itemView.getContext(), PlayActivity.class);
                            intent.putExtra("vid", video.getVid());
                            context.startActivity(intent);
                        } catch (Exception e) {
                            Log.d("url error", e.getMessage());
                        }
                    }
                });
            }
        }
    }

    public class EmptyViewHolder extends BaseViewHolder {

        @BindView(R.id.btn_retry)
        Button retryButton;

        @BindView(R.id.tv_message)
        TextView messageTextView;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {

        }

        @OnClick(R.id.btn_retry)
        void onRetryClick() {
            if (mCallback != null)
                mCallback.onBlogEmptyViewRetryClick();
        }
    }

    public AvgleResponse getResponse() {
        return response;
    }

    public void setResponse(AvgleResponse response) {
        this.response = response;
    }
}
