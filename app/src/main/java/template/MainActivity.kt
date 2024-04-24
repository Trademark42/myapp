package com.pokerhand.myapp

import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.io.FileInputStream
import java.io.InputStream
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import template.theme.TemplateTheme
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color


class MainActivity : AppCompatActivity() {
    private lateinit var selectImageButton: Button
    private lateinit var imageView: ImageView
    private lateinit var resultText: TextView
    private lateinit var tflite: Interpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectImageButton = findViewById(R.id.select_image_button)
        imageView = findViewById(R.id.image_view)
        resultText = findViewById(R.id.result_text)

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 0)
            }
            try {
                tflite = Interpreter(loadModelFile())
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }

    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = this.assets.openFd("your_model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage = data.data
            val imageStream: InputStream? = contentResolver.openInputStream(selectedImage!!)
            val bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            val inputArray = Array(1) { Array(224) { Array(224) { FloatArray(3) } } }

            for (x in 0 until 224) {
                for (y in 0 until 224) {
                    val pixel = bitmap.getPixel(x, y)
                    inputArray[0][y][x][0] = Color.red(pixel) / 255.0f
                    inputArray[0][y][x][1] = Color.green(pixel) / 255.0f
                    inputArray[0][y][x][2] = Color.blue(pixel) / 255.0f
                }
            }

            imageView.setImageBitmap(bitmap)
            // TODO: Run image classification on the bitmap and display the results in resultText.
        }
    }
}
