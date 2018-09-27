package com.gjj.avgle.net.api;

import com.gjj.avgle.net.model.VideoResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface AvgleServiceApi {

    @GET("v1/videos/{page}")
    Observable<VideoResponse> getVideos(@Path("page") int page);

    /**
     * 获取视频播放地址
     *
     * @param url 连接
     * @return ob
     */
    @GET
    Observable<Response<ResponseBody>> getPlayVideoUrl(@Url String url);
}
