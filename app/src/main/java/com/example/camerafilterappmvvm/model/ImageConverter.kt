package com.example.camerafilterappmvvm.model

import android.media.ImageReader

/**
 * ImageReader 에서 공급되는 이미지를
 * OpenCV 를 이용하여 변환한다
 *
 * 비동기 작업은 RxJava 를 통해 이루어 진다
 */
class ImageConverter: ImageReader.OnImageAvailableListener {
    override fun onImageAvailable(p0: ImageReader?) {
        TODO("Not yet implemented")
    }

}