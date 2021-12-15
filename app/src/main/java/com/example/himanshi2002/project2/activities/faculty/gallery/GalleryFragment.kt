package com.example.himanshi2002.project2.activities.faculty.gallery

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.activities.faculty.DashboardFaculty
import com.example.himanshi2002.project2.activities.student.ui.home.CreateNewPost
import com.example.himanshi2002.project2.activities.student.ui.home.PostAdapter
import com.example.himanshi2002.project2.model.PostDetails
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class GalleryFragment : Fragment() {

    private lateinit var recyclerViewPostFaculty: RecyclerView
    private lateinit var myAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as DashboardFaculty?)
            ?.setActionBarTitle("Gallery")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_gallery, container, false)

        setUpRecyclerView(view)

        return view
    }
    private fun setUpRecyclerView(view: View) {

        recyclerViewPostFaculty = view.findViewById(R.id.recyclerViewForPostFaculty)
        val query= FirebaseFirestore.getInstance().collection(Constants.POST).orderBy("createdAt",
            Query.Direction.DESCENDING)

        val option: FirestoreRecyclerOptions<PostDetails> = FirestoreRecyclerOptions.Builder<PostDetails>().setQuery(query,
            PostDetails::class.java).build()

        myAdapter= PostAdapter(option)
        recyclerViewPostFaculty.adapter=myAdapter
        recyclerViewPostFaculty.layoutManager= LinearLayoutManager(requireContext())

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