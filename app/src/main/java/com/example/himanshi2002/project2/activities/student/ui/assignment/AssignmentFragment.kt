package com.example.himanshi2002.project2.activities.student.ui.assignment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.Dashboard
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.Utils.FireStoreClass

import com.example.himanshi2002.project2.model.AssignmentDetails
import com.example.himanshi2002.project2.model.StudentDetails
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class AssignmentFragment : Fragment()
{
    private lateinit var main_recyclerViewAssignment:RecyclerView
    lateinit var myAdapter:AssignmentAdapter
    private  var bundle: Bundle?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as Dashboard?)
            ?.setActionBarTitle("Assignment")

        bundle=savedInstanceState

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      val view: View = inflater.inflate(R.layout.fragment_assignment, container, false)

        setUpRecyclerView(view)
      return view
  }

    private fun setUpRecyclerView(view: View) {
        main_recyclerViewAssignment=view.findViewById(R.id.main_recyclerViewAssignment)

        val query= FirebaseFirestore.getInstance().collection(Constants.ASSIGNMENT).orderBy("date_of_assigned",
            Query.Direction.DESCENDING)

        Log.d("crashes","assignment here1")

        val option: FirestoreRecyclerOptions<AssignmentDetails> = FirestoreRecyclerOptions.Builder<AssignmentDetails>().setQuery(query,
            AssignmentDetails::class.java).build()
        Log.d("crashes","assignment here2")

        myAdapter= AssignmentAdapter(option,bundle)
        Log.d("crashes","assignment here3")
        main_recyclerViewAssignment.adapter=myAdapter
        Log.d("crashes","assignment here4")
        main_recyclerViewAssignment.layoutManager= LinearLayoutManager(requireContext())
        Log.d("crashes","assignment here5")

    }



    override fun onStart() {
        super.onStart()
        Log.d("crashes","assignment here6")
        myAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        myAdapter.stopListening()
    }
}
