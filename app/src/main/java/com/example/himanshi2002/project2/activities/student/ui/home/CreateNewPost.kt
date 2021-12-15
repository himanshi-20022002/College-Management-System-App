package com.example.himanshi2002.project2.activities.student.ui.home

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.himanshi2002.project2.R

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View.GONE
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.Utils.ConnectionReceiver
import com.example.himanshi2002.project2.Utils.FireStoreClass
import com.example.himanshi2002.project2.Utils.GlideLoader
import com.example.himanshi2002.project2.activities.BasicActivity
import com.example.himanshi2002.project2.model.FacultyDetails
import com.example.himanshi2002.project2.model.StudentDetails
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.IOException

class CreateNewPost : BasicActivity()
{

    lateinit var postButton:Button
    lateinit var postImage: ImageView
    lateinit var postTitle:TextView
    lateinit var sampleText:TextView
/*    lateinit var postId:EditText*/
    lateinit var mSelectedImageUri:Uri
    lateinit var mSelectedImage:String

    lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_post)

        broadcastReceiver= ConnectionReceiver()
        registerNetworkBroadcast()
        sampleText=findViewById(R.id.sampleText)
        postButton=findViewById(R.id.postButton)
        postImage=findViewById(R.id.ImagePosted)
       /* postId=findViewById(R.id.postID)*/
        postTitle=findViewById(R.id.postDescription)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "New Post"
            actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.action_bar_bg))

        }

        postImage.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
            {
                Constants.showImageChooser(this)
            }
            else
            {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),Constants.ADD_PERMISSION_CODE)
            }
        }
        postButton.setOnClickListener{
            if(mSelectedImageUri != null)
            {

                FireStoreClass().uploadFileToCloudStorage(
                    this,
                    mSelectedImageUri,
                    postTitle.text.toString())






            }


        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==Constants.ADD_PERMISSION_CODE)
        {
            if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Constants.showImageChooser(this)
                //showErrorSnackBar("Your permission is granted",false)
            }
            else
            {
                Toast.makeText(this,"permission Denied",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK)
        {
            if(requestCode==Constants.PICK_IMAGE_REQUEST_CODE)
            {
                if(data!=null)
                {
                    try
                    {
                        mSelectedImageUri=data.data!!
                        sampleText.visibility= GONE
                        GlideLoader(this).loadUserPicture(mSelectedImageUri,postImage)
                        //postInput.setImageURI(Uri.parse(selectedImageFileUri.toString()))
                    }
                    catch(e:IOException)
                    {
                        e.printStackTrace()
                        Toast.makeText(this,"image selction falied",Toast.LENGTH_LONG).show()
                    }
                }
            }
            else if(resultCode==Activity.RESULT_CANCELED)
            {
                Toast.makeText(this,"no image selected",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun imageUploadSuccess(img: String)
    {
        mSelectedImage=img
    }



    private fun registerNetworkBroadcast()
    {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N)
        {
            Log.d("inside ","register network")
            registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }

    private fun unregisterNetwork()
    {
        try{
            unregisterReceiver(broadcastReceiver)
        }
        catch (e: IllegalArgumentException)
        {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetwork()
    }





    override fun onStart() {
        super.onStart()
        broadcastReceiver= ConnectionReceiver()
        registerNetworkBroadcast()
    }


}

