package org.yanzi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.yanzi.mode.ApkVersion;
import org.yanzi.playcamera.R;
import org.yanzi.util.NetUtil;
import org.yanzi.util.VersionUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;

/**
 * 演示了APK更新
 */
public class ApkUpdateActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apk_update);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if(NetUtil.isNetworkConnected(ApkUpdateActivity.this)){
                        OkHttpUtils
                                .get()
                                .url("http://192.168.111.111:8080/PictureUpdate/VersionUpdataServlet")
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        Log.d("Update", "请求失败 "+e.getMessage());
                                    }

                                    @Override
                                    public void onResponse(final String response, int id) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Gson gson = new Gson();
                                                ApkVersion version = gson.fromJson(response, ApkVersion.class);
                                                Log.d("Update", version.getAPKUrl()+"\n"
                                                        +version.getImportantLevel()+"\n"
                                                        +version.getVersionNumber());

                                                try {
                                                    if(!version.getVersionNumber().equals("1")){
                                                        URL url = new URL("http://"+version.getAPKUrl());
                                                        Log.d("Update", "the addr is "+"http://"+version.getAPKUrl());
                                                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                        Log.d("Update", "1"+(conn == null));
                                                        assert conn != null;
                                                        conn.connect();
                                                        Log.d("Update", "2");

                                                        InputStream is = conn.getInputStream();
                                                        Log.d("Update", "3");

                                                        if(conn.getContentLength() > 0){
                                                            Log.d("Update", "start download!!!!!");
                                                            File file = new File("/data/data/org.yanzi.playcamera/files/");
                                                            if(!file.exists()) file.mkdir();
                                                            FileOutputStream fos = new FileOutputStream("/data/data/org.yanzi.playcamera/files/app_debug.apk");
                                                            byte[] bytes = new byte[1024];
                                                            int read = 0;
                                                            int hasRead = 0;
                                                            while ((read = is.read(bytes))!=-1){
                                                                fos.write(bytes, 0, read);
                                                                hasRead = hasRead + read;
                                                                Log.d("Update", "DOWNLOADING "+hasRead/conn.getContentLength());
                                                            }
                                                            fos.flush();
                                                            fos.close();
                                                            Log.d("Update", "download apk success!!!");
                                                        }

                                                        Intent intent = new Intent();
                                                        //执行动作
                                                        intent.setAction(Intent.ACTION_VIEW);
                                                        //执行的数据类型
                                                        intent.setDataAndType(Uri.fromFile(new File("/data/data/org.yanzi.playcamera/files/app_debug.apk")), "application/vnd.android.package-archive");
                                                        startActivity(intent);
                                                    }
                                                } catch (NullPointerException e) {
                                                    e.printStackTrace();
                                                    Log.d("Update", "1update is error "+e.getMessage());
                                                } catch (FileNotFoundException e) {
                                                    e.printStackTrace();
                                                    Log.d("Update", "2update is error "+e.getMessage());

                                                } catch (MalformedURLException e) {
                                                    e.printStackTrace();
                                                    Log.d("Update", "3update is error "+e.getMessage());

                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                    Log.d("Update", "4update is error "+e.getMessage());

                                                }
                                            }
                                        }).start();
                                    }
                                });
                    }
                    try {
                        Thread.sleep(1000000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
