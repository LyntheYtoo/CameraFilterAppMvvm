package com.example.camerafilterappmvvm.model

import android.graphics.Bitmap
import android.media.Image
import io.reactivex.rxjava3.core.Observable
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer

/**
 * ImageReader 에서 공급되는 이미지를
 * OpenCV 를 이용하여 변환한다
 *
 * YUV -> RGB 변환 후
 * OpenCV Api 를 사용해서 이미지를 변경한다
 *
 * 비동기 작업은 RxJava 를 통해 이루어 진다
 */
class ImageConverter {
    fun convertImage(image: Observable<Image>): Observable<ByteBuffer> {
        return image
            .concatMap {
                image.takeLast(1)
            }
            .map {
                val width = it.width
                val height = it.height
                val yData = it.planes[0].buffer
                val uvData = it.planes[1].buffer

                val yMat = Mat(height, width, CvType.CV_8UC1, yData)
                val uvMat = Mat(height / 2, width / 2, CvType.CV_8UC2, uvData)
                val convertedMat = Mat()

                // YUV -> RGB 변환
                Imgproc.cvtColorTwoPlane(yMat, uvMat, convertedMat, Imgproc.COLOR_YUV2BGR_NV21)

                // Mat -> Byte 변환
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                Utils.matToBitmap(convertedMat, bitmap)
                val byteBuffer = ByteBuffer.allocateDirect(width*height*4)
                bitmap.copyPixelsToBuffer(byteBuffer)

                byteBuffer
            }
    }


    enum class Filter {
        DEAULT, CANNY, BLUR, EMBOSE, SKETCH
    }
    lateinit var detectorFile: File
}