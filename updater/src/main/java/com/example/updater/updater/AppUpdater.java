package com.example.updater.updater;

import com.example.updater.updater.net.INetManager;
import com.example.updater.updater.net.OkHttpNetManager;

public class AppUpdater {

    private static AppUpdater mInstance = new AppUpdater();
    //网络请求，下载的能力
    private INetManager iNetManager = new OkHttpNetManager();

    /*public void setiNetManager(INetManager manager){
        iNetManager = manager;
    }*/
    public INetManager getiNetManager(){
        return iNetManager;
    }

    public static AppUpdater getInstance(){
        return mInstance;
    }

}
