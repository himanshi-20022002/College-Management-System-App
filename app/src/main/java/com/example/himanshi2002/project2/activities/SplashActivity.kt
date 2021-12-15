package com.example.himanshi2002.project2.activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.Dashboard
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.activities.faculty.DashboardFaculty
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash)
        Handler().postDelayed(Runnable {
            val user = Firebase.auth.currentUser
            if (user != null) {
                val rootRef = FirebaseFirestore.getInstance()
                val docIdRef = rootRef.collection(Constants.STUDENT).document(user.uid)
                docIdRef.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document!!.exists()) {
                            Log.d("RTag", "Document exists!")
                            val i=Intent(this,Dashboard::class.java)
                            startActivity(i)
                            finish()
                        } else {
                            Log.d(ContentValues.TAG, "Document does not exist!")
                        }
                    } else {
                        Log.d(ContentValues.TAG, "Failed with: ", task.exception)
                    }
                }

                val rootRef1 = FirebaseFirestore.getInstance()
                val docIdRef1 = rootRef1.collection(Constants.FACULTY).document(user.uid)
                docIdRef1.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document!!.exists()) {
                            Log.d("RTag", "Document exists!")
                            val i=Intent(this,DashboardFaculty::class.java)
                            startActivity(i)
                            finish()
                        } else {
                            Log.d(ContentValues.TAG, "Document does not exist!")
                        }
                    } else {
                        Log.d(ContentValues.TAG, "Failed with: ", task.exception)
                    }
                }


            }
            else
            {
                val i= Intent(this@SplashActivity,FacultyOrStudent::class.java)
                startActivity(i)
                finish()
            }

        },3000)
    }
}