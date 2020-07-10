package com.example.camerafilterappmvvm.model

import android.opengl.GLSurfaceView
import io.reactivex.rxjava3.core.Observable
import java.nio.ByteBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ImageRenderer(val imgBuffer: Observable<ByteBuffer>) : GLSurfaceView.Renderer{

    override fun onDrawFrame(p0: GL10?) {
        imgBuffer.subscribe {

        }
    }

    override fun onSurfaceChanged(p0: GL10?, p1: Int, p2: Int) {
        // TODO("Not yet implemented")
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        // TODO("Not yet implemented")
    }

    companion object {
        const val VERTEX_SHADER =
            "attribute vec4 v_position;" +
            "attribute vec2 v_colour;" +
            "varying vec2 v_texCoord;" +
            "void main()" +
            "{" +
                "gl_Position = v_position;" +
                "v_texCoord = v_colour;" +
            "}"
        const val FRAGMENT_SHADER =
            "precision mediump float;" +
            "varying vec2 v_texCoord;" +
            "uniform sampler2D s_texture;" +
            "void main() {" +
                "gl_FragColor = texture2D(s_texture, v_texCoord);" +
            "}"

        const val COORDS_PER_VERTEX = 3
        val VERTEXS = arrayOf(
            -1.0f,  1.0f,   0.0f,
            1.0f,   1.0f,   0.0f,
            -1.0f,  -1.0f,  0.0f,
            1.0f,   -1.0f,  0.0f
        )
        val COORDS = arrayOf(
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
        )
    }
}