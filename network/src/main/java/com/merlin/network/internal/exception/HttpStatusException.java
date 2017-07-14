package com.merlin.network.internal.exception;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * User: Simon
 * Date: 2016/1/20
 * Desc: 网络异常
 */
public class HttpStatusException extends RestClientException {


    private static final String DEFAULT_CHARSET = "ISO-8859-1";


    private final int statusCode;

    private final byte[] responseBody;

    private final Map<String,String> responseHeaders;

    private final String responseCharset;


    public HttpStatusException(int statusCode, byte[] responseBody, Map<String,String> responseHeaders) {
        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.responseHeaders = responseHeaders;
        this.responseCharset = DEFAULT_CHARSET;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public Map<String,String>  getResponseHeaders() {
        return responseHeaders;
    }

    public String getResponseCharset() {
        return responseCharset;
    }

    public String getResponseBodyAsString() {
        try {
            return new String(this.responseBody, this.responseCharset);
        }
        catch (UnsupportedEncodingException ex) {
            // should not occur
            throw new IllegalStateException(ex);
        }
    }

}
