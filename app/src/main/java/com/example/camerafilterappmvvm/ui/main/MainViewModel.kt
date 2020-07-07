package com.example.camerafilterappmvvm.ui.main

import android.content.Context
import android.view.Surface
import android.view.SurfaceHolder
import androidx.lifecycle.ViewModel
import com.example.camerafilterappmvvm.imageProcess.CameraApi
import com.example.camerafilterappmvvm.model.CameraParams

class MainViewModel : ViewModel() {
    private val cameraApi = CameraApi

    private var cameraPosition: Int = 0

    /**
     * SurfaceView 에 등록할 카메라프리뷰 콜백을 만들어주는 함수
     */
    fun createCameraPreviewCallback(context: Context, preview: Surface) = object : SurfaceHolder.Callback {
        override fun surfaceChanged(
            holder: SurfaceHolder?,
            format: Int,
            width: Int,
            height: Int
        ) {}

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            cameraApi.closeCamera()
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
            cameraApi.openCamera(
                context,
                CameraParams(preview, 0)
            )
        }
    }

    /**
     * 카메라 관련 동작
     */
    fun openCamera(context: Context, surface: Surface) =
        cameraApi.openCamera(context, CameraParams(surface, cameraPosition))
    fun closeCamera() = cameraApi.closeCamera()
}