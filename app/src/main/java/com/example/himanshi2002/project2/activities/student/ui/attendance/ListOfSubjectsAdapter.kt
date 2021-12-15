package com.example.himanshi2002.project2.activities.student.ui.attendance

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.model.SubjectsDetails

class ListOfSubjectsAdapter (private val subjectList:ArrayList<SubjectsDetails>):
    RecyclerView.Adapter<ListOfSubjectsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cardview_subject_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListOfSubjectsAdapter.MyViewHolder, position: Int) {
        val subject: SubjectsDetails = subjectList[position]
        holder.itemSubjectName.text = subject.Subject_Name
        holder.itemSubjectID.text = subject.Subject_ID
        holder.takeAttendance.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(v!!.context, ViewAttendance::class.java)
                intent.putExtra("CourseID", subject.Course_ID)
                intent.putExtra("SubjectID", subject.Subject_ID)
                v.context.startActivity(intent)
            }

        })



    }

    override fun getItemCount(): Int {
        return subjectList.size
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)/*,View.OnClickListener*/ {
        var itemImage: ImageView
        var itemSubjectName: TextView
        var itemSubjectID: TextView
        var takeAttendance: Button

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemSubjectName = itemView.findViewById(R.id.item_subjectName)
            itemSubjectID = itemView.findViewById(R.id.item_subjectID)
            takeAttendance = itemView.findViewById(R.id.attendance_btn)
            takeAttendance.text="View Attendance"
            /*takeAttendance.setOnClickListener(this)*/
        }
    }
}