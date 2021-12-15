package com.example.himanshi2002.project2.activities.faculty.attendance

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
import com.example.himanshi2002.project2.model.SubjectsDetails
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore


class TakeAttendanceFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: ListOfClassesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_take_attendance, container, false)

        setUpRecyclerView(view)
        return view
    }

    fun setUpRecyclerView(view: View) {
        Log.d("inside ", "set up")
        recyclerView = view.findViewById(R.id.recyclerView)
        val facultyID: String? = activity?.intent?.getStringExtra("ID")
        Log.d("facultyid", facultyID.toString())

        val query = FirebaseFirestore.getInstance().collection(Constants.SUBJECT)
            .whereEqualTo("Faculty_ID", facultyID)

        val option: FirestoreRecyclerOptions<SubjectsDetails> =
            FirestoreRecyclerOptions.Builder<SubjectsDetails>().setQuery(query,
                SubjectsDetails::class.java).build()
        myAdapter = ListOfClassesAdapter(option)
        recyclerView.adapter = myAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)


    }

    override fun onStart() {
        super.onStart()
        Log.d("on start", "is called")

        myAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        Log.d("on stop", "is called")

        myAdapter.stopListening()
    }


}



