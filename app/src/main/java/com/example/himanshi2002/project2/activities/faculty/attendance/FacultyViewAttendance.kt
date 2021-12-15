package com.example.himanshi2002.project2.activities.faculty.attendance

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.model.StudentDetails
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList

import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.himanshi2002.project2.Utils.ConnectionReceiver
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.*
import java.text.SimpleDateFormat

class FacultyViewAttendance : AppCompatActivity()
{
    lateinit var tv_total: TextView
    lateinit var tv_present: TextView
    lateinit var tv_absent: TextView
    lateinit var date: TextView
    lateinit var btnCheckStatus: Button
    lateinit var status: TextView

    lateinit var broadcastReceiver: BroadcastReceiver
   
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val dialog = ProgressDialog.show(this, "Loading...", "Please wait...", true)
        setContentView(R.layout.activity_faculty_view_attendance)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "Attendance"
            actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.action_bar_bg))

        }

        broadcastReceiver= ConnectionReceiver()
        registerNetworkBroadcast()
        val spinner = findViewById<Spinner>(R.id.spinner)

        tv_total=findViewById(R.id.tv_total)
        tv_present=findViewById(R.id.tv_present)
        tv_absent=findViewById(R.id.tv_absent)
        date=findViewById(R.id.date)
        btnCheckStatus=findViewById(R.id.btnCheckStatus)
        status=findViewById(R.id.status)

        val subjectId:String=intent.getStringExtra("SubjectID").toString()
        Log.d("subjectid",subjectId.toString())


        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MMM-yyyy")
        val formattedDate = df.format(c)
        Log.d("format date",formattedDate)
        date.text = formattedDate

        val calender=Calendar.getInstance()
        val year=calender.get(Calendar.YEAR)
        val month=calender.get(Calendar.MONTH)
        val day=calender.get(Calendar.DAY_OF_MONTH)
        var dateString=formattedDate
        var selectedItem:String = ""

        date.setOnClickListener{
            val dpd= DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener{ view, mYear, mMon: Int, mDay->

                Log.d("day",mDay.toString())
                Log.d("day",mMon.toString())
                Log.d("day",mYear.toString())
                var month=mMon+1
                date.text = "$mDay-$month-$mYear"
                calender.set(mYear,mMon,mDay)

                /*val format = SimpleDateFormat("dd-MMM-yyyy")*/
                dateString = df.format(calender.time)
                Log.d("date",dateString)
            },year,month,day)
            dpd.show()
        }
        btnCheckStatus.setOnClickListener {
            getStatus(dateString,subjectId,selectedItem)
        }


        val studentList: MutableList<String> = ArrayList()


        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, studentList)
        spinner.adapter = dataAdapter


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
                    val student:StudentDetails=document.toObject(StudentDetails::class.java)
                    studentList.add(student.student_ID)
                }
            }
            catch(e: FirebaseFirestoreException)
            {
                Log.d("documents is ", "Error getting documents: ")
            }

            withContext(Dispatchers.Main)
            {
                spinner.adapter = dataAdapter
            }

        }

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItem =
                    parent?.getItemAtPosition(
                        position).toString()
                Log.d("selected item",selectedItem)

                GlobalScope.launch(Dispatchers.IO)
                {

                    var total:Int=0
                    var present:Int=0
                    var absent:Int=0
                    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                    try
                    {

                        val item = db.collection(Constants.ATTENDANCE)
                            .get().await()

                        for(document in item)
                        {
                            Log.d("document is ", "${document.id} => ${document.data}")
                            val status : Map<*,*> = document.get(subjectId) as Map<*, *>
                            Log.d("status is",status.toString())
                            if(status.isNullOrEmpty())
                            {
                                Log.d("inside","continue")
                                continue
                            }
                            else
                            {
                                total++
                                if(status[selectedItem]=="P")
                                {
                                    present++
                                }
                                Log.d("s is ", status[selectedItem].toString())
                                for ((k, v) in status) {
                                    Log.d("key",k.toString())
                                    Log.d("value",v.toString())
                                    println("$k = $v")
                                }
                            }

                        }
                        Log.d("count",total.toString())
                        Log.d("present",present.toString())
                        absent=total-present
                        Log.d("absent ",absent.toString())
                    }
                    catch(e: FirebaseFirestoreException)
                    {
                        Log.d("document5 is ", "Error getting documents: ")
                    }
                    withContext(Dispatchers.Main)
                    {

                        dialog.dismiss()
                        tv_total.text=total.toString()
                        tv_present.text=present.toString()
                        tv_absent.text= absent.toString()

                    }
                }



            }

        }




    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        onBackPressed()
        return true
    }


    private fun getStatus(text: String, subjectId: String, selectedItem: String)
    {
        GlobalScope.launch(Dispatchers.IO)
        {

            val db: FirebaseFirestore = FirebaseFirestore.getInstance()
            var item: DocumentSnapshot? =null
            try {

                Log.d("text getting", text)
                item = db.collection(Constants.ATTENDANCE).document(text)
                    .get().await()
                Log.d("exists",item.exists().toString())
            }
            catch(e: FirebaseFirestoreException)
            {
                Log.d("document5 is ", "Error getting documents: ")
            }
            withContext(Dispatchers.Main)
            {
                Log.d("item is ", item.toString())
                if (item != null) {
                    if (!item.exists()) {
                        withContext(Dispatchers.Main)
                        {
                            status.text=""
                            Toast.makeText(applicationContext, "No information available", Toast.LENGTH_LONG)
                                .show()
                        }
                    } else {
                        val s = item.get(subjectId) as Map<*, *>

                        if (s.isNullOrEmpty()) {
                            Log.d("not ", "attendance taken")

                        } else {
                            status.text=s[selectedItem].toString()

                            Log.d("s is ", s[selectedItem].toString())
                        }
                    }
                }
            }

        }
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