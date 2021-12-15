package com.example.himanshi2002.project2.activities.faculty.assignment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.himanshi2002.project2.R

class Custom(
    private val context: Activity,
    private val nameArray: MutableList<String>,
    private val dateArray: MutableList<String>,
    private val fileArray: MutableList<String>,
    private val bundle: Bundle?
)
    : ArrayAdapter<String>(context, R.layout.cardview_assignment_uploaded_list, nameArray){


    override fun getView(position: Int, rowView: View?, parent: ViewGroup): View {
        val inflater=context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom,null,true)

        val nameText=rowView.findViewById<TextView>(R.id.status)
        val dateText=rowView.findViewById<TextView>(R.id.item_student_subDate)
        val ass_btn : ImageView = rowView.findViewById(R.id.ass_btn)

        Log.d("date aary has",dateArray.toString())

        nameText.text=nameArray[position]
        dateText.text=dateArray[position]

        ass_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fileArray[position]))

                if (v != null) {
                    ContextCompat.startActivity(v.context, intent, bundle)
                }
            }
        })



        return rowView
    }
}