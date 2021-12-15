package com.example.himanshi2002.project2.activities.student.ui.notices

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

class NoticesFragment : Fragment() {


    private lateinit var myAdapter: NoticesAdapter
    lateinit var recyclerViewNoticesForStudents:RecyclerView
    private  var bundle: Bundle?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as Dashboard?)
            ?.setActionBarTitle("Notices")

            bundle=savedInstanceState

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.fragment_notices, container, false)
        if (savedInstanceState != null) {
            bundle=savedInstanceState
        }
        setUpRecyclerView(view)
        return view
    }

    private fun setUpRecyclerView(view: View) {
        recyclerViewNoticesForStudents=view.findViewById(R.id.recyclerViewNoticesStudents)
        val query=FirebaseFirestore.getInstance().collection(Constants.NOTICES).orderBy("date_of_Issue",
            Query.Direction.DESCENDING)
        val option: FirestoreRecyclerOptions<NoticesDetails> = FirestoreRecyclerOptions.Builder<NoticesDetails>().setQuery(query,NoticesDetails::class.java).build()


        myAdapter= NoticesAdapter(option,bundle)

        recyclerViewNoticesForStudents.adapter=myAdapter
        recyclerViewNoticesForStudents.layoutManager=LinearLayoutManager(requireContext())

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