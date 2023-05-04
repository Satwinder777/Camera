package com.example.cameraactivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import com.example.cameraactivity.databinding.ActivityMainBinding
import com.google.android.gms.cast.framework.media.ImagePicker
import com.google.common.util.concurrent.ListenableFuture
import com.shz.imagepicker.imagepicker.ImagePickerCallback
import com.shz.imagepicker.imagepicker.model.PickedResult
import com.squareup.picasso.Picasso
import java.util.concurrent.ExecutorService

class MainActivity : AppCompatActivity(), ImagePickerCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    private var imageCapture: ImageCapture? = null
    private lateinit var imgCaptureExecutor: ExecutorService
    private lateinit var imgView:ImageView

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var imagePicker = com.shz.imagepicker.imagepicker.ImagePicker.Builder(
            this.packageName + ".provider",
            this
        )
            .useCamera(true)                            // Use camera picker if true
            .build()
        var btn = findViewById<ImageView>(R.id.btn_pick_image)
        btn.setOnClickListener {
            animateFlash()
            var imgdata = com.github.dhaval2404.imagepicker.ImagePicker.with(this).cameraOnly().start()
        }
         imgView = findViewById(R.id.imageView)
    }


    private fun animateFlash() {
        binding.root.postDelayed({
            binding.root.foreground = ColorDrawable(Color.WHITE)
            binding.root.postDelayed({
                binding.root.foreground = null
            }, 50)
        }, 100)
        var img = ImagePicker()
    }


    override fun onImagePickerResult(result: PickedResult) {
        when (result) {
            PickedResult.Empty -> {
                // No file was selected, noting to do
            }
            is PickedResult.Error -> {
                val throwable = result.throwable
                // Some error happened, handle this throwable
            }
            is PickedResult.Multiple -> {
                val pickedImages = result.images
                val files = pickedImages.map { it.file }
                // Selected multiple images, do whatever you want with files
            }
            is PickedResult.Single -> {
                val pickedImage = result.image
                val file = pickedImage.file
                // Selected one image, do whatever you want with file
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            Log.e("satwinder", ": $uri", )
            Picasso.get()
                .load(uri)
                .into(imgView);
        } else if (resultCode == com.github.dhaval2404.imagepicker.ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, com.github.dhaval2404.imagepicker.ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}