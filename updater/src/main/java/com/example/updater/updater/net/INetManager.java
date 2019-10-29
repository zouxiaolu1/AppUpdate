package com.example.updater.updater.net;

import java.io.File;

public interface INetManager {

    void get(String url,INetCallBack callBack);
    void download(String url, File targetFile,INetDownloadCallBack callBack);

}
