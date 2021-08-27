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
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var bCameraAccess = false
    private var bStorageAccess = false
    private val _CAMERAACCESSREQUEST= 101
    private val _STORAGEACCESSREQUEST = 102
    private val photoList = mutableListOf<Photos>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkCameraPermission()
        checkStoragePermission()
        getListOfImages()

        val mLayoutMgr = GridLayoutManager(this,2)
        photosEditList.layoutManager = mLayoutMgr
        val onItemClicked = {position: Int -> onPhotoClicked(position)}
        photosEditList.adapter = PhotosAdapter(photoList,onItemClicked)

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


    fun onPhotoClicked(position: Int) {


    }


    fun getListOfImages() {

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.RELATIVE_PATH,
            "_data"
        )

        val cursor: Cursor? = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,

            projection,
            null,
            null,
            null
        )

        Log.d("index4","${cursor!!.count}")

        if (cursor != null && cursor.count > 0) {
            var count =0

            while (cursor.moveToNext()) {

               val idIndex = cursor.getColumnIndex("_id")
              // val heightIndex = cursor.getColumnIndex("height")
               val dispNameIndex = cursor.getColumnIndex("_display_name")
              // val dateTakenIndex = cursor.getColumnIndex("datetaken")
               val relativePathIndex = cursor.getColumnIndex("relative_path")
               val titleIndex = cursor.getColumnIndex("title")
               val uriIndex = cursor.getColumnIndex("_data")
               val imageUri = Uri.parse("file:///" + cursor.getString(uriIndex))
               Log.d("index3","${cursor.getString(uriIndex)}")

               var objectPhotos : Photos = Photos(

                   cursor.getString(idIndex),
                   "",
                   "",
                   "",
                   "",
                   "",
                   imageUri

               )

               photoList.add(objectPhotos)
               //songsInfo[count].songId = cursor.getString(idIndex)
               Log.d("index1","${photoList[count].photoId}," +
                       "${photoList[count].height}," +
                       "${photoList[count].dateTaken}," +
                       "${photoList[count].photoUri},"+
                       "${photoList[count].relativePath},"+
                       "${photoList[count].title},"
                        )
               count++


            }


        }

    }



}