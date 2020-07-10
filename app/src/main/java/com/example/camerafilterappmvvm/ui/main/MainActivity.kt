package com.example.camerafilterappmvvm.ui.main

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelProvider
import com.example.camerafilterappmvvm.R
import com.example.camerafilterappmvvm.databinding.ActivityMainBinding
import com.example.camerafilterappmvvm.util.askPermissions
import com.example.camerafilterappmvvm.util.checkPermissions
import com.example.camerafilterappmvvm.util.showToast

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    lateinit var lifecycleRegistry: LifecycleRegistry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // 커스텀 라이프사이클 오너
        lifecycleRegistry = LifecycleRegistry(this)

        // 뷰모델 초기화
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // 뷰모델 데이터 바인딩
        binding.viewModel = viewModel

        // 퍼미션 체크
        if(checkPermissions(this, NECESSARY_PERMISSIONS))
            // 체크가 통과되면 콜백을 부착
            viewModel.setCameraPreviewCallback(this)
            // 체크가 실패하면 퍼미션 요청
        else askPermissions(this, NECESSARY_PERMISSIONS, REQUEST_CODE_PERMISSIONS)

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
                // 카메라 관련 콜백을 업데이트
                viewModel.setCameraPreviewCallback(this)

            }
            else showToast(this, "퍼미션이 거부되었습니다.")
        }
    }

    companion object {
        const val TAG = "MainActivity"
        const val REQUEST_CODE_PERMISSIONS = 10

        val NECESSARY_PERMISSIONS = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO)
    }
}