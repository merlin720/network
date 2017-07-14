package com.merlin.network.internal.exception;

/**
 * User: Simon
 * Date: 2016/6/14
 * Desc: 无法预料的问题
 */
public class HttpMessageUnspectException extends  RestClientException {

    public HttpMessageUnspectException(String exceptionMessage) {
        super(exceptionMessage);
    }

}

