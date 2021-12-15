package com.example.himanshi2002.project2.activities.faculty.notices

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.Utils.NoticesAdapter
import com.example.himanshi2002.project2.model.NoticesDetails
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import android.content.Intent
import com.example.himanshi2002.project2.activities.faculty.DashboardFaculty


class FacultyNotices : Fragment() {

    private lateinit var adapter:NoticesAdapter
    private  var bundle: Bundle?=null
    lateinit var add_notice_btn:FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            bundle=savedInstanceState
        }
        (activity as DashboardFaculty?)
            ?.setActionBarTitle("Notices")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_faculty_notices, container, false)
        add_notice_btn=view.findViewById(R.id.Add_notice)
        if (savedInstanceState != null) {
            bundle=savedInstanceState
        }
        setUpRecyclerView(view)
        add_notice_btn.setOnClickListener{
            val intent = Intent(activity, AddNotices::class.java)
            startActivity(intent)
            /*requireActivity().supportFragmentManager.beginTransaction().replace(R.id.facultyNotices,AddNotices()).addToBackStack(null).commit()*/
        }
        return view
    }

    private fun setUpRecyclerView(view: View) {
        val recyclerViewNotices:RecyclerView=view.findViewById(R.id.recyclerViewNoticesFaculty)
        val query=FirebaseFirestore.getInstance().collection(Constants.NOTICES).orderBy("description",
            Query.Direction.DESCENDING)
        val option:FirestoreRecyclerOptions<NoticesDetails> = FirestoreRecyclerOptions.Builder<NoticesDetails>().setQuery(query,NoticesDetails::class.java).build()


        adapter= NoticesAdapter(option,bundle)

        recyclerViewNotices.adapter=adapter
        recyclerViewNotices.layoutManager=LinearLayoutManager(requireContext())



    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onResume() {
        super.onResume()
        adapter.startListening()
    }
    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }



}