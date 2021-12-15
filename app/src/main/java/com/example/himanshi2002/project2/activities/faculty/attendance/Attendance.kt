package com.example.himanshi2002.project2.activities.faculty.attendance

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.Utils.ConnectionReceiver
import com.example.himanshi2002.project2.activities.faculty.DashboardFaculty
import com.google.android.material.tabs.TabLayout

class Attendance : AppCompatActivity()
{
    lateinit var viewPager:ViewPager
    lateinit var tabLayout:TabLayout
    lateinit var broadcastReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?)
    {
        broadcastReceiver= ConnectionReceiver()
        registerNetworkBroadcast()

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "Attendance"
            actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.action_bar_bg))

        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        viewPager=findViewById(R.id.viewPager)
        tabLayout=findViewById(R.id.tabLayout)


        viewPager.adapter=VPAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onStart() {
        super.onStart()
        Log.d("on start","is called")
        broadcastReceiver= ConnectionReceiver()
        registerNetworkBroadcast()

    }

    override fun onStop() {
        super.onStop()
        Log.d("on stop","is called")
        unregisterNetwork()

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

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        val myIntent = Intent(applicationContext, DashboardFaculty::class.java)
        startActivityForResult(myIntent, 0)
        return true
    }
    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("on backpressed","is called")
        val i= Intent(this, DashboardFaculty::class.java)
        startActivity(i)
        finish()
    }
}