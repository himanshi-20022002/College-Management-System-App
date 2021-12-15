package com.example.himanshi2002.project2.activities.faculty.assignment

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.Utils.FireStoreClass
import com.example.himanshi2002.project2.activities.faculty.DashboardFaculty
import java.io.File
import java.io.IOException
import android.R.attr.path
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.himanshi2002.project2.Utils.ConnectionReceiver


class CreateNewAssignment : AppCompatActivity()
{
    private  lateinit var assignmentPosted:ImageView
    private lateinit var assignmentDescription:EditText
    lateinit var tv_click:TextView

    private lateinit var assignmentName:EditText
    private lateinit var subject_name:EditText
    private lateinit var lastDate:EditText
    private lateinit var assignmentButton:Button

    lateinit var mSelectedFileUri: Uri
    lateinit var mSelectedFile: String

    lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_assignment)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "Assignment"
            actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.action_bar_bg))

        }
        broadcastReceiver= ConnectionReceiver()
        registerNetworkBroadcast()

        assignmentPosted=findViewById(R.id.assignmentPosted)
        assignmentDescription=findViewById(R.id.assignmentDescription)
        tv_click=findViewById(R.id.tv_click)
       /* assignmentID=findViewById(R.id.assignmentID)*/
        assignmentName=findViewById(R.id.assignmentName)
        subject_name=findViewById(R.id.subject_name)
        lastDate=findViewById(R.id.lastDate)
        assignmentButton=findViewById(R.id.assignmentButton)

        assignmentPosted.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
            {
                Log.d("activity is ",this.toString())
                Constants.showFileChooser(this)
            }
            else
            {
                Log.d("activity is ",this.toString())
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.ADD_PERMISSION_CODE)
            }
        }


        assignmentButton.setOnClickListener{
            if (mSelectedFileUri != null)
            {
                FireStoreClass().uploadAssignmentToCloudStorage(this,mSelectedFileUri,
                    assignmentDescription.text.toString(),
                    assignmentName.text.toString(),
                    subject_name.text.toString(),lastDate.text.toString())

            }
        }
    }

    fun imageUploadSuccess(img: String)
    {
         mSelectedFile = img
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
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
                       Log.d("path is", path.toString())
                        val filename: String? = path?.substring(path?.lastIndexOf("/")+1)
                        Log.d("filename is ",filename.toString())
                        tv_click.text=filename
                        /*GlideLoader(this).loadUserPicture(mSelectedFileUri,assignmentPosted)*/

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
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        val myIntent = Intent(applicationContext, DashboardFaculty::class.java)
        startActivityForResult(myIntent, 0)
        return true
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
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
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

    override fun onStop() {
        super.onStop()
        unregisterNetwork()
    }




    override fun onStart() {
        super.onStart()
        broadcastReceiver= ConnectionReceiver()
        registerNetworkBroadcast()
    }
}