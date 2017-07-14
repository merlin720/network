package com.merlin.network.ext;

import com.merlin.network.controller.DownloadProgressListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * User: Simon
 * Date: 2016/6/13
 * Desc: 响应进度
 */
public class ProgressResponseBody extends ResponseBody {

    //实际的待包装请求体
    private final ResponseBody responseBody;
    //进度回调接口
    private final DownloadProgressListener progressListener;
    //包装完成的BufferedSource
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, DownloadProgressListener progressListener){
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }



    /**
     * 重写进行包装source
     * @return BufferedSource
     * @throws IOException 异常
     */
    @Override public BufferedSource source(){
        if (bufferedSource == null) {
            //包装
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    /**
     /**
     * 读取，回调进度接口
     * @param source Source
     * @return Source
     */
    private Source source(Source source) {

        return new ForwardingSource(source) {
            //当前读取字节数
            long totalBytesRead = 0L;
            @Override public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                //回调，如果contentLength()不知道长度，会返回-1
                progressListener.onProgressUpdate(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                return bytesRead;
            }
        };
    }
}
