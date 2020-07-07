package com.example.camerafilterappmvvm.ui.main

import android.Manifest
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.camerafilterappmvvm.R
import com.example.camerafilterappmvvm.imageProcess.CameraApi
import com.example.camerafilterappmvvm.model.CameraParams
import com.example.camerafilterappmvvm.util.askPermissions
import com.example.camerafilterappmvvm.util.checkPermissions
import com.example.camerafilterappmvvm.util.showToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    lateinit var cameraPreviewCallback: SurfaceHolder.Callback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 뷰모델 초기화 및 뷰모델관련 작업
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        cameraPreviewCallback = viewModel.createCameraPreviewCallback(this, preview_camera.holder.surface)

        // 퍼미션 체크
        if(!checkPermissions(this, NECESSARY_PERMISSIONS))
            askPermissions(this, NECESSARY_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        else preview_camera.holder.addCallback(cameraPreviewCallback)

        // Hide the status bar
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or // hide nav bar
                View.SYSTEM_UI_FLAG_FULLSCREEN or // hide status bar
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY // hide status bar and nav bar after a short delay, or if the user interacts with the middle of the screen
                )
    }

    /**
     * 퍼미션 결과 처리
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 이 액티비티에서 요청한 퍼미션이고
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            // 모든 퍼미션이 허락된 상황이라면
            if (checkPermissions(this, NECESSARY_PERMISSIONS)) {
                // 카메라를 키고 프리뷰의 뷰에 카메라 관련 콜백을 부착
                viewModel.openCamera(this, preview_camera.holder.surface)
                preview_camera.holder.addCallback(cameraPreviewCallback)
            }
            else showToast(this, "퍼미션이 거부되었습니다.")
        }
    }

    companion object {
        val TAG = "MainActivity"
        val NECESSARY_PERMISSIONS = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO)
        val REQUEST_CODE_PERMISSIONS = 10
    }
}