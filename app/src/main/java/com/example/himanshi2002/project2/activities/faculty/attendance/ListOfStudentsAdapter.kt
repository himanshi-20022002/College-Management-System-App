package com.example.himanshi2002.project2.activities.faculty.attendance

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.model.StudentDetails
import com.example.himanshi2002.project2.R

import android.widget.RadioButton
import android.widget.CompoundButton








class ListOfStudentsAdapter(private val studentList:ArrayList<StudentDetails>) : RecyclerView.Adapter<ListOfStudentsAdapter.MyViewHolder>(){
    private var selectedItemPosition: Int = 0
    private var status = ""

    var  attendance_map = mutableMapOf<String,String>()


    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var imageView:ImageView
        var itemStudentName:TextView
        var itemStudentID:TextView

        val radioGroup:RadioGroup
        var radioButton:RadioButton
        var radioButton1:RadioButton


        init {
            imageView=itemView.findViewById(R.id.item_attendanceImage)
            itemStudentName=itemView.findViewById(R.id.item_studentName)

            itemStudentID=itemView.findViewById(R.id.item_studentRollNo)

            radioGroup=itemView.findViewById(R.id.radioGroup)
            radioButton=itemView.findViewById(R.id.radioPresent)
            radioButton1=itemView.findViewById(R.id.radioAbsent)

        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.cardview_students_list,parent,false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged", "RecyclerView")
    override fun onBindViewHolder(holder: MyViewHolder, position : Int) {
        val student: StudentDetails =studentList[position]
        var row_index=-1
        holder.itemStudentName.text=student.first_Name+" "+student.last_Name
        holder.itemStudentID.text=student.student_ID


        holder.radioButton.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if (p1)
                    attendance_map[student.student_ID] = "P"

            }
        })
        holder.radioButton1.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if (p1)
                    attendance_map[student.student_ID] = "A"

            }
        })


    }


    override fun getItemCount(): Int {
        return studentList.size
    }
}