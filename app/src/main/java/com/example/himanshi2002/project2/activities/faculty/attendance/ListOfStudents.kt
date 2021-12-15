package com.example.himanshi2002.project2.activities.faculty.attendance

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.model.StudentDetails
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.view.MenuItem
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.himanshi2002.project2.Dashboard
import com.example.himanshi2002.project2.Utils.ConnectionReceiver
import com.example.himanshi2002.project2.Utils.FireStoreClass
import com.example.himanshi2002.project2.activities.faculty.DashboardFaculty
import com.example.himanshi2002.project2.model.FacultyDetails


class ListOfStudents : AppCompatActivity()
{
    private lateinit var recyclerView: RecyclerView
    private lateinit var studentArrayList: ArrayList<StudentDetails>
    private lateinit var myAdapter: ListOfStudentsAdapter
    private lateinit var btn:Button

    lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_students)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null)
        {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "Attendance"
            actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.action_bar_bg))

        }

        broadcastReceiver= ConnectionReceiver()
        registerNetworkBroadcast()

        btn=findViewById(R.id.save)
        recyclerView=findViewById(R.id.recyclerViewForStudents)
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        studentArrayList= arrayListOf()

        myAdapter= ListOfStudentsAdapter(studentArrayList)

        recyclerView.adapter=myAdapter
        btn.setOnClickListener {

            val subjectID = intent.getStringExtra("SubjectID")


            val c = Calendar.getInstance().time
            println("Current time => $c")

            val df = SimpleDateFormat("dd-MMM-yyyy")
            val formattedDate = df.format(c)
            Log.d("format date",formattedDate)


            val city = hashMapOf(
                subjectID to myAdapter.attendance_map,
                )
            val db = FirebaseFirestore.getInstance()
            db.collection(Constants.ATTENDANCE).document(formattedDate)
                .set(city, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                    Toast.makeText(applicationContext,"Attendance done",Toast.LENGTH_SHORT).show()
                    val i=Intent(this,DashboardFaculty::class.java)
                    startActivity(i)
                    finish()

                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }

        eventChangedListener()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun eventChangedListener()
    {
        val courseID=intent.getStringExtra("CourseID")
        val mFireStore= FirebaseFirestore.getInstance()
        GlobalScope.launch(Dispatchers.IO)
        {
            try
            {
                val item =mFireStore.collection(Constants.STUDENT).whereEqualTo("Course_ID", courseID)
                    .get().await()
                for (document in item)
                {
                    studentArrayList.add(document.toObject(StudentDetails::class.java))
                }
            }
            catch(e: FirebaseFirestoreException)
            {
                Log.d("documents is ", "Error getting documents: ")
            }
            withContext(Dispatchers.Main)
            {
                myAdapter.notifyDataSetChanged()
            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var facultyD: FacultyDetails
        GlobalScope.launch(Dispatchers.IO) {
            facultyD =
                FireStoreClass().getCurrentFaculty().await()
                    .toObject(FacultyDetails::class.java)!!

            withContext(Dispatchers.Main)
            {
                val myIntent = Intent(applicationContext, Attendance::class.java)
                myIntent.putExtra("ID", facultyD.faculty_ID)
                startActivityForResult(myIntent, 0)



            }
        }
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
