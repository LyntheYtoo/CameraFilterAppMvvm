package com.example.camerafilterappmvvm.util

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat

/**
 * 퍼미션 체크 함수
 * Array에 있는 퍼미션들이 허락되있는지 체크한다
 * 반환값 - 허락 여부
 */
fun checkPermissions(activity: Activity, permissions: List<String>): Boolean =
    permissions.all {
        ActivityCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
    }

/**
 * 퍼미션 요청 함수
 * 요청할 퍼미션들을 Array에 담아 매개변수로 보낸다
 */
fun askPermissions(
    activity: Activity,
    permissions: List<String>,
    requestCodePermissions: Int
): Boolean {
    for (permission: String in permissions) {
        if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showToast(activity, "$permission 권한이 필요합니다.");
            }
            ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), requestCodePermissions)
            return false
        }
    }
    return true
}

/**
 * 토스트 메시지 출력 함수
 */
fun showToast(activity: Activity, message: String)
        = Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()