package com.gjj.avgle.net.model;

public class CollectionResponse {

    private int status;

    private int total;

    private int[] vidList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int[] getVidList() {
        return vidList;
    }

    public void setVidList(int[] vidList) {
        this.vidList = vidList;
    }
}
