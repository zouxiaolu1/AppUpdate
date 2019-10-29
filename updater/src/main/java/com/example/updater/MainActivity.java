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
import com.example.updater.updater.net.INetCallBack;
import com.example.updater.updater.net.INetDownloadCallBack;

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

                        AppUpdater.getInstance().getiNetManager().download("", null, new INetDownloadCallBack() {
                            @Override
                            public void success(File apkFile) {


                            }

                            @Override
                            public void progress(int progress) {

                            }

                            @Override
                            public void failed(Throwable throwable) {

                            }
                        });
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
