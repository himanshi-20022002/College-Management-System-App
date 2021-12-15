package com.example.himanshi2002.project2.activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.Dashboard
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.Utils.FireStoreClass
import com.example.himanshi2002.project2.model.FacultyDetails
import com.example.himanshi2002.project2.model.StudentDetails
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import androidx.annotation.NonNull
import com.example.himanshi2002.project2.activities.faculty.DashboardFaculty

import com.google.android.gms.tasks.OnCompleteListener

import com.google.firebase.firestore.DocumentReference




class FacultyOrStudent : AppCompatActivity()
{
    lateinit var student_btn:Button
    lateinit var faculty_btn:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_faculty_or_student)




            student_btn = findViewById(R.id.student)
            faculty_btn = findViewById(R.id.faculty)

            student_btn.setOnClickListener {
                val i = Intent(applicationContext, LoginActivity::class.java)
                i.putExtra("student", "student")
                startActivity(i)
                finish()
            }

            faculty_btn.setOnClickListener {
                val i = Intent(applicationContext, LoginActivity::class.java)
                i.putExtra("faculty", "faculty")
                startActivity(i)
                finish()
            }

    }
}