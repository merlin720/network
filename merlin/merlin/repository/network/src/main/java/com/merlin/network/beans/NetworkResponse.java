package com.merlin.network.beans;


import com.merlin.network.HttpStatus;
import com.merlin.network.controller.ICacheController;
import com.merlin.network.internal.exception.HttpStatusException;
import com.merlin.network.internal.utils.NestLog;

import java.io.Serializable;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * User: Simon
 * Date: 2016/1/19
 * Desc: 网络请求响应
 */
public class NetworkResponse implements Serializable{

    public NetworkResponse(ICacheController.Entry entry) {
        this(HttpStatus.SC_OK, entry.data, entry.responseHeaders, null);
    }

    public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers, ResponseBody responseBody) {
        this(statusCode, data, headers);
        this.responseBody = responseBody;
    }
    public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
        NestLog.e("statusCode >> %s", statusCode);
        if (statusCode != 200) {
            throw new HttpStatusException(statusCode, data, headers);
        }
        NestLog.e("response >> %s", new String(data));

    }
    /**
     * http状态码
     */
    public final int statusCode;

    /**
     * 字节码响应body
     */
    public final byte[] data;

    /**
     * 响应头
     */
    public final Map<String, String> headers;

    public ResponseBody responseBody;

}
