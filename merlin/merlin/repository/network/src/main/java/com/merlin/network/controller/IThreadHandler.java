package com.merlin.network.controller;

import com.merlin.network.internal.RestTemplate;

/**
 * User: Simon
 * Date: 2016/1/19
 * Desc: 线程管理器
 */
public interface IThreadHandler {

    void start();

    void stop();

    void addRestTemplate(RestTemplate restTemplate);
}
