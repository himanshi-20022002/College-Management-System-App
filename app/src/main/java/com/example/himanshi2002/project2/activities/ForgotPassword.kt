package com.example.himanshi2002.project2.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.himanshi2002.project2.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : BasicActivity()
{
    lateinit var btn_submit:Button
    lateinit var et_email:EditText

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null)
        {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "Edu4u"
            actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.action_bar_bg))
        }
        et_email=findViewById(R.id.et_email)
        btn_submit=findViewById(R.id.btn_submit)
        btn_submit.setOnClickListener{
            val email:String=et_email.text.toString().trim{ it<= ' '}
            if(email.isEmpty())
            {
                showErrorSnackBar("Enter email",true)
            }
            else
            {
                showProgressDialog(this)
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{task->
                        hideProgressDialog()
                        if(task.isSuccessful){
                            Toast.makeText(applicationContext,"Email send success",Toast.LENGTH_LONG).show()
                            finish()
                        }
                        else{
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }

                    }
            }
        }


    }

}