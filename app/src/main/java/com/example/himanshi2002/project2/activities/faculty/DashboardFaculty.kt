package com.example.himanshi2002.project2.activities.faculty

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.Utils.ConnectionReceiver
import com.example.himanshi2002.project2.Utils.FireStoreClass
import com.example.himanshi2002.project2.activities.FacultyOrStudent
import com.example.himanshi2002.project2.activities.LoginActivity
import com.example.himanshi2002.project2.model.FacultyDetails


import com.example.himanshi2002.project2.activities.faculty.gallery.GalleryFragment
import com.example.himanshi2002.project2.activities.faculty.assignment.AssFragment
import com.example.himanshi2002.project2.activities.faculty.attendance.Attendance
import com.example.himanshi2002.project2.activities.faculty.notices.FacultyNotices
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class DashboardFaculty : AppCompatActivity()
{
    lateinit var facultyProfileImage:ImageView

    lateinit var attendance_grid:CardView
    lateinit var facultyProfile:CardView
    lateinit var timeTable_grid:CardView
    lateinit var notices_grid:CardView
    lateinit var assignmentGrid:CardView
    lateinit var gallery_grid:CardView

    lateinit var broadcastReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard_faculty)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.title = "Edu4u"
            actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.action_bar_bg))

        }

        broadcastReceiver= ConnectionReceiver()
        registerNetworkBroadcast()


        facultyProfileImage=findViewById(R.id.facultyProfile)

        attendance_grid=findViewById(R.id.attendance_grid)
        facultyProfile=findViewById(R.id.my_Profile)
        timeTable_grid=findViewById(R.id.timeTable_grid)
        notices_grid=findViewById(R.id.notices_grid)
        assignmentGrid=findViewById(R.id.assignmentGrid)
        gallery_grid=findViewById(R.id.gallery_grid)
        var facultyD:FacultyDetails
        GlobalScope.launch(Dispatchers.IO) {
            facultyD =
                FireStoreClass().getCurrentFaculty().await()
                    .toObject(FacultyDetails::class.java)!!

            withContext(Dispatchers.Main)
            {
                val imageUri: Uri =facultyD.faculty_image.toUri()
                Glide.with(facultyProfileImage.context).load(facultyD.faculty_image).circleCrop().into(facultyProfileImage)


            }
        }



        attendance_grid.setOnClickListener{

            GlobalScope.launch(Dispatchers.IO) {
                facultyD =
                    FireStoreClass().getCurrentFaculty().await()
                        .toObject(FacultyDetails::class.java)!!

                withContext(Dispatchers.Main)
                {
                    val i = Intent(applicationContext, Attendance::class.java)

                   /* val faculty: FacultyDetails? =
                        intent.getParcelableExtra(Constants.FACULTY_DETAILS)
                    val faculty_ID: String? = faculty?.faculty_ID*/
                    i.putExtra("ID", facultyD.faculty_ID)
                    Log.d("extra", facultyD.faculty_ID.toString())
                    startActivity(i)
                    finish()
                }
            }

        }

        facultyProfile.setOnClickListener{
            /*if (actionBar != null) {
                actionBar.title = "Dashboard"
                actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.action_bar_bg))

            }*/
            supportFragmentManager.beginTransaction().replace(R.id.dashBoard,FacultyProfile()).addToBackStack(null).commit()
            Log.d("back count",fragmentManager.backStackEntryCount.toString())
        }

        timeTable_grid.setOnClickListener{
            supportFragmentManager.beginTransaction().replace(R.id.dashBoard,TimeTable()).addToBackStack(null).commit()
        }

        notices_grid.setOnClickListener{
            supportFragmentManager.beginTransaction().replace(R.id.dashBoard, FacultyNotices()).addToBackStack(null).commit()
        }

        assignmentGrid.setOnClickListener{

            GlobalScope.launch(Dispatchers.IO) {
                facultyD =
                    FireStoreClass().getCurrentFaculty().await()
                        .toObject(FacultyDetails::class.java)!!

                withContext(Dispatchers.Main)
                {
                    /*val i = Intent(applicationContext, TakeAttendance::class.java)*/

                    val mFragmentTransaction=supportFragmentManager.beginTransaction()
                    val mFragment = AssFragment()
                    val mBundle = Bundle()
                    mBundle.putString("data",facultyD.faculty_ID)
                    mFragment.arguments = mBundle
                    mFragmentTransaction.replace(R.id.dashBoard, mFragment).addToBackStack(null).commit()
                }
            }



        }
        gallery_grid.setOnClickListener{
            supportFragmentManager.beginTransaction().replace(R.id.dashBoard,GalleryFragment()).addToBackStack(null).commit()
        }



    }
    fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_button)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setBackgroundDrawable(resources.getDrawable(R.drawable.action_bar_bg))
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.facultydashboard,menu)
        return true
    }


    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {


       /* val myIntent = Intent(this, DashboardFaculty::class.java)
        startActivityForResult(myIntent, 0)
        return true*/
        return when (item.itemId) {
            R.id.action_settings -> {
                FirebaseAuth.getInstance().signOut()
                val intent=Intent(this, FacultyOrStudent::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this,"Logout succesfully", Toast.LENGTH_LONG).show()
                true



            }
            android.R.id.home -> {
                val myIntent = Intent(this, DashboardFaculty::class.java)
                startActivityForResult(myIntent, 0)
                true
            }



            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportActionBar!!.title = "Edu4u"
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setBackgroundDrawable(resources.getDrawable(R.drawable.action_bar_bg))
        /*val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false)
            actionBar.title = "Edu4u"
            actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.action_bar_bg))

        }*/

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