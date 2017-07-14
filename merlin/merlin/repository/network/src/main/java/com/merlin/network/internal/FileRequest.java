package com.merlin.network.internal;

import com.merlin.network.internal.utils.NestLog;
import com.merlin.network.HttpMethod;
import com.merlin.network.beans.NetworkResponse;
import com.merlin.network.beans.Response;
import com.merlin.network.controller.DownloadProgressListener;
import com.merlin.network.internal.exception.RestClientException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;


/**
 * User: Simon
 * Date: 2016/1/20
 * Desc: 文件下载请求
 */
public class FileRequest extends Request<File> {

    private File file;

    public FileRequest(final String dir, final String fileName, HttpMethod method, String url, DownloadProgressListener<File> downloadProgressListener) {
        super(File.class, method, url, downloadProgressListener);
        try {
            File fileDir = new File(dir);
            if(!fileDir.exists()){
                fileDir.mkdirs();
            }
            this.file = new File(dir, fileName);
            if(!this.file.exists()){
                this.file.createNewFile();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.initData();
    }

    @Override
    public Response parse(NetworkResponse response) {

        NestLog.e("parse:" + file.getPath());
        DownloadProgressListener<File> listener = (DownloadProgressListener<File>) getListener();
        try {
            if(response != null){
                FileOutputStream fos = new FileOutputStream(file);
                byte[] bytes = response.data;
                fos.write(bytes);
                fos.close();
                return new Response(response.headers, file);
            }
            throw new RestClientException();
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onErrorResponse(e);
            }
            return null;
        }


    }

    @Override
    public byte[] writeBody(Map<String, ?> params, String paramsEncoding) {
        return encodeParameters(params, paramsEncoding);
    }

}
