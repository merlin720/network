package com.merlin.network.controller;

/**
 * User: Simon
 * Date: 2016-03-09
 * Time: 22:07
 * Desc:
 */
public interface DownloadProgressListener<T> extends HttpListener<T> {

    void onProgressUpdate(long bytesRead, long contentLength, boolean done);
}
