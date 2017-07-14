package com.merlin.network;


/**
 * User: Simon
 * Date: 2016/3/7
 * Desc:
 */

public interface CallBack<T> {
    void onResponse(T response);

    void onFailure(Exception exception);
}
