package com.example.snaptown.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.snaptown.R;

public class CaptureControlsView extends RelativeLayout {

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
