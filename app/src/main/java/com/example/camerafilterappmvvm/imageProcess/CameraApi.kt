package com.example.camerafilterappmvvm.imageProcess

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import com.example.camerafilterappmvvm.model.CameraParams

object CameraApi {
    val TAG: String = "CameraApi"

    private lateinit var cameraDevice: CameraDevice
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraCaptureSession: CameraCaptureSession
    private lateinit var cameraCharacter: CameraCharacteristics

    private var cameraHandlerThread: HandlerThread? = null
    private lateinit var cameraHandler: Handler

    private lateinit var cameraParams: CameraParams

    private val cameraStateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            createPreview()
        }

        override fun onDisconnected(camera: CameraDevice) { }

        override fun onError(camera: CameraDevice, error: Int) { }

    }

    /**
     * 카메라가 보여줄 프리뷰를 생성합니다
     */
    private fun createPreview() {
        val previewSurfaceArr = ArrayList<Surface>()
        val cameraRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

        cameraRequest.addTarget(cameraParams.surface)
        previewSurfaceArr.add(cameraParams.surface)

        cameraDevice.createCaptureSession(previewSurfaceArr, object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                Log.d(TAG, "Create Camera Session Success")
                cameraCaptureSession = session
                cameraCaptureSession.setRepeatingRequest(cameraRequest.build(), null, cameraHandler)
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                Log.e(TAG, "Create Camera Session Fail")
            }

        }, null)
    }


    /**
     * 카메라를 여는 함수
     */
    @SuppressLint("MissingPermission")
    fun openCamera(context: Context, params: CameraParams) {
        Log.d(TAG, "openCamera")

        cameraParams = params

        if (cameraHandlerThread == null) {
            cameraHandlerThread = HandlerThread(TAG)
            cameraHandlerThread!!.start()

            cameraHandler = Handler(cameraHandlerThread!!.looper)
        }

        cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        val cameraId = cameraManager.cameraIdList[cameraParams.camPosition]
        cameraCharacter = cameraManager.getCameraCharacteristics(cameraId)

        cameraManager.openCamera(cameraId, cameraStateCallback,cameraHandler)
    }

    /**
     * 카메라를 닫는 함수
     */
    fun closeCamera() {
        Log.d(TAG, "closeCamera")

        cameraCaptureSession.close()
        cameraDevice.close()

        if (cameraHandlerThread != null) {
            cameraHandlerThread!!.quitSafely()
            cameraHandlerThread = null
        }
    }
}
