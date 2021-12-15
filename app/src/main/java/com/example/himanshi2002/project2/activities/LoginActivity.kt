package com.example.himanshi2002.project2.activities

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.Dashboard
import com.example.himanshi2002.project2.Utils.FireStoreClass
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.activities.faculty.DashboardFaculty
import com.example.himanshi2002.project2.model.FacultyDetails
import com.example.himanshi2002.project2.model.StudentDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

import android.util.Log

import android.view.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.himanshi2002.project2.Utils.ConnectionReceiver


class LoginActivity : BasicActivity(),View.OnClickListener {
    private lateinit var auth: FirebaseAuth
    lateinit var et_email: EditText
    lateinit var et_password: EditText
    lateinit var bt_login: Button
    lateinit var forgot_password:TextView
    lateinit var dialog: Dialog

    lateinit var broadcastReceiver:BroadcastReceiver


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)


        setContentView(R.layout.activity_login)


        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        broadcastReceiver= ConnectionReceiver()
        registerNetworkBroadcast()



            auth = Firebase.auth
            et_email = findViewById(R.id.rollno)
            et_password = findViewById(R.id.password)
            bt_login = findViewById(R.id.login)
            forgot_password=findViewById(R.id.forgot_password)
            bt_login.setOnClickListener(this)
            forgot_password.setOnClickListener(this)





    }


    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                /*showErrorSnackBar("Your details are valid",false)*/
                true
            }

        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.login -> {

                        logIn()

                        Log.d("not internet","connection")


                }
                R.id.forgot_password->{
                    val intent=Intent(applicationContext, ForgotPassword::class.java)
                    startActivity(intent)
                }

            }
        }
    }


    private fun logIn()
    {
        if (validateLoginDetails())
        {
            dialog = ProgressDialog.show(this, "Loading...", "Please wait...", true)
          /*  showProgressDialog(this)*/
            val email = et_email.text.toString().trim { it <= ' ' }
            val password = et_password.text.toString().trim { it <= ' ' }

            GlobalScope.launch(Dispatchers.IO)
            {
                try{

                    val authResult = auth.signInWithEmailAndPassword(email, password).await()

                    val firebaseUser = authResult.user

                    withContext(Dispatchers.Main)
                    {

                        updateUI(firebaseUser)
                    }
                }
                catch (e: Exception)
                {
                   withContext(Dispatchers.Main)
                    {
                        updateUI(null)

                    }
                }
            }

        }
    }

    private fun updateUI(firebaseUser: FirebaseUser?)
    {
        if(firebaseUser!=null)
        {
            if(intent.hasExtra("student"))
            {
                GlobalScope.launch {
                    val s=FireStoreClass().getCurrentStudent().await().toObject(StudentDetails::class.java)
                    withContext(Dispatchers.Main)
                    {
                       loggedInSuccessFully(s)

                    }

                }
            }
            else if(intent.hasExtra("faculty"))
            {

                GlobalScope.launch {
                    val f=FireStoreClass().getCurrentFaculty().await().toObject(FacultyDetails::class.java)
                    withContext(Dispatchers.Main)
                    {
                        loggedInSuccessFullyAsFaculty(f)
                    }

                }
            }
        }
        else
        {
            dialog.dismiss()
            /*hideProgressDialog()*/
            showErrorSnackBar("Your details are invalid",true)
        }

    }

    fun loggedInSuccessFully(student: StudentDetails?)
    {
        /*hideProgressDialog()*/
        dialog.dismiss()
        Toast.makeText(applicationContext,"Logged in successfully",Toast.LENGTH_LONG).show()
        val i = Intent(applicationContext, Dashboard::class.java)

        i.putExtra(Constants.STUDENT_DETAILS,student)

        startActivity(i)
        finish()

    }

    fun loggedInSuccessFullyAsFaculty(faculty: FacultyDetails?)
    {
       /* hideProgressDialog()*/
        dialog.dismiss()
        Toast.makeText(applicationContext,"Logged in successfully",Toast.LENGTH_LONG).show()

        val intent = Intent(this@LoginActivity, DashboardFaculty::class.java)

        intent.putExtra(Constants.FACULTY_DETAILS,faculty)


        startActivity(intent)
        finish()

    }



    private fun registerNetworkBroadcast()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            Log.d("inside ","register network")
            registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }

    private fun unregisterNetwork()
    {
        try{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        }
        catch (e: IllegalArgumentException)
        {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

       unregisterNetwork()
    }

    override fun onStop() {
        super.onStop()
        unregisterNetwork()
    }




     override fun onStart() {
        super.onStart()
        broadcastReceiver= ConnectionReceiver()
        registerNetworkBroadcast()
    }




}

