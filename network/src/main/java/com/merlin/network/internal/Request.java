/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.merlin.network.internal;


import android.net.Uri;
import android.text.TextUtils;

import com.merlin.network.HttpMethod;
import com.merlin.network.controller.HttpListener;
import com.merlin.network.controller.ICacheController;
import com.merlin.network.controller.IRequestQueueController;
import com.merlin.network.controller.IResponseParser;
import com.merlin.network.internal.utils.NestLog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Request<T> implements Comparable<Request>, IResponseParser {

    public List<String> mPath;

    /**
     * 默认参数编码
     */
    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

    /**
     * 请求的url
     */
    private final String mUrl;

    /**
     * 请求的方法
     */
    private final HttpMethod mMethod;

    /**
     * 唯一的身份标识
     */
    private String mIdentifier;

    /**
     * 传送标识
     */
    private final int mDefaultTrafficStatsTag;

    /**
     * 网络请求回调接口
     */
    private HttpListener<T> mHttpListener;

    /**
     * 生成的请求序号
     */
    private Integer mSequence;

    /**
     * 请求队列
     */
    private IRequestQueueController messageQueueController;

    /**
     * 请求是否取消
     */
    private boolean mCanceled = false;

    /**
     * 响应结果是否分发
     */
    private boolean mResponseDelivered = false;

    /**
     * 自定义标记
     */
    private Object mTag = new Object();

    /**
     * 是否需要缓存
     */
    private boolean isShouldCache = false;

    /**
     * 缓存的结果
     */
    private ICacheController.Entry mCacheEntry = null;

    /**
     * 日志管理器
     */
    private final NestLog.MarkerLog mEventLog = NestLog.MarkerLog.ENABLED ? new NestLog.MarkerLog() : null;

    private String threeVerTag;

    private Class<T> mGeneralClass;

    public HttpListener<T> getListener() {
        return mHttpListener;
    }

    public Request(Class<T> generalClass, HttpMethod method, String url, HttpListener<T> listener) {
        mGeneralClass = generalClass;
        mMethod = method;
        mUrl = url;
        mIdentifier = createIdentifier(method, url);
        mHttpListener = listener;
        mDefaultTrafficStatsTag = findDefaultTrafficStatsTag(url);

    }


    public void initData() {
        threeVerTag = createRequestTag(getMethod(), getUrl(), getParams());
    }

    public HttpMethod getMethod() {
        return mMethod;
    }

    public Request setTag(Object tag) {
        mTag = tag;
        return this;
    }

    public boolean isShouldCache() {
        return isShouldCache;
    }

    public void setIsShouldCache(boolean isShouldCache) {
        this.isShouldCache = isShouldCache;
    }


    public String getCacheKey() {
        return mMethod + ":" + mUrl;
    }

    public Object getTag() {
        return mTag;
    }

    public int getTrafficStatsTag() {
        return mDefaultTrafficStatsTag;
    }

    private static int findDefaultTrafficStatsTag(String url) {
        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            String host = uri.getHost();
            if (host != null) {
                return host.hashCode();
            }
        }
        return 0;
    }

    public void finish() {
        if (messageQueueController != null) {
            messageQueueController.finish(this);
            onFinish();
        }

    }

    protected void onFinish() {
        mHttpListener = null;
    }

    public Request setRequestQueue(IRequestQueueController messageQueueController) {
        this.messageQueueController = messageQueueController;
        return this;
    }

    public final Request setSequence(int sequence) {
        mSequence = sequence;
        return this;
    }

    public final int getSequence() {
        if (mSequence == null) {
            throw new IllegalStateException("getSequence called before setSequence");
        }
        return mSequence;
    }

    public String getUrl() {
        String url = mUrl;
        if (getMethod() == HttpMethod.GET) {
            byte[] body = new byte[0];
            try {
                body = getBody();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (body != null) {
                url = url + "?" + new String(body);
            }
        }

        return url;
    }

    public String getIdentifier() {
        return mIdentifier;
    }


    public void cancel() {
        mCanceled = true;
    }


    public boolean isCanceled() {
        return mCanceled;
    }

    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }


    public Map<String, ?> getParams() {

        return new HashMap<>();
    }

    protected String getParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }


    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
    }

    public String getThreeVerTag() {
        return this.threeVerTag;
    }

    public byte[] getBody() {
        Map<String, Object> params = (Map<String, Object>) getParams();

        if (params == null) {
            params = new HashMap<>();
        }


        if (HttpMethod.permitsRequestBody(getMethod().name())) {
            return writeBody(params, getParamsEncoding());
        }
        return encodeParameters(params, getParamsEncoding());

    }

    byte[] encodeParameters(Map<String, ?> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            int i = 0;
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(String.valueOf(entry.getValue()));
                if (i < params.size() - 1) {
                    encodedParams.append('&');
                }

                i++;
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }


    public abstract byte[] writeBody(Map<String, ?> params, String paramsEncoding);


    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }

    public Priority getPriority() {
        return Priority.NORMAL;
    }


    public void markDelivered() {
        mResponseDelivered = true;
    }


    public void deliverResponse(T response) {
        if (mHttpListener != null) {
            mHttpListener.onResponse(response);
        }
    }

    public void deliverError(Exception error) {
        if (mHttpListener != null) {
            mHttpListener.onErrorResponse(error);
        }
    }

    @Override
    public int compareTo(Request other) {
        Priority left = this.getPriority();
        Priority right = other.getPriority();

        return left == right ?
                this.mSequence - other.mSequence :
                right.ordinal() - left.ordinal();
    }


    private static long sCounter;

    private static String createIdentifier(final HttpMethod method, final String url) {
        return InternalUtils.sha1Hash("Request:" + method.toString() + ":" + url +
                ":" + System.currentTimeMillis() + ":" + (sCounter++));
    }

    private static String createRequestTag(final HttpMethod method, final String url, final Map<String, ?> params) {
        StringBuilder encodedParams = new StringBuilder();

        if (params != null) {
            int i = 0;
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                try {
                    encodedParams.append(URLEncoder.encode(entry.getKey(), DEFAULT_PARAMS_ENCODING));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                encodedParams.append('=');
                encodedParams.append(String.valueOf(entry.getValue()));
                if (i < params.size() - 1) {
                    encodedParams.append('&');
                }

                i++;
            }
        }

        NestLog.e("tag: %s", method.toString() + url + encodedParams.toString());
        return InternalUtils.sha1Hash(method.toString() + url + encodedParams.toString());
    }

    public void addMarker(String tag) {
        if (NestLog.MarkerLog.ENABLED) {
            mEventLog.add(tag, Thread.currentThread().getId());
        }
    }

    public Request<?> setCacheEntry(ICacheController.Entry entry) {
        mCacheEntry = entry;
        return this;
    }

    public ICacheController.Entry getCacheEntry() {
        return mCacheEntry;
    }


    public Class<T> getGeneralClass() {
        return mGeneralClass;
    }
}
