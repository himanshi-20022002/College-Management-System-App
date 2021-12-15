package com.example.himanshi2002.project2.activities.student.ui.timetable

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.Dashboard
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.Utils.NoticesAdapter
import com.example.himanshi2002.project2.Utils.TimeTableAdapter
import com.example.himanshi2002.project2.activities.faculty.DashboardFaculty
import com.example.himanshi2002.project2.model.NoticesDetails
import com.example.himanshi2002.project2.model.TimeTableDetails
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.ArrayList

class TimeTableFragment : Fragment() {


    private lateinit var recyclerViewTimeTableStudents: RecyclerView
    private lateinit var timeTableArrayList: ArrayList<TimeTableDetails>
    private lateinit var myAdapter: TimeTableAdapter
    private  var  bundle:Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as Dashboard?)
            ?.setActionBarTitle("Time Table")
        bundle=savedInstanceState
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_time_table, container, false)

        setUpRecyclerView(view)
   /*     recyclerViewTimeTableStudents = view.findViewById(R.id.recyclerViewTimeTableStudents)


        recyclerViewTimeTableStudents.layoutManager = LinearLayoutManager(activity)
        recyclerViewTimeTableStudents.setHasFixedSize(true)

        timeTableArrayList = arrayListOf()



        myAdapter = TimeTableAdapter(timeTableArrayList,options)

        recyclerViewTimeTableStudents.adapter = myAdapter
        eventChangedListener()*/
        return view
    }

    private fun setUpRecyclerView(view: View) {
        Log.d("set up","recyclerview1")
        recyclerViewTimeTableStudents=view.findViewById(R.id.recyclerViewTimeTableStudents)
        Log.d("set up","recyclerview2")
        val query=FirebaseFirestore.getInstance().collection(Constants.TIMETABLE).orderBy("timeTableClass",
            Query.Direction.DESCENDING)
        Log.d("set up","recyclerview3")
        val option: FirestoreRecyclerOptions<TimeTableDetails> = FirestoreRecyclerOptions.Builder<TimeTableDetails>().setQuery(query,
            TimeTableDetails::class.java).build()
        Log.d("set up","recyclerview4")

        myAdapter= TimeTableAdapter(option,bundle)
        Log.d("set up","recyclerview5")
        recyclerViewTimeTableStudents.adapter=myAdapter
        Log.d("set up","recyclerview6")
        recyclerViewTimeTableStudents.layoutManager=LinearLayoutManager(requireContext())
        Log.d("set up","recyclerview7")
    }
    override fun onStart() {
        super.onStart()
        myAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        myAdapter.stopListening()
    }


}