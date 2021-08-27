package com.example.hackathondelta

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_all_photos.*
import kotlinx.android.synthetic.main.photodetails.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllPhotos.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllPhotos : Fragment() {
    private val photoList = mutableListOf<Photos>()
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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


        val cursor: Cursor? = activity?.contentResolver?.query(
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_all_photos, container, false)
        val activity = activity as Context
        getListOfImages()
        var photosEditList = view.findViewById(R.id.photosEditList) as RecyclerView
        photosEditList.layoutManager = GridLayoutManager(activity,2)
        val onItemClicked = {position: Int -> onPhotoClicked(position)}
        photosEditList.adapter = PhotosAdapter(photoList,onItemClicked, activity)

        return view
    }


    internal inner class PhotosAdapter(private val photosList: List<Photos>,
                        private val onItemClicked: (position: Int) -> Unit, activityContext: Context): RecyclerView.Adapter<PhotosAdapter.ViewHolder> ()
    {
        private val layoutInflater = LayoutInflater.from(activityContext)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosAdapter.ViewHolder {
            val view = layoutInflater.inflate(R.layout.photodetails,parent,false)
            return ViewHolder(view, onItemClicked)
        }

        override fun onBindViewHolder(holder: PhotosAdapter.ViewHolder, position: Int) {
            holder.bindPhotos(photosList[position])

        }

        override fun getItemCount(): Int {
            return photosList.size

        }

        internal inner class ViewHolder(val view: View, private val onItemClicked: (position: Int) -> Unit):
            RecyclerView.ViewHolder(view), View.OnClickListener{

            init {
                itemView.setOnClickListener(this)
            }

            fun bindPhotos(photos: Photos) {
                itemView.theImage.setImageURI(photos.photoUri)
            }

            override fun onClick(v: View?) {
                val position = adapterPosition
                onItemClicked(position)
            }
        }

    }

    fun onPhotoClicked(position: Int) {

           Log.d("photo1", "onPhotovclicked")
           val bundle: Bundle = Bundle()
           bundle.putString("photoUri", photoList[position].photoUri.toString())
           val photoDisplay: PhotoDisplay = PhotoDisplay()
           photoDisplay.arguments = bundle
          // photoDisplay.arguments?.putBundle("photo", bundle)

           val manager = activity?.supportFragmentManager
           val transaction = manager?.beginTransaction()
           transaction?.replace(R.id.rootLayout,photoDisplay,"photoItem")
           transaction?.addToBackStack(null)
           transaction?.commit()



    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AllPhotos.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllPhotos().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}