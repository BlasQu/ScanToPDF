package com.example.scantopdf

import android.animation.ObjectAnimator
import android.app.ActionBar
import android.app.Activity
import android.app.Notification
import android.app.SearchManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.provider.LiveFolders.INTENT
import android.provider.MediaStore
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.scantopdf.Data.Doc
import com.example.scantopdf.Fragments.DocumentsFragment
import com.example.scantopdf.MVVM.Viewmodel
import com.labters.documentscanner.ImageCropActivity
import com.labters.documentscanner.helpers.ScannerConstants
import kotlinx.android.synthetic.main.action_bar.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    val CAMERA_RQ = 101
    val CAMERA_PERMISSION = 1010
    val IMAGE_RQ = 102
    lateinit var viewmodel : Viewmodel
    lateinit var data : Bitmap
    lateinit var dataImage : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        relativeLayoutMainActivity.startAnimation(AnimationUtils.loadAnimation(this, R.anim.open_activity).apply {
            interpolator = FastOutSlowInInterpolator()
            startOffset = 1000
        })

        btnListeners()
        setToolbar()
        setupViewModel()
        Functions().translateScanner()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, DocumentsFragment(), "DOCUMENTS_FRAGMENT")
            commit()
        } // Adding fragment to container
    }

    fun setupViewModel() {
        viewmodel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(Viewmodel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val search = menu?.findItem(R.id.search)?.actionView as SearchView
        search.apply {
            findViewById<TextView>(R.id.search_src_text).setTextColor(resources.getColor(R.color.white))
            findViewById<ImageView>(R.id.search_button).setImageResource(R.drawable.ic_baseline_search_24)
            findViewById<ImageView>(R.id.search_close_btn).setImageResource(R.drawable.ic_baseline_close_24)
            queryHint = "'Receipt', 'Taxes' or 'Document'"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewmodel.searchString.postValue(newText)
                    return true
                }

            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sortby -> Functions().singleChoiceDialog(this)
        }
        return super.onOptionsItemSelected(item)
    }

    fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.apply {
            setDisplayShowTitleEnabled(false)
            setHomeButtonEnabled(true)
        }
    }

    fun getSharedPrefs(): Int {
       val prefs = getSharedPreferences("ScanToPDF", Context.MODE_PRIVATE)
       return prefs.getInt("sortBy", 0)
    }

    override fun onBackPressed() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        val fragmentSearch = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (fragmentSearch!!.tag.equals("ITEM_FRAGMENT")) {
            supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(R.anim.slide_from_left_enter, R.anim.slide_from_left_exit)
                replace(R.id.fragmentContainer, DocumentsFragment(), "DOCUMENTS_FRAGMENT")
                commit()
            } // Detect if item fragment is visible, if so on back pressed button take user back to documents fragment
            btn_camera.apply {
                visibility = View.VISIBLE
                isClickable = true
                startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_up))
            }
            btn_selectFromGallery.apply {
                visibility = View.VISIBLE
                isClickable = true
                startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_up))
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun btnListeners(){
        btn_camera.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, Proceed with button action
                    startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE),CAMERA_RQ)
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
        btn_selectFromGallery.setOnClickListener {
            pickImage()
        }
    }

    private fun pickImage(){
        val getImage = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(getImage, IMAGE_RQ) // User gets to select image from gallery
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
                    val appSettings = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName")).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK // Create new tab for app settings instead of overwriting working application
                    }
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
            dataImage = data?.extras?.get("data") as Bitmap// Get image
            ScannerConstants.selectedImageBitmap = dataImage
            startActivityForResult(Intent(this, ImageCropActivity::class.java), 110)
        }
        else if (requestCode == IMAGE_RQ && resultCode == RESULT_OK){
            dataImage = BitmapFactory.decodeStream(contentResolver.openInputStream(data?.data!!))
            ScannerConstants.selectedImageBitmap = dataImage
            startActivityForResult(Intent(this, ImageCropActivity::class.java), 110)
        } else if (requestCode == 110 && resultCode == RESULT_OK) {
            Functions().dialogAdd(this)
        }
        else {
            Toast.makeText(applicationContext, "There was an error! Try again!", Toast.LENGTH_SHORT).show()
        }
    }
}