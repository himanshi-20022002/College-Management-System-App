package com.example.himanshi2002.project2.activities.faculty.assignment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.activities.faculty.DashboardFaculty

import com.example.himanshi2002.project2.model.AssignmentDetails
import com.example.himanshi2002.project2.model.FacultyDetails
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AssFragment : Fragment() {
    private lateinit var add_ass_btn: FloatingActionButton
    private lateinit var main_recyclerViewAssignmentFaculty: RecyclerView
    lateinit var myAdapter: AssAdapter
    private  var bundle: Bundle?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            bundle=savedInstanceState

        }
        (activity as DashboardFaculty?)
            ?.setActionBarTitle("Assignment")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_faculty_assignment, container, false)
        Log.d("assignment","crashes1")
        add_ass_btn=view.findViewById(R.id.add_ass_btn)
        add_ass_btn.setOnClickListener{
            val i= Intent(activity, CreateNewAssignment::class.java)
            startActivity(i)
        }
        Log.d("assignment","crashes2")

        val getArgument = requireArguments().getString("data")
        Log.d("get argurmet",getArgument.toString())


        setUpRecyclerView(view,getArgument)
        return view
    }

    private fun setUpRecyclerView(view: View, getArgument: String?)
    {
        main_recyclerViewAssignmentFaculty=view.findViewById(R.id.main_recyclerViewAssignmentFaculty)

        val query: Query = FirebaseFirestore.getInstance().collection(Constants.ASSIGNMENT)
            .whereEqualTo("createdBy.faculty_ID",getArgument).orderBy("date_of_assigned",
                Query.Direction.DESCENDING)

        val option: FirestoreRecyclerOptions<AssignmentDetails> = FirestoreRecyclerOptions.Builder<AssignmentDetails>().setQuery(query,
            AssignmentDetails::class.java).build()

        myAdapter= AssAdapter(option,bundle,activity)
        main_recyclerViewAssignmentFaculty.adapter=myAdapter

        main_recyclerViewAssignmentFaculty.layoutManager= LinearLayoutManager(requireContext())

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


