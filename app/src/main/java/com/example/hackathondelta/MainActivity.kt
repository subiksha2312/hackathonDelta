package com.example.hackathondelta

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var bCameraAccess = false
    private var bStorageAccess = false
    private val _CAMERAACCESSREQUEST= 101
    private val _STORAGEACCESSREQUEST = 102



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        checkCameraPermission()
        checkStoragePermission()

        if(savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.rootLayout,AllPhotos.newInstance("",""))
                .commit()
        }




    }

    private fun checkCameraPermission() {
        val cameraPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            Log.d("permission1.1", "Making camera request")
            makeCameraPermRequest()
        }
        else {
            Log.d("permission1.2", "Camera Permission already granted")
            bCameraAccess = true
        }
    }

    private fun checkStoragePermission() {
        val storagePerm = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        if (storagePerm != PackageManager.PERMISSION_GRANTED) {
            Log.d("permission2.1", "Making Storage perm request")
            makeStoragePermRequest()

        }
        else {
            Log.d("permission2.2", "Storage Permission already granted")
            bStorageAccess = true
        }
    }

    private fun makeCameraPermRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA),
            _CAMERAACCESSREQUEST)
    }

    private fun makeStoragePermRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            _STORAGEACCESSREQUEST)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            _CAMERAACCESSREQUEST -> {
                Log.d("permission1.3", "Receiving Camera permission")
                bCameraAccess = !(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
            }

            _STORAGEACCESSREQUEST -> {
                Log.d("permission2.3", "Receiving Storage permission")
                Log.d("permission2.4", "${grantResults[0]}, ${PackageManager.PERMISSION_GRANTED}, ${PackageManager.PERMISSION_DENIED}")
                bStorageAccess = !(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
            }
        }
    }








}