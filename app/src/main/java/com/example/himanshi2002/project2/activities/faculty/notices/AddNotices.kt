package com.example.himanshi2002.project2.activities.faculty.notices

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.Utils.FireStoreClass
import com.example.himanshi2002.project2.Utils.GlideLoader
import com.example.himanshi2002.project2.activities.faculty.DashboardFaculty
import java.io.IOException

class AddNotices : AppCompatActivity() {

    lateinit var postFile:ImageView
    lateinit var desc:EditText
    lateinit var addNotice_btn:Button
    lateinit var noticeID:EditText
    lateinit var textSampleNotice:TextView

    lateinit var mSelectedFileUri:Uri
    lateinit var mSelectedImage:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_notices)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "Add Notice"
            actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.action_bar_bg))

        }

        textSampleNotice=findViewById(R.id.textSampleNotice)

        postFile=findViewById(R.id.filePosted)
        desc=findViewById(R.id.noticeDescription)
        addNotice_btn=findViewById(R.id.post_btn)
    /*    noticeID=findViewById(R.id.noticeID)*/

        postFile.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
            {
                Log.d("activity is ",this.toString())
                Constants.showFileChooser(this as Activity)
            }
            else
            {
                Log.d("activity is ",this.toString())
                ActivityCompat.requestPermissions(
                    this as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.ADD_PERMISSION_CODE)
            }
        }

        addNotice_btn.setOnClickListener {
            if (mSelectedFileUri != null) {
                FireStoreClass().uploadFileToCloudStorage(
                    this,
                    mSelectedFileUri,
                    desc.text.toString())


            }
        }

    }

   /* override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_add_notices, container, false)

        return view
    }*/

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
                Constants.showFileChooser(this)

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
        Log.d("inside:","activity on result5")
        if(resultCode== Activity.RESULT_OK)
        {
            Log.d("inside:","activity on result1")
            if(requestCode==Constants.PICK_FILE_REQUEST_CODE)
            {
                Log.d("inside:","activity on result2")
                if(data!=null)
                {
                    Log.d("inside:","activity on result3")
                    try
                    {
                        Log.d("inside:","activity on result4")
                        mSelectedFileUri=data.data!!
                        val path: String? =data.data!!.path
                        val filename: String? = path?.substring(path?.lastIndexOf("/")+1)
                        Log.d("filename is ",filename.toString())
                        textSampleNotice.text=filename


                    }
                    catch(e: IOException)
                    {
                        e.printStackTrace()
                        Toast.makeText(this,"image selction falied", Toast.LENGTH_LONG).show()
                    }
                }
            }
            else if(resultCode== Activity.RESULT_CANCELED)
            {
                Toast.makeText(this,"no image selected", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun imageUploadSuccess(img: String)
    {
        mSelectedImage=img
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        val myIntent = Intent(applicationContext, DashboardFaculty::class.java)
        startActivityForResult(myIntent, 0)
        return true
    }

}