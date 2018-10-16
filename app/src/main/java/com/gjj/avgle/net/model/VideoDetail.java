package com.gjj.avgle.net.model;

public class VideoDetail {

    private boolean success;

    private DetailResponse response;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DetailResponse getResponse() {
        return response;
    }

    public void setResponse(DetailResponse response) {
        this.response = response;
    }
}
