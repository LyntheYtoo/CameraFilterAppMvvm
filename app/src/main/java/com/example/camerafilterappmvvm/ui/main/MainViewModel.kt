package com.example.camerafilterappmvvm.ui.main

import android.app.Activity
import android.content.Context
import android.graphics.ImageFormat
import android.media.ImageReader
import android.opengl.GLSurfaceView
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.SurfaceHolder
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.example.camerafilterappmvvm.model.CameraParams
import com.example.camerafilterappmvvm.model.CameraApi
import com.example.camerafilterappmvvm.model.ImageRenderer

class MainViewModel : ViewModel(), LifecycleObserver {
    val cameraPreviewCallback = ObservableField<SurfaceHolder.Callback>()

    private val cameraApi = CameraApi()

    private var cameraPosition = 0

    private lateinit var cameraImageReader: ImageReader
    private lateinit var imageReaderHandler: Handler

    /**
     * SurfaceView 에 등록할 카메라프리뷰 콜백을 만들어주는 함수
     * SurfaceView 가 켜지고 꺼질때 작동하는 메인로직이 담겨있다
     */
    fun createCameraPreviewCallback(context: Context) = object : SurfaceHolder.Callback {

        override fun surfaceChanged(
            holder: SurfaceHolder,
            format: Int,
            width: Int,
            height: Int
        ) {}

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            Log.d(TAG, "Surface Destroyed")

            cameraApi.closeCamera()
            cameraImageReader.close()
            imageReaderHandler.close()
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            Log.d(TAG, "Created Surface Size : ${holder.surfaceFrame.width()}, ${holder.surfaceFrame.height()}")

            imageReaderHandler = createHandler("ImageReader")
            cameraImageReader = createImageReader(
                holder.surfaceFrame.width(),
                holder.surfaceFrame.height(),
                imageReaderHandler
            )

            val displayOrientation =
                if (context is Activity)
                    context.windowManager.defaultDisplay.rotation
                else 0

            cameraApi.openCamera(
                context,
                CameraParams(cameraImageReader.surface, cameraPosition, IMG_FORMAT, displayOrientation)
            )
        }
    }

    /**
     * 카메라에서 이미지를 받아 처리할
     * 이미지리더 생성 함수
     */
    fun createImageReader(width: Int, height: Int, handler: Handler): ImageReader {
        val imgReader = ImageReader.newInstance(width, height, IMG_FORMAT, IMG_BUF_SIZE)
        imgReader.setOnImageAvailableListener({
            val image = it.acquireNextImage()
            // TODO()
            image.close()

        },handler)

        return imgReader
    }

    /**
     * ImageReader 가 다른 스레드에서 작동할 수 있게
     * Handler 를 생성하는 함수
     */
    fun createHandler(name: String): Handler {
        val handlerThread = HandlerThread(name)
        handlerThread.start()

        return Handler(handlerThread.looper)
    }

    /**
     * 해당 Handler 를 종료시키는 익스텐션 함수
     */
    fun Handler.close() = this.looper.quitSafely()

    /**
     * GLSurfaceView 에서 사용할 Renderer 생성 함수
     */
    fun createRenderer(): GLSurfaceView.Renderer {
        return ImageRenderer()
    }

    companion object {
        const val TAG = "MainViewModel"
        const val IMG_FORMAT = ImageFormat.YUV_420_888
        const val IMG_BUF_SIZE = 2
    }
}