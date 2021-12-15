package com.example.himanshi2002.project2.activities.student.ui.attendance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.Utils.FireStoreClass
import com.example.himanshi2002.project2.model.StudentDetails
import com.example.himanshi2002.project2.model.SubjectsDetails
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class AttendanceFragment : Fragment(){

   private lateinit var recyclerView: RecyclerView
    private lateinit var subjectArrayList: ArrayList<SubjectsDetails>
    private lateinit var myAdapter: ListOfSubjectsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view:View=inflater.inflate(R.layout.fragment_attendance, container, false)

        recyclerView=view.findViewById(R.id.recyclerViewSubjectList)
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

        subjectArrayList= arrayListOf()

        myAdapter= ListOfSubjectsAdapter(subjectArrayList)

        recyclerView.adapter=myAdapter
        eventChangedListener()
        return view
    }

    private fun eventChangedListener() {
        var studentInfo:StudentDetails = StudentDetails()
        Log.d("check "," whether data saved1")
        Log.d("check ",this.requireActivity().intent.hasExtra(Constants.STUDENT_DETAILS).toString())
        if(this.requireActivity().intent.hasExtra(Constants.STUDENT_DETAILS))
        {
            studentInfo=this.requireActivity().intent.getParcelableExtra(Constants.STUDENT_DETAILS)!!
            Log.d("check "," whether data saved2")
        }
        val mFirestore= FirebaseFirestore.getInstance()
        var subjects:SubjectsDetails= SubjectsDetails()

            GlobalScope.launch(Dispatchers.IO) {
                val student=FireStoreClass().getCurrentStudent().await().toObject(StudentDetails::class.java)
                try {
                    val item=mFirestore.collection(Constants.SUBJECT)
                        .whereEqualTo("Course_ID", student?.Course_ID)
                        .get().await()
                    for (document in item)
                    {
                        Log.d("inside","for loop")
                        Log.d("document is ","${document.id} => ${document.data}")
                        subjects=document.toObject(SubjectsDetails::class.java)
                        subjectArrayList.add(document.toObject(SubjectsDetails::class.java))

                    }
                }
                catch (e: FirebaseFirestoreException) {
                    Log.d("document5 is ", "Error getting documents: ")
                }
                withContext(Dispatchers.Main)
                {
                    myAdapter.notifyDataSetChanged()
                }

            }

        }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}


