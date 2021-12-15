package com.example.himanshi2002.project2.activities.student.ui.assignment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.Utils.FireStoreClass
import com.example.himanshi2002.project2.model.AssignmentDetails
import com.example.himanshi2002.project2.model.StudentDetails
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AssignmentSubmitAdapter(options: FirestoreRecyclerOptions<AssignmentDetails>, private val bundle: Bundle?)
    :
    FirestoreRecyclerAdapter<AssignmentDetails, AssignmentSubmitAdapter.AssignmentViewHolder>(
        options as FirestoreRecyclerOptions<AssignmentDetails>){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AssignmentSubmitAdapter.AssignmentViewHolder {
        val viewHolder = AssignmentSubmitAdapter.AssignmentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_assignment_upload, parent, false))

        return viewHolder
    }

    override fun onBindViewHolder(
        holder: AssignmentSubmitAdapter.AssignmentViewHolder,
        position: Int,
        model: AssignmentDetails,
    ) {
        var studentD: StudentDetails = StudentDetails()
        GlobalScope.launch(Dispatchers.IO){
            studentD =
                FireStoreClass().getCurrentStudent().await().toObject(StudentDetails::class.java)!!
            Log.d("student",studentD.student_ID)
            withContext(Dispatchers.Main)
            {
                val id = studentD.student_ID
                Log.d("id is ",studentD.student_ID)
                Log.d("check record",model.record.contains(id).toString())
                if (model.record[id] == null) {
                    holder.status.text = "Add Assignment"
                    holder.item_student_subDate.visibility = GONE
                    holder.ass_btn.setBackgroundResource(R.drawable.ic_add)
                    holder.ass_btn.setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            val intent = Intent(v!!.context, UploadAssignment::class.java)
                            intent.putExtra("id", model.assignment_ID)
                            v.context.startActivity(intent)
                        }

                    })
                }
                else{
                    holder.status.text = "Assignment uploaded successfully"
                    val time= model.record[id]!!["submissionDate"]?.toLong()

                    val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS")

                    val calendar = Calendar.getInstance()
                    if (time != null) {
                        calendar.timeInMillis = time
                    }
                    val dateWithoutTime = sdf.format(calendar.time)
                    holder.item_student_subDate.text = dateWithoutTime
                    holder.ass_btn.setBackgroundResource(R.drawable.ic_download)

                    holder.ass_btn.setOnClickListener { v ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(model.assignment_File))


                        if (v != null) {
                            ContextCompat.startActivity(v.context, intent, bundle)
                        }
                    }

                }
            }
        }

    }


    class AssignmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var status: TextView
        var item_student_subDate: TextView
        var ass_btn: ImageView

        init {
            status = itemView.findViewById(R.id.status)
            item_student_subDate = itemView.findViewById(R.id.item_student_subDate)
            ass_btn = itemView.findViewById(R.id.ass_btn)

        }

    }
}