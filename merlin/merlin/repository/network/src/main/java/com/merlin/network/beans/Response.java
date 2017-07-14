package com.merlin.network.beans;


import java.io.Serializable;
import java.util.Map;

/**
 * User: Simon
 * Date: 2016/1/19
 * Desc: 解析之后的响应体
 */
public class Response implements Serializable{

    private Object responseBody;

    private Map<String,String> header;

    public Response(Map<String, String> header, Object responseBody) {
        this.header = header;
        this.responseBody = responseBody;
    }

    public Object getResponseBody() {
        return responseBody;
    }

    public Map<String, String> getHeader() {
        return header;
    }
}
