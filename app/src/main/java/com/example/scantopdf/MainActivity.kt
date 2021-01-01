package com.example.scantopdf

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val CAMERA_RQ = 101
    private val CAMERA_PERMISSION = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnListeners()
    }

    private fun btnListeners(){
        btn_camera.setOnClickListener {
            //startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_RQ)
        }
    }

    private fun checkForPermissions(
        permission: String,
        code: Int
    ) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Permission is granted, Proceed with action
            }
            else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                // Denied once
            } else {
                //!shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)
                // Means that denied permanently, cannot proceed further, provide way for user to app settings
            }
        }
    }
}