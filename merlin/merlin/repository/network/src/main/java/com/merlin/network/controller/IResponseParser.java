package com.merlin.network.controller;

import com.merlin.network.beans.NetworkResponse;
import com.merlin.network.beans.Response;
import com.merlin.network.internal.exception.HttpMessageNotReadableException;


/**
 * User: Simon
 * Date: 2016/1/19
 * Desc: 响应解析器
 */
public interface IResponseParser {

    Response parse(NetworkResponse response) throws HttpMessageNotReadableException;

}
