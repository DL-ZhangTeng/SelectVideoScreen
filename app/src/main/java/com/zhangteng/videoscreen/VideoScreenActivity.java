package com.zhangteng.videoscreen;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

import com.zhangteng.videopicker.callback.IHandlerCallBack;
import com.zhangteng.videopicker.config.VideoPickerConfig;
import com.zhangteng.videopicker.config.VideoPickerOpen;
import com.zhangteng.videopicker.imageloader.GlideImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoScreenActivity extends AppCompatActivity {
    private Context mContext;
    private static final int PERMISSIONS_REQUEST_READ = 454;
    static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private IHandlerCallBack iHandlerCallBack = new IHandlerCallBack() {
        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(List<String> photoList) {
            if (photoList != null && !photoList.isEmpty()) {
                Constant.path = "file://" + photoList.get(0);
            }
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onFinish() {
            checkSelfPermission();
        }

        @Override
        public void onError() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent_screen);
        mContext = this;
        VideoPickerConfig videoPickerConfig = new VideoPickerConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）,可以实现ImageLoader自定义（内置Glid实现）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口，可以实现IHandlerCallBack自定义
                .pathList(new ArrayList<String>())                         // 记录已选的图片
                .isShowCamera(false)
                .provider("com.zhangteng.videoscreen.fileprovider")
                .multiSelect(false)                      // 是否多选   默认：false
                .build();
        VideoPickerOpen.getInstance().setVideoPickerConfig(videoPickerConfig).open(this);
    }

    /**
     * 检查权限
     */
    void checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(mContext, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_READ);
        } else {
            startWallpaper();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startWallpaper();

                } else {
                    Toast.makeText(mContext, getString(R.string._lease_open_permissions), Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    /**
     * 选择壁纸
     */
    void startWallpaper() {
        final Intent pickWallpaper = new Intent(Intent.ACTION_SET_WALLPAPER);
        Intent chooser = Intent.createChooser(pickWallpaper, getString(R.string.choose_wallpaper));
        startActivity(chooser);
        finish();
    }
}
