package com.example.himanshi2002.project2.activities.student.ui.assignment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.model.AssignmentDetails
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AssignmentAdapter(options: FirestoreRecyclerOptions<AssignmentDetails>, private val bundle: Bundle?):
    FirestoreRecyclerAdapter<AssignmentDetails, AssignmentAdapter.AssignmentViewHolder>(options)  {

    private lateinit var option2: FirestoreRecyclerOptions<AssignmentDetails>
    private lateinit var nestedAdapter:AssignmentSubmitAdapter


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AssignmentAdapter.AssignmentViewHolder {

        val viewHolder = AssignmentAdapter.AssignmentViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_for_assignment, parent, false))
        Log.d("assignment","crashes 9")
        return viewHolder
    }


    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int, model: AssignmentDetails) {
        holder.item_assignment_name.text=model.assignment_Name
        holder.item_assignment_desc.text=model.assignment_Desc
        holder.item_assignment_lastDate.text=model.date_of_submission
        holder.item_assignment_createdBy.text=model.createdBy.name

        holder.assDownload_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(model.assignment_File))


                if (v != null) {
                    ContextCompat.startActivity(v.context, intent, bundle)
                }
            }
        })



        var isExpandable:Boolean=model.isExpandable
        holder.expandableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE

        val query1: Query = FirebaseFirestore.getInstance().collection(Constants.ASSIGNMENT).whereEqualTo("assignment_ID",model.assignment_ID)

        option2 = FirestoreRecyclerOptions.Builder<AssignmentDetails>().setQuery(query1,
            AssignmentDetails::class.java).build()

        nestedAdapter= AssignmentSubmitAdapter(option2,bundle)
        holder.nestedRecyclerView.adapter=nestedAdapter
        holder.nestedRecyclerView.layoutManager= LinearLayoutManager(holder.itemView.context)

        nestedAdapter.startListening()
        holder.linearLayout.setOnClickListener{

            model.isExpandable=!model.isExpandable

            notifyItemChanged(holder.adapterPosition)
        }



    }

    class AssignmentViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        var item_assignment_name: TextView
        var item_assignment_desc: TextView
        var item_assignment_lastDate: TextView
        var item_assignment_createdBy: TextView
        var assDownload_btn:ImageView
        var expandableLayout: RelativeLayout
        var linearLayout: LinearLayout
        var nestedRecyclerView: RecyclerView
        init{
            Log.d("assignment","crashes 11")
            item_assignment_name=itemView.findViewById(R.id.item_assignment_name)
            item_assignment_lastDate=itemView.findViewById(R.id.item_assignment_lastDate)
            item_assignment_desc=itemView.findViewById(R.id.item_assignment_desc)
            item_assignment_createdBy=itemView.findViewById(R.id.item_assignment_createdBy)
            assDownload_btn=itemView.findViewById(R.id.assDownload_btn)
            expandableLayout=itemView.findViewById(R.id.expandable_layout)
            nestedRecyclerView=itemView.findViewById(R.id.child_rv)
            linearLayout=itemView.findViewById(R.id.linear_layout)
        }
    }
}