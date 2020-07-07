package com.example.camerafilterappmvvm.ui.main

import android.Manifest
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.camerafilterappmvvm.R
import com.example.camerafilterappmvvm.imageProcess.CameraApi
import com.example.camerafilterappmvvm.model.CameraParams
import com.example.camerafilterappmvvm.util.askPermissions
import com.example.camerafilterappmvvm.util.checkPermissions
import com.example.camerafilterappmvvm.util.showToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(!checkPermissions(this, NECESSARY_PERMISSIONS))
            askPermissions(this, NECESSARY_PERMISSIONS, REQUEST_CODE_PERMISSIONS)

        preview_camera.holder.addCallback(cameraPreviewCallback)

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (checkPermissions(this, NECESSARY_PERMISSIONS))
                CameraApi.openCamera(this, CameraParams(preview_camera.holder.surface, 0))
            else showToast(this, "퍼미션이 거부되었습니다.")
        }
    }

    private val cameraPreviewCallback = object : SurfaceHolder.Callback {
        override fun surfaceChanged(
            holder: SurfaceHolder?,
            format: Int,
            width: Int,
            height: Int) {

        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            CameraApi.closeCamera()
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
            if (!checkPermissions(this@MainActivity, NECESSARY_PERMISSIONS)) return
            CameraApi.openCamera(this@MainActivity,
                CameraParams(preview_camera.holder.surface, 0))
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