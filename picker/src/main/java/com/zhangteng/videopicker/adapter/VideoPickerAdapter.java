package com.zhangteng.videopicker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.zhangteng.videopicker.R;
import com.zhangteng.videopicker.bean.VideoInfo;
import com.zhangteng.videopicker.config.VideoPickerConfig;
import com.zhangteng.videopicker.config.VideoPickerOpen;
import com.zhangteng.videopicker.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/4/17.
 */
public class VideoPickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEAD = 0;
    private static final int PHOTO = 1;
    private Context mContext;
    private List<VideoInfo> videoInfoList;
    private VideoPickerConfig videoPickerConfig = VideoPickerOpen.getInstance().getVideoPickerConfig();
    private List<String> selectImage = new ArrayList<>();

    public VideoPickerAdapter(Context context, ArrayList<VideoInfo> videoInfoList) {
        this.mContext = context;
        this.videoInfoList = videoInfoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            CameraViewHolder cameraViewHolder = new CameraViewHolder(LayoutInflater.from(mContext).inflate(R.layout.video_picker_item_camera, parent, false));
            return cameraViewHolder;
        } else {
            ImageViewHolder imageViewHolder = new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.video_picker_item_photo, parent, false));
            return imageViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        int heightOrWidth = Math.min(ScreenUtils.getScreenHeight(mContext) / 3, ScreenUtils.getScreenWidth(mContext) / 3);
        layoutParams.height = heightOrWidth;
        layoutParams.width = heightOrWidth;
        holder.itemView.setLayoutParams(layoutParams);
        VideoInfo videoInfo = null;
        if (videoPickerConfig.isShowCamera()) {
            if (position == 0) {
                ((CameraViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemClickListener != null) {
                            if (videoPickerConfig.isMultiSelect() && selectImage.size() < videoPickerConfig.getMaxSize()) {
                                onItemClickListener.onCameraClick(selectImage);
                            } else {
                                onItemClickListener.onCameraClick(selectImage);
                            }
                        }
                        notifyDataSetChanged();
                    }
                });
            } else {
                videoInfo = videoInfoList.get(position - 1);
                videoPickerConfig.getImageLoader().loadImage(mContext, ((ImageViewHolder) holder).imageView, videoInfo.getPath());
                final VideoInfo finalVideoInfo = videoInfo;
                ((ImageViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectImage.contains(finalVideoInfo.getPath())) {
                            selectImage.remove(finalVideoInfo.getPath());
                        } else {
                            if (selectImage.size() < videoPickerConfig.getMaxSize()) {
                                selectImage.add(finalVideoInfo.getPath());
                            }
                        }
                        if (onItemClickListener != null)
                            onItemClickListener.onImageClick(selectImage);
                        notifyDataSetChanged();
                    }
                });
                initView(holder, videoInfo);
            }
        } else {
            videoInfo = videoInfoList.get(position);
            videoPickerConfig.getImageLoader().loadImage(mContext, ((ImageViewHolder) holder).imageView, videoInfo.getPath());
            final VideoInfo finalVideoInfo1 = videoInfo;
            ((ImageViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectImage.contains(finalVideoInfo1.getPath())) {
                        selectImage.remove(finalVideoInfo1.getPath());
                    } else {
                        if (selectImage.size() < videoPickerConfig.getMaxSize())
                            selectImage.add(finalVideoInfo1.getPath());
                    }
                    if (onItemClickListener != null)
                        onItemClickListener.onImageClick(selectImage);
                    notifyDataSetChanged();
                }
            });
            initView(holder, videoInfo);
        }

    }

    private void initView(RecyclerView.ViewHolder holder, VideoInfo videoInfo) {
        if (videoPickerConfig.isMultiSelect()) {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
        } else {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.GONE);
        }
        if (selectImage.contains(videoInfo.getPath())) {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
            ((ImageViewHolder) holder).mask.setVisibility(View.VISIBLE);
            ((ImageViewHolder) holder).checkBox.setChecked(true);
            ((ImageViewHolder) holder).checkBox.setButtonDrawable(R.mipmap.video_picker_select_checked);
        } else {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
            ((ImageViewHolder) holder).mask.setVisibility(View.GONE);
            ((ImageViewHolder) holder).checkBox.setChecked(false);
            ((ImageViewHolder) holder).checkBox.setButtonDrawable(R.mipmap.video_picker_select_unchecked);
        }
    }

    @Override
    public int getItemCount() {
        return videoInfoList.isEmpty() ? 0 : videoPickerConfig.isShowCamera() ? videoInfoList.size() + 1 : videoInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (videoPickerConfig.isShowCamera()) {
                return HEAD;
            }
        }
        return PHOTO;
    }

    public void setVideoInfoList(List<VideoInfo> videoInfoList) {
        this.videoInfoList = videoInfoList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onCameraClick(List<String> selectImage);

        void onImageClick(List<String> selectImage);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private View mask;
        private CheckBox checkBox;

        public ImageViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.video_picker_im_photo);
            this.mask = (View) itemView.findViewById(R.id.video_picker_v_photo_mask);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.video_picker_cb_select);
        }
    }

    private static class CameraViewHolder extends RecyclerView.ViewHolder {

        public CameraViewHolder(View itemView) {
            super(itemView);
        }
    }
}
