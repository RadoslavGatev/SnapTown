package com.rado.veni.snaptown.controls;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rado.veni.snaptown.R;

/**
 * Created by HAXVY on 30.1.2015 г..
 */
public class CaptureControlsView extends LinearLayout {

    private Button pictureButton;
    private Button videoButton;

    public CaptureControlsView(Context context) {
        super(context);
        init(null, 0);
    }

    public CaptureControlsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CaptureControlsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle){
        inflate(getContext(), R.layout.view_capture_controls, this);

        pictureButton = (Button) findViewById(R.id.capture_picture_button);
        videoButton = (Button) findViewById(R.id.capture_video_button);
    }

    public void setPictureButtonOnClickListener(OnClickListener listener){
        if (pictureButton != null){
            pictureButton.setOnClickListener(listener);
        }
    }

    public void setVideoButtonOnClickListener(OnClickListener listener){
        if (videoButton != null){
            videoButton.setOnClickListener(listener);
        }
    }
}
