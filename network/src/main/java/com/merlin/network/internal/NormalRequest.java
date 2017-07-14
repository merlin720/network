package com.merlin.network.internal;

import com.google.gson.Gson;
import com.merlin.network.HttpMethod;
import com.merlin.network.beans.NetworkResponse;
import com.merlin.network.beans.Response;
import com.merlin.network.controller.HttpListener;
import com.merlin.network.internal.exception.HttpMessageNotReadableException;
import com.merlin.network.internal.utils.NestLog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * User: Simon
 * Date: 2016/1/20
 * Desc: restful方式请求
 */
public class NormalRequest<T> extends Request<T> {

    private final Map<String, ?> params;

    private final static String BOUNDARY = "----HV2ymHFg03ehbqgZCaKO6jyH";

    public NormalRequest(Class<T> generalClass, HttpMethod method, Map<String, ?> params, String url, HttpListener<T> listener) {
        super(generalClass, method, url, listener);
        this.params = params;
        super.initData();
    }


    @Override
    public Map<String, ?> getParams() {
        return params;
    }

    @Override
    public byte[] writeBody(Map<String, ?> params, String paramsEncoding) {


        return encodeParameters(params, paramsEncoding);
    }

    byte[] encodeParameters(Map<String, ?> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();


        try {
            // 传输内容
            StringBuilder contentBody = new StringBuilder("--" + BOUNDARY);
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                contentBody.append("\r\n")

                        .append("Content-Disposition: form-data; name=\"")

                        .append(URLEncoder.encode(entry.getKey(), paramsEncoding) + "\"")

                        .append("\r\n")

                        .append("\r\n")

                        .append(String.valueOf(entry.getValue()))

                        .append("\r\n")
                        .append("--")
                        .append(BOUNDARY);
            }

            contentBody.append("--\r\n");
            encodedParams.append(contentBody);
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }

    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + BOUNDARY ;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept","application/json");
        headers.put("content-type", "application/json; charset=utf-8");

        return headers;
    }

    @Override
    public Response parse(NetworkResponse response) {

        try {
            if (getGeneralClass().equals(String.class)) {
                return new Response(response.headers, new String(response.data));
            }

            T object = new Gson().fromJson(new String(response.data), getGeneralClass());

            NestLog.e("type >> %s", object.getClass());
            NestLog.e("T type >> %s", getGeneralClass().toString());

            return new Response(response.headers, object);
        } catch (Exception e) {
            throw new HttpMessageNotReadableException("parse error");
        }
    }

}
