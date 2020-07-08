package com.example.camerafilterappmvvm.ui.main.adapter

import android.opengl.GLSurfaceView
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.databinding.BindingAdapter

/**
 * SurfaceView에서 사용할
 * Surface 콜백 설정 속성
 */
@BindingAdapter("android:setSurfaceCallback")
fun setSurfaceCallback(view: SurfaceView, callback: SurfaceHolder.Callback?) {
    if (callback != null) {
        if (view.isLaidOut) callback.surfaceCreated(view.holder)

        view.holder.addCallback(callback)
    }
}

/**
 * GLSurfaceView에서 사용할
 * 렌더러 설정 속성
 */
@BindingAdapter("android:setRenderer")
fun setRenderer(view: GLSurfaceView, renderer: GLSurfaceView.Renderer) = view.setRenderer(renderer)

/**
 * GL ES 버전 설정 속성
 */
@BindingAdapter("android:setEGLContextClientVersion")
fun setEGLContextClientVersion(view: GLSurfaceView, version: Int) = view.setEGLContextClientVersion(version)