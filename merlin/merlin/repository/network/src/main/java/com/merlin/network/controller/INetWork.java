package com.merlin.network.controller;


import com.merlin.network.beans.NetworkResponse;
import com.merlin.network.internal.Request;
import com.merlin.network.internal.RestTemplate;

/**
 * User: Simon
 * Date: 2016/1/19
 * Desc: 网络请求处理器接口
 * 用于创建链接，以及处理返回结果
 *
 */
public interface INetWork {

    NetworkResponse performRequest(Request<?> request)
            throws Exception;


    void addRestTemplate(RestTemplate restTemplate);

}
