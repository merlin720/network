package com.merlin.network.controller;


import com.merlin.network.beans.NetworkResponse;
import com.merlin.network.internal.Request;
import com.merlin.network.internal.RestTemplate;
import com.merlin.network.internal.exception.RestClientException;

/**
 * User: Simon
 * Date: 2016/1/19
 * Desc: 消息分发器
 */
public interface IDeliveryController {


    /**
     * 分发响应
     *
     * @param request 请求体
     * @param response 响应体
     * @param <T> 返回对应的格式
     */
    void postResponse(Request request, NetworkResponse response);

    /**
     * 分发错误
     *
     * 出现异常不会分发响应
     *
     * @param request 请求体
     * @param error 返回相应的处理异常
     */
    void postError(Request request, Exception error);


    void addRestTemplate(RestTemplate restTemplate);
}
