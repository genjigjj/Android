package com.gjj.avgle.net.api;

import com.gjj.avgle.net.model.CollectionResponse;
import com.gjj.avgle.net.model.VideoDetail;
import com.gjj.avgle.net.model.VideoResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface AvgleServiceApi {

    @GET("v1/videos/{page}?limit=10")
    Observable<VideoResponse> getVideos(@Path("page") int page, @Query("c") String c);

    /**
     * 获取视频播放地址
     *
     * @param url 连接
     * @return ob
     */
    @GET
    Observable<Response<ResponseBody>> getPlayVideoUrl(@Url String url);

    /**
     * 搜索视频
     *
     * @param query 查询条件
     * @param page  页数
     * @return 搜索结果
     */
    @GET("v1/search/{query}/{page}?limit=10")
    Observable<VideoResponse> searchVideo(@Path("query") String query, @Path("page") int page);

    /**
     * 获取收藏列表
     *
     * @param url   url
     * @param start 开始
     * @param end   结束
     * @return 收藏列表
     */
    @GET
    Observable<CollectionResponse> getFavorites(@Url String url, @Query("start") int start, @Query("end") int end);

    /**
     * 获取视频详情
     *
     * @param vid 视频id
     * @return 视频详情
     */
    @GET("v1/video/{vid}")
    Observable<VideoDetail> getDetail(@Path("vid") int vid);

}
