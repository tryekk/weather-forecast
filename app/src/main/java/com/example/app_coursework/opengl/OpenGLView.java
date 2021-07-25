package com.example.app_coursework.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.example.app_coursework.opengl.OpenGLRenderer;

public class OpenGLView extends GLSurfaceView {

    public OpenGLView(Context context) {
        super(context);
        init(context);
    }

    public OpenGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context activityContext) {
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        setRenderer(new OpenGLRenderer(activityContext, getWidth(), getHeight()));
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
}
