package com.merlin.network;

/**
 * User: Simon
 * Date: 2016/1/13
 * Desc: 网络请求方式
 */
public enum  HttpMethod {

    GET, POST, HEAD, OPTIONS, PUT, PATCH, DELETE, TRACE;

    public static boolean permitsRequestBody(String method) {
        return requiresRequestBody(method) || method.equals("OPTIONS") || method.equals("DELETE") || method.equals("PROPFIND") || method.equals("MKCOL") || method.equals("LOCK");
    }

    public static boolean requiresRequestBody(String method) {
        return method.equals("POST") || method.equals("PUT") || method.equals("PATCH") || method.equals("PROPPATCH") || method.equals("REPORT");
    }
}
