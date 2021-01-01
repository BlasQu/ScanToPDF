package com.example.scantopdf

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val CAMERA_RQ = 101
    val CAMERA_PERMISSION = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnListeners()
    }

    private fun btnListeners(){
        btn_camera.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, Proceed with button action
                    startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_RQ) // Go to onActivityResult()
                }
                else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                    // Denied Once
                    Functions().showDialog(this@MainActivity, "Permission required!", "ScanToPDF requires camera permission to scan images for text to PDF convertion. Denying the option will lead to functionality problems!")
                    // Information for user that app won't be functional without camera permission
                } else if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                    // First time run or means that user denied permission permanently, cannot proceed further - provide way for user to app settings
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION)
                }
            }
        }
        getText.setOnClickListener {
            scanTextFromImage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission is granted, Proceed with button action
                    startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_RQ) // Go to onActivityResult()
                }
                !shouldShowRequestPermissionRationale(permissions[0]) -> {
                    // Means that user denied permission permanently, cannot proceed further - provide way for user to app settings
                    val appSettings = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
                    appSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK // Create new tab for app settings instead of overwriting working application
                    startActivity(appSettings) // Go to permission settings
                }
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_RQ && resultCode == RESULT_OK){
            imageHolder.setImageBitmap(data?.extras?.get("data") as Bitmap) // Display photo to test image view
            // Later convert everything to fragment
        }
    }

    private fun scanTextFromImage(){
        val firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageHolder.drawToBitmap())
        val firebaseTextDetector = FirebaseVision.getInstance().onDeviceTextRecognizer
        val result = firebaseTextDetector.processImage(firebaseVisionImage).addOnSuccessListener {
            Toast.makeText(applicationContext, it.text, Toast.LENGTH_SHORT).show()
        }
    }
}