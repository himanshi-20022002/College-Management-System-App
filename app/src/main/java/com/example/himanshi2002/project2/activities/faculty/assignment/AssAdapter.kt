package com.example.himanshi2002.project2.activities.faculty.assignment

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
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.model.AssignmentDetails
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.ArrayList

class AssAdapter(
    options: FirestoreRecyclerOptions<AssignmentDetails>,
    private val bundle: Bundle?,
    private val activity: FragmentActivity?
):
    FirestoreRecyclerAdapter<AssignmentDetails, AssAdapter.AssignmentViewHolder>(options) {
    private lateinit var option2: FirestoreRecyclerOptions<AssignmentDetails>


    private lateinit var recordArrayList: ArrayList<Map<*,Map<*,*>>>
    private lateinit var nestedAdapter: AssignmentSubmittedAdapter
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AssignmentViewHolder {
        val viewHolder = AssignmentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_for_assignment, parent, false))
        Log.d("assignment","crashes 9")
        return viewHolder
    }


    override fun onBindViewHolder(
        holder: AssignmentViewHolder,
        position: Int,
        model: AssignmentDetails,
    ) {
        Log.d("assignment","crashes 10")
        holder.item_assignment_name.text=model.assignment_Name
        holder.item_assignment_desc.text=model.assignment_Desc
        holder.item_assignment_lastDate.text=model.date_of_submission
        holder.item_assignment_createdBy.text=model.createdBy.name
        var isExpandable:Boolean=model.isExpandable
        holder.expandableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE





        holder.nestedRecyclerView.layoutManager= LinearLayoutManager(holder.itemView.context)
        holder.nestedRecyclerView.setHasFixedSize(true)

        recordArrayList= arrayListOf()

        Log.d("record here",recordArrayList.toString())
        nestedAdapter= AssignmentSubmittedAdapter(recordArrayList,activity,bundle)

        holder.nestedRecyclerView.adapter=nestedAdapter




        eventChangedListener(model.assignment_ID as String)

        holder.linearLayout.setOnClickListener{

            Log.d("cluick here","click")
            model.isExpandable=!model.isExpandable

            notifyItemChanged(holder.adapterPosition)
        }
        holder.assDownload_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(model.assignment_File))

                if (v != null) {
                    ContextCompat.startActivity(v.context, intent, bundle)
                }
            }
        })


    }

    private fun eventChangedListener(text: String) {

        GlobalScope.launch(Dispatchers.IO)
        {
            try{
                val item=FirebaseFirestore.getInstance()
                    .collection(Constants.ASSIGNMENT)
                    .whereEqualTo("assignment_ID",text)
                    .get().await()
                for(document in item)
                {
                    Log.d("document is ", "DocumentSnapshot data: ${document.data}")
                    Log.d("record is ",document.get("record").toString())
                    if(document.get("record")!=null)
                    {
                        val record=document.get("record") as Map<*, Map<*,*>>
                        recordArrayList.add(record)
                        Log.d("record array list",recordArrayList.toString())
                    }
                }
            }
            catch(e:FirebaseFirestoreException)
            {
                Log.d("documents is ", "Error getting documents: ")
            }
            withContext(Dispatchers.Main)
            {
                nestedAdapter.notifyDataSetChanged()
            }

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
        var nestedRecyclerView:RecyclerView
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
