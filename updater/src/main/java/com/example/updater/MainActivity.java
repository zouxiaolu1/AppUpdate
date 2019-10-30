package com.example.updater;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.updater.updater.AppUpdater;
import com.example.updater.updater.bean.DownloadBean;
import com.example.updater.updater.net.INetCallBack;
import com.example.updater.updater.net.INetDownloadCallBack;
import com.example.updater.updater.ui.UpdateVersionShowDialog;
import com.example.updater.updater.utils.AppUtils;

import java.io.File;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    private Button mBtnUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnUpdater = findViewById(R.id.btn_updater);


        mBtnUpdater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AppUpdater.getInstance().getiNetManager().get("http://59.110.162.30/app_updater_version.json", new INetCallBack() {
                    @Override
                    public void success(final String response) {

                        Log.d("hyman","response =" + response);
                        DownloadBean bean = DownloadBean.parse(response);

                        if(bean == null){
                            Toast.makeText(MainActivity.this,"版本更测试接口返回数据异常",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //检测是否需要弹窗
                        try {
                            long versionCode = Long.parseLong(bean.versionCode);
                            if(versionCode <= AppUtils.getVersionCode(MainActivity.this)){
                                Toast.makeText(MainActivity.this,"已经是最新版本，无需更新",Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }catch (NumberFormatException e){
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,"版本更测试接口返回版本号异常",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //弹窗
                        UpdateVersionShowDialog.show(MainActivity.this,bean);

                    }

                    @Override
                    public void failed(Throwable throwable) {

                        Toast.makeText(MainActivity.this,"版本更新失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
