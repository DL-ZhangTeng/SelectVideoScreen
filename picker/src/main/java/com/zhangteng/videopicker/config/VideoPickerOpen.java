package com.zhangteng.videopicker.config;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.zhangteng.videopicker.activitys.VideoPickerActivity;
import com.zhangteng.videopicker.utils.FileUtils;

/**
 * Created by swing on 2018/4/18.
 */
public class VideoPickerOpen {
    private static VideoPickerOpen videoPickerOpen;
    private static String TAG = "ImagePicker";
    private VideoPickerConfig videoPickerConfig;

    public static VideoPickerOpen getInstance() {
        if (videoPickerOpen == null) {
            videoPickerOpen = new VideoPickerOpen();
        }
        return videoPickerOpen;
    }


    public void open(Activity mActivity) {
        if (videoPickerOpen.videoPickerConfig == null) {
            Log.e(TAG, "请配置 videoPickerConfig");
            return;
        }
        if (videoPickerOpen.videoPickerConfig.getImageLoader() == null) {
            Log.e(TAG, "请配置 ImageLoader");
            return;
        }
        if (TextUtils.isEmpty(videoPickerOpen.videoPickerConfig.getProvider())) {
            Log.e(TAG, "请配置 Provider");
            return;
        }
        if (videoPickerOpen.videoPickerConfig.getiHandlerCallBack() == null) {
            Log.e(TAG, "请配置 IHandlerCallBack");
            return;
        }
        FileUtils.createFile(videoPickerOpen.videoPickerConfig.getFilePath());

        Intent intent = new Intent(mActivity, VideoPickerActivity.class);
        mActivity.startActivity(intent);
    }

    public void openCamera(Activity mActivity) {
        if (videoPickerOpen.videoPickerConfig == null) {
            Log.e(TAG, "请配置 videoPickerConfig");
            return;
        }
        if (videoPickerOpen.videoPickerConfig.getImageLoader() == null) {
            Log.e(TAG, "请配置 ImageLoader");
            return;
        }
        if (TextUtils.isEmpty(videoPickerOpen.videoPickerConfig.getProvider())) {
            Log.e(TAG, "请配置 Provider");
            return;
        }

        FileUtils.createFile(videoPickerOpen.videoPickerConfig.getFilePath());
        videoPickerOpen.videoPickerConfig.setOpenCamera(true);

        Intent intent = new Intent(mActivity, VideoPickerActivity.class);
        mActivity.startActivity(intent);
    }


    public VideoPickerOpen setVideoPickerConfig(VideoPickerConfig videoPickerConfig) {
        this.videoPickerConfig = videoPickerConfig;
        return this;
    }

    public VideoPickerConfig getVideoPickerConfig() {
        if (videoPickerConfig == null) {
            videoPickerConfig = new VideoPickerConfig(new VideoPickerConfig.Builder());
        }
        return videoPickerConfig;
    }
}
