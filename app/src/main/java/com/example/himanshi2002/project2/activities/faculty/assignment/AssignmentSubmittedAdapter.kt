package com.example.himanshi2002.project2.activities.faculty.assignment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.R
import kotlin.collections.ArrayList
import java.text.SimpleDateFormat
import java.util.*


class AssignmentSubmittedAdapter(
    private val recordList: ArrayList<Map<*, Map<*, *>>>,
    private val activity: FragmentActivity?,
    private val bundle: Bundle?,

):
    RecyclerView.Adapter<AssignmentSubmittedAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AssignmentSubmittedAdapter.MyViewHolder {
        val viewHolder = AssignmentSubmittedAdapter.MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_assignment_uploaded_list, parent, false))
       /* Log.d("record list",recordList.toString())*/
        return viewHolder
    }

    override fun onBindViewHolder(holder: AssignmentSubmittedAdapter.MyViewHolder, position: Int) {

        var nameArray: MutableList<String> = mutableListOf()
        var dateArray:MutableList<String> = mutableListOf()
        var fileArray:MutableList<String> = mutableListOf()
        for ((k, v) in recordList[position]) {
            Log.d("key",k.toString())
            nameArray.add(k.toString())
            /*holder.item_student_name.text=k.toString()*/
           /* Log.d("value",v.toString())
            Log.d("file name",v["file"].toString())*/
            for((s,l) in v)
            {

                   if(s.toString()=="file")
                   {
                     /*  Log.d("add to","file array")*/
                       fileArray.add(l.toString())
                   }
                    if(s.toString()=="submissionDate")
                    {
                       /* Log.d("add to","date array")*/
                        val date: Long =l.toString().toLong()

                       val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS")
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = date
                        val dateWithoutTime = sdf.format(calendar.time)


                        dateArray.add(dateWithoutTime)
                    }

            }
        }

        val cs= activity?.let { Custom(it,nameArray,dateArray,fileArray,bundle) }
        holder.listview.adapter=cs

    }

    override fun getItemCount(): Int {
        Log.d("size is",recordList.size.toString())
       return recordList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var listview:ListView


        init {
            listview=itemView.findViewById(R.id.list1)


        }

    }
}


/*
class AssignmentSubmittedAdapter(options: FirestoreRecyclerOptions<AssignmentDetails>)
    :
    FirestoreRecyclerAdapter<AssignmentDetails, AssignmentSubmittedAdapter.AssignmentViewHolder>(
        options as FirestoreRecyclerOptions<AssignmentDetails>) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AssignmentSubmittedAdapter.AssignmentViewHolder {
        val viewHolder = AssignmentSubmittedAdapter.AssignmentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_assignment_uploaded_list, parent, false))

        return viewHolder
    }

    override fun onBindViewHolder(
        holder: AssignmentViewHolder,
        position: Int,
        model: AssignmentDetails,
    ) {
        for ((key, value) in model.record.entries) {
            for ((key1, value1) in value.entries)
            {

                Log.d("outerkey",key)
                holder.item_student_name.text = key
                Log.d("innerkey",key1)
                Log.d("value",value1)
                holder.item_student_subDate.text = value1
                println("OuterKey: $key InnerKey: $key1 VALUE:$value1")
            }
        }

        */
/*holder.item_student_name.text = model.record[""]!!.toString()
        val time= model.record[""]!!["submissionDate"]?.toLong()

        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS")

        val calendar = Calendar.getInstance()
        if (time != null) {
            calendar.timeInMillis = time
        }
        val dateWithoutTime = sdf.format(calendar.time)
        holder.item_student_subDate.text = dateWithoutTime*//*

    }

    class AssignmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item_student_name: TextView
        var item_student_subDate: TextView
        var assDownload_btn: ImageView

        init {
            item_student_name = itemView.findViewById(R.id.item_student_name)
            item_student_subDate = itemView.findViewById(R.id.item_student_subDate)
            assDownload_btn = itemView.findViewById(R.id.assDownload_btn)

        }

    }
}*/
