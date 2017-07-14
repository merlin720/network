package com.merlin.network.internal.exception;

import com.merlin.network.beans.NetworkResponse;

/**
 * User: Simon
 * Date: 2016/1/18
 * Desc: 客户端运行时异常
 */
public class RestClientException extends NestedRuntimeException {

    public final NetworkResponse networkResponse;

    public RestClientException() {
        networkResponse = null;
    }

    public RestClientException(String exceptionMessage) {
        super(exceptionMessage);
        networkResponse = null;
    }

    public RestClientException(NetworkResponse response) {
        super();
        networkResponse = response;
    }


}
