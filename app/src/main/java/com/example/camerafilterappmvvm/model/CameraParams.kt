package com.example.camerafilterappmvvm.model

import android.view.Surface

/**
 * 카메라 Api를 위한 데이터 객체
 */
data class CameraParams(
    // 송출될 화면 서피스
    val surface: Surface,
    // 카메라 번호(전면 or 후면)
    val camPosition: Int
)