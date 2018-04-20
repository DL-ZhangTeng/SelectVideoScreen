package com.zhangteng.videopicker.bean;

import java.util.List;

/**
 * Created by swing on 2018/4/17.
 */
public class FolderInfo {

    private String name;                         // 文件夹名称
    private String path;                         // 文件夹路径
    private VideoInfo videoInfo;                 // 文件夹中第一个视频的信息
    private List<VideoInfo> videoInfoList;       // 文件夹中的视频集合

    public FolderInfo() {
    }

    public FolderInfo(String name, String path, VideoInfo videoInfo, List<VideoInfo> videoInfoList) {
        this.name = name;
        this.path = path;
        this.videoInfo = videoInfo;
        this.videoInfoList = videoInfoList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(VideoInfo videoInfo) {
        this.videoInfo = videoInfo;
    }

    public List<VideoInfo> getVideoInfoList() {
        return videoInfoList;
    }

    public void setVideoInfoList(List<VideoInfo> videoInfoList) {
        this.videoInfoList = videoInfoList;
    }

    @Override
    public boolean equals(Object object) {
        try {
            FolderInfo other = (FolderInfo) object;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(object);
    }
}
