package com.merlin.network.ext;

import android.text.TextUtils;
import android.util.Log;

import com.merlin.network.HttpMethod;
import com.merlin.network.beans.NetworkResponse;
import com.merlin.network.controller.DownloadProgressListener;
import com.merlin.network.controller.INetWork;
import com.merlin.network.internal.FileRequest;
import com.merlin.network.internal.Request;
import com.merlin.network.internal.RestTemplate;
import com.merlin.network.internal.UpLoadRequest;
import com.merlin.network.internal.utils.NestLog;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * User: Simon
 * Date: 2016/1/19
 * Desc: okHttp请求处理器
 */
public class OkHttpStack implements INetWork {


    private FileInterceptor interceptor = new FileInterceptor();

    private OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30,TimeUnit.SECONDS)
            .writeTimeout(30,TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build();

    public static class FileInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            //拦截
            Response originalResponse = chain.proceed(chain.request());

            if (request instanceof FileRequest) {
                if (request.getListener() instanceof DownloadProgressListener) {

                    //包装响应体并返回
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), (DownloadProgressListener) request.getListener()))
                            .build();
                }
            }
            return originalResponse;
        }


        Request request;


        public void setRequest(Request request) {
            this.request = request;
        }
    }

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    @Override
    public NetworkResponse performRequest(final Request<?> request) throws Exception {
        try {
            long startTime = System.currentTimeMillis();
            NestLog.e("request url >>%d -- %s ", request.getSequence(), request.getUrl());
            if (request.getBody() != null)
                NestLog.e("request params >>%d --  %s", request.getSequence(), new String(request.getBody()));
            NestLog.e("request method >>%d --  %s", request.getSequence(), request.getMethod().name());
            NestLog.e("request contentType >>%d --  %s", request.getSequence(), request.getBodyContentType());

            okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
            builder.url(request.getUrl());
            RequestBody requestBody = null;
            if (request instanceof UpLoadRequest) {
                MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                Iterator iter = request.getParams().entrySet().iterator();
                if (request.mPath != null && request.mPath.size()>0) {
                    for (String s : request.mPath) {
                        if (!TextUtils.isEmpty(s)) {
                            NestLog.v("merlin_____________UpLoadRequest" + request.mPath);
                            File file = new File(s);
                            Log.i("imageName:", file.getName());
                            mbody.addFormDataPart("img", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
                        }
                    }

                }

                while (iter.hasNext()) {
                    HashMap.Entry<String, String> entry = (HashMap.Entry<String, String>) iter.next();
                    mbody.addFormDataPart(entry.getKey(), entry.getValue());

                }
                requestBody = mbody.build();
            } else {
                if (HttpMethod.permitsRequestBody(request.getMethod().name())) {
                    requestBody = RequestBody.create(MediaType.parse(request.getBodyContentType()), request.getBody());
                }
            }
            Map<String, String> headers = request.getHeaders();
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            interceptor.setRequest(request);
            builder.method(request.getMethod().name(), requestBody);
            Call call = httpClient.newCall(builder.build());
            okhttp3.Response response = call.execute();
            NestLog.e("request take time millions >>%d -- %d", request.getSequence(), System.currentTimeMillis() - startTime);
            return new NetworkResponse(response.code(), response.body().bytes(), toMap(response.headers()), response.body());
        } catch (Exception e) {
            NestLog.e("%s", e.toString());
            e.printStackTrace();
            if(e instanceof SocketTimeoutException) {
                throw new SocketTimeoutException();
            }else{
                throw new Exception();
            }
        }

    }


    @Override
    public void addRestTemplate(RestTemplate restTemplate) {

    }

    private static Map<String, String> toMap(Headers headers) {
        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < headers.size(); i++) {
            map.put(headers.name(i), headers.value(i));
        }
        return map;
    }
}
