package com.example.camerafilterappmvvm.ui.main.adapter

import android.opengl.GLSurfaceView
import android.view.SurfaceHolder
import androidx.databinding.BindingAdapter

/**
 * GLSurfaceView에서 사용할
 * 렌더러 설정 속성
 */
@BindingAdapter("gl:setRenderer")
fun setRenderer(view: GLSurfaceView, renderer: GLSurfaceView.Renderer) = view.setRenderer(renderer)

/**
 * GLSurfaceView에서 사용할
 * Surface 콜백 설정 속성
 */
@BindingAdapter("gl:setSurfaceCallback")
fun setSurfaceCallback(view: GLSurfaceView, callback: SurfaceHolder.Callback)
        = view.holder.addCallback(callback)