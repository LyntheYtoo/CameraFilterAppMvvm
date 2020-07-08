package com.example.camerafilterappmvvm.model

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.*
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface

/**
 * 카메라에 대한 파이프라인을 다루는 객체
 */
class CameraApi {
    val TAG: String = "CameraApi"

    private lateinit var cameraDevice: CameraDevice
    private lateinit var cameraCharacter: CameraCharacteristics

    private var cameraHandlerThread: HandlerThread? = null
    private lateinit var cameraHandler: Handler

    private lateinit var cameraParams: CameraParams

    /**
     * CameraDevice 의 콜백 객체
     * 카메라가 열리거나, 연결해제되거나, 오류 발생시 실행되는 콜백
     */
    private val cameraStateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            createPreview(camera)
        }

        override fun onDisconnected(camera: CameraDevice) {
            Log.d(TAG, "CameraDevice disconnected, Shut down CameraDevice")
            camera.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            Log.e(TAG, "Error occurred on CameraDevice")
            camera.close()
        }

    }

    /**
     * 카메라가 보여줄 프리뷰를 생성합니다
     */
    private fun createPreview(camera: CameraDevice) {
        val previewSurfaceArr = ArrayList<Surface>()
        val cameraRequest = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

        val sensorOrientation = cameraCharacter.get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0
        val jpegOrientation =
            (cameraParams.displayOrientation + sensorOrientation + 270) % 360

        previewSurfaceArr.add(cameraParams.surface)
        cameraRequest.addTarget(cameraParams.surface)

//        cameraRequest.set(CaptureRequest.JPEG_ORIENTATION, jpegOrientation)

        camera.createCaptureSession(previewSurfaceArr, object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                Log.d(TAG, "Create Camera Session Success")

                session.setRepeatingRequest(cameraRequest.build(), null, cameraHandler)
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                Log.e(TAG, "Create Camera Session Fail")
            }

        }, cameraHandler)
    }


    /**
     * 카메라를 여는 함수
     */
    @SuppressLint("MissingPermission")
    fun openCamera(context: Context, params: CameraParams) {
        cameraParams = params

        if (cameraHandlerThread == null) {
            cameraHandlerThread = HandlerThread(
                TAG
            )
            cameraHandlerThread!!.start()

            cameraHandler = Handler(
                cameraHandlerThread!!.looper)
        }

        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[cameraParams.camPosition]
        cameraCharacter = cameraManager.getCameraCharacteristics(cameraId)

        cameraManager.openCamera(cameraId, cameraStateCallback, cameraHandler)

        Log.d(TAG, "openCamera")
    }

    /**
     * 카메라를 닫는 함수
     */
    fun closeCamera() {
        cameraDevice.close()

        if (cameraHandlerThread != null) {
            cameraHandlerThread!!.quitSafely()
            cameraHandlerThread = null
        }
        Log.d(TAG, "closeCamera")
    }

    companion object {

        /**
         * 화면 방향을 JPEG 방향으로 변환하는데 필요한 상수
         */
        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }
    }
}
