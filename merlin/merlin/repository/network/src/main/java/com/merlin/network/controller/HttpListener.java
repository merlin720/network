package com.merlin.network.controller;

/**
 * User: Simon
 * Date: 2016/1/19
 * Desc: 网络请求监听器
 */
public interface HttpListener<T> {

    void onResponse(T response);

    void onErrorResponse(Exception error);
}
