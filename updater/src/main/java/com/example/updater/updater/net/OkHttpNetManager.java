package com.example.updater.updater.net;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpNetManager implements INetManager{

    private static OkHttpClient sOkHttpClient;
    private static Handler sHandler = new Handler(Looper.getMainLooper());


    static {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS);
        sOkHttpClient = builder.build();

    }

    @Override
    public void get(String url, final INetCallBack callBack) {

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).get().build();
        Call call = sOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {

                sHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        callBack.failed(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try{
                    final String string = response.body().string();
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            callBack.success(string);
                        }
                    });
                }catch (Throwable e){

                    e.printStackTrace();
                    callBack.failed(e);
                }
            }
        });

    }

    @Override
    public void download(String url, final File targetFile, final INetDownloadCallBack callBack) {

        if(!targetFile.exists()){
            targetFile.getParentFile().mkdirs();
        }
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).get().build();
        Call call = sOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.failed(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                InputStream is = null;
                OutputStream os = null;
                try{

                    final long totalLen = response.body().contentLength();

                    is = response.body().byteStream();
                    os = new FileOutputStream(targetFile);

                    byte[] buffer = new byte[8*1024];
                    long curLen = 0;
                    int bufferLen = 0;
                    while (( bufferLen = is.read(buffer))!=-1){
                        os.write(buffer,0,bufferLen);
                        os.flush();
                        curLen += bufferLen;
                        final long finalCurLen = curLen;
                        sHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.progress((int) (finalCurLen * 1.0f / totalLen * 100));
                            }
                        });
                    }
                    try {
                        targetFile.setExecutable(true,false);
                        targetFile.setReadable(true,false);
                        targetFile.setWritable(true,false);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.success(targetFile);
                        }
                    });
                }catch (final Throwable e){

                    e.printStackTrace();
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.failed(e);
                        }
                    });

                }finally {
                    if(is != null){
                        is.close();
                    }
                    if(os != null){
                        os.close();
                    }
                }

            }
        });

    }
}
