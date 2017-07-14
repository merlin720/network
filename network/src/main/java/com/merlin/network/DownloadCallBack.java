package com.merlin.network;

/**
 * User: Simon
 * Date: 2016/6/13
 * Desc: 下载回调
 */
public interface DownloadCallBack<T> extends CallBack<T> {
    void onProgressUpdate(long bytesRead, long contentLength, boolean done);
}
