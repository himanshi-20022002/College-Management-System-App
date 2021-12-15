package com.example.himanshi2002.project2.activities.faculty

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.model.TimeTableDetails
import java.util.ArrayList
import android.util.Log
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.Dashboard
import com.example.himanshi2002.project2.Utils.TimeTableAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class TimeTable : Fragment()
{
    private lateinit var recyclerViewTimeTable: RecyclerView
    private lateinit var timeTableArrayList: ArrayList<TimeTableDetails>
    private lateinit var myAdapter: TimeTableAdapter
private lateinit var thiscontext:Context
private  var  bundle:Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as DashboardFaculty?)
            ?.setActionBarTitle("Time Table")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        if (container != null) {
            thiscontext = container.context
        }
        bundle =savedInstanceState
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_time_table_faculty, container, false)

        setUpRecyclerView(view)
        return view
    }

    private fun setUpRecyclerView(view: View) {
        recyclerViewTimeTable = view.findViewById(R.id.recyclerViewForTimeTable)
        val query=FirebaseFirestore.getInstance().collection(Constants.TIMETABLE).orderBy("timeTableClass",
            Query.Direction.DESCENDING)
        val option: FirestoreRecyclerOptions<TimeTableDetails> = FirestoreRecyclerOptions.Builder<TimeTableDetails>().setQuery(query,
            TimeTableDetails::class.java).build()


        myAdapter= TimeTableAdapter(option,bundle)

        recyclerViewTimeTable.adapter=myAdapter
        recyclerViewTimeTable.layoutManager=LinearLayoutManager(requireContext())
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