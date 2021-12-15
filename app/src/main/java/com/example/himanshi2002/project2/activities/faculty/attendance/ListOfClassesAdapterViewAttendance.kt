package com.example.himanshi2002.project2.activities.faculty.attendance

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.model.SubjectsDetails
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ListOfClassesAdapterViewAttendance(options: FirestoreRecyclerOptions<SubjectsDetails>):
    FirestoreRecyclerAdapter<SubjectsDetails, ListOfClassesAdapterViewAttendance.SubjectViewHolder>(options)  {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ListOfClassesAdapterViewAttendance.SubjectViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_subject_list, parent, false)
        return ListOfClassesAdapterViewAttendance.SubjectViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ListOfClassesAdapterViewAttendance.SubjectViewHolder,
        position: Int,
        model: SubjectsDetails,
    ) {
        Log.d("model is ",model.toString())
        holder.itemSubjectName.text = model.Subject_Name
        holder.itemSubjectID.text = model.Subject_ID
        holder.takeAttendance.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(v!!.context, FacultyViewAttendance::class.java)
                intent.putExtra("CourseID", model.Course_ID)
                intent.putExtra("SubjectID", model.Subject_ID)
                v.context.startActivity(intent)
            }

        })
    }

    class SubjectViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)
    {
        var itemImage: ImageView
        var itemSubjectName: TextView
        var itemSubjectID: TextView
        var takeAttendance: Button

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemSubjectName = itemView.findViewById(R.id.item_subjectName)
            itemSubjectID = itemView.findViewById(R.id.item_subjectID)
            takeAttendance = itemView.findViewById(R.id.attendance_btn)
            takeAttendance.text = "View Attendance"

        }
    }
}