package com.merlin.network.internal.exception;

/**
 * User: Simon
 * Date: 2016/1/18
 * Desc: 内部运行时异常
 */
public class NestedRuntimeException extends RuntimeException {

    public NestedRuntimeException() {

    }

    public NestedRuntimeException(String exceptionMessage) {
        super(exceptionMessage);

    }

    public NestedRuntimeException(String exceptionMessage, Throwable reason) {
        super(exceptionMessage, reason);

    }

}
