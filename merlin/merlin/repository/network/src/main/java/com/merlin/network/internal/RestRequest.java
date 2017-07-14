package com.merlin.network.internal;

import com.google.gson.Gson;
import com.merlin.network.HttpMethod;
import com.merlin.network.beans.NetworkResponse;
import com.merlin.network.beans.Response;
import com.merlin.network.controller.HttpListener;
import com.merlin.network.internal.exception.HttpMessageNotReadableException;
import com.merlin.network.internal.exception.HttpMessageUnspectException;
import com.merlin.network.internal.utils.NestLog;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Simon
 * Date: 2016/1/20
 * Desc: restful方式请求
 */
public class RestRequest<T> extends Request<T> {

    private final Map<String, ?> params;

    private Gson gson = new Gson();

    public RestRequest(Class<T> generalClass, HttpMethod method, Map<String, ?> params, String url, HttpListener<T> listener) {
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

        String json = gson.toJson(params);
        try {
            return json.getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public Response parse(NetworkResponse response) {

        try {
            if (getGeneralClass().equals(String.class)) {
                return new Response(response.headers, new String(response.data));
            }

            NestLog.e("T type >> %s", getGeneralClass().toString());
            String respStr = new String(response.data);
            if(respStr.length() == 0 || "null".equals(respStr)){
                throw new HttpMessageUnspectException("response null");
            }
            T object = new Gson().fromJson(respStr, getGeneralClass());
            NestLog.e("type >> %s", object.getClass());
            return new Response(response.headers, object);
        } catch (Exception e) {
            if(e instanceof HttpMessageUnspectException){
                throw e;
            }else{
                throw new HttpMessageNotReadableException("parse error");
            }

        }
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept","application/json");
        headers.put("content-type", "application/json; charset=utf-8");

        return headers;
    }

}
