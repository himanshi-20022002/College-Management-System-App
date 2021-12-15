package com.example.himanshi2002.project2

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.activities.BasicActivity
import com.example.himanshi2002.project2.databinding.ActivityDashboardBinding

import android.view.View
import android.widget.ImageView

import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.himanshi2002.project2.Utils.ConnectionReceiver
import com.example.himanshi2002.project2.Utils.FireStoreClass
import com.example.himanshi2002.project2.model.StudentDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

import android.view.MenuItem
import android.widget.Toast
import com.example.himanshi2002.project2.activities.FacultyOrStudent
import com.example.himanshi2002.project2.activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth


class Dashboard : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)


        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarDashboard.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_profile, R.id.nav_home, R.id.nav_assignment, R.id.nav_slideshow,R.id.nav_notices,R.id.nav_timetable, R.id.nav_busTracking
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val hView: View = navigationView.getHeaderView(0)
        val userImage:ImageView=hView.findViewById(R.id.profileView)
        val title:TextView=hView.findViewById(R.id.name)
        val roll:TextView=hView.findViewById(R.id.rollno)
        var student:StudentDetails= StudentDetails()
        GlobalScope.launch {
            student =
                FireStoreClass().getCurrentStudent().await().toObject(StudentDetails::class.java)!!
            withContext(Dispatchers.Main){
                Glide.with(userImage.context).load(student.profile_image).circleCrop().into(userImage)
                title.text=student.first_Name+student.last_Name
                roll.text=student.student_ID
            }
        }
        Log.d("check ",intent.hasExtra(Constants.STUDENT_DETAILS).toString())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.dashboard, menu)

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_settings -> {
                FirebaseAuth.getInstance().signOut()
                val intent=Intent(this, FacultyOrStudent::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this,"Logout succesfully",Toast.LENGTH_LONG).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }





}