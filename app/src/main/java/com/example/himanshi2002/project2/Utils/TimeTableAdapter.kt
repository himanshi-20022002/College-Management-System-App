package com.example.himanshi2002.project2.Utils

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.model.TimeTableDetails
import androidx.core.content.ContextCompat.startActivity
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.example.himanshi2002.project2.model.NoticesDetails
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.sql.Time


class TimeTableAdapter(
    options: FirestoreRecyclerOptions<TimeTableDetails>,
    private val bundle: Bundle?
): FirestoreRecyclerAdapter<TimeTableDetails, TimeTableAdapter.TimeTableViewHolder>(options) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TimeTableViewHolder {

        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_for_timetable, parent, false)

        return TimeTableViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TimeTableViewHolder, position: Int,model:TimeTableDetails) {

        holder.item_timeTable_title.text = model.timeTableClass + "-"+ model.Semester + " Semester"
        holder.download_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(model.timeTableFile))


                if (v != null) {
                    startActivity(v.context,intent,bundle)
                }


            }

        })

    }


    class TimeTableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var item_timeTable_title: TextView
        var download_btn: Button

        init {

            item_timeTable_title = itemView.findViewById(R.id.item_timetable_title)
            download_btn = itemView.findViewById(R.id.download_btn)

        }


    }
}