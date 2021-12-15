package com.example.himanshi2002.project2.Utils

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.model.NoticesDetails
import com.example.himanshi2002.project2.model.TimeTableDetails
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.text.SimpleDateFormat
import java.util.*

class NoticesAdapter(options: FirestoreRecyclerOptions<NoticesDetails>,private val bundle: Bundle?) :FirestoreRecyclerAdapter<NoticesDetails,NoticesAdapter.NoticesViewHolder>(options)
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoticesAdapter.NoticesViewHolder {
        return NoticesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cardview_for_notice,parent,false))

    }

    override fun onBindViewHolder(holder: NoticesAdapter.NoticesViewHolder, position: Int,model:NoticesDetails)
    {
       /* val notice:NoticesDetails=noticesList[position]*/
        holder.item_notice_title.text=model.description
        val time= model.date_of_Issue
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS")

        val calendar = Calendar.getInstance()
        if (time != null) {
            calendar.timeInMillis = time
        }
        val dateWithoutTime = sdf.format(calendar.time)

        holder.item_notice_date.text= dateWithoutTime
        holder.noticeDownload_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(model.notice_File))


                if (v != null) {
                    ContextCompat.startActivity(v.context, intent, bundle)
                }
            }
        })


    }



    class NoticesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item_notice_title:TextView
        var item_notice_date:TextView
        var noticeDownload_btn:Button
        init {
            item_notice_title=itemView.findViewById(R.id.item_notice_title)
            item_notice_date=itemView.findViewById(R.id.item_notice_date)
            noticeDownload_btn=itemView.findViewById(R.id.noticeDownload_btn)
        }

    }
}