package com.example.himanshi2002.project2.activities

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.himanshi2002.project2.R
import com.google.android.material.snackbar.Snackbar


open class BasicActivity : AppCompatActivity()
{
    lateinit var mProgressDialog:Dialog

    fun showErrorSnackBar(message: String,errorMessage: Boolean)
    {
        val snackBar= Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
        val snackBarView=snackBar.view

        if(errorMessage)
        {
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BasicActivity,
                R.color.colorSnackBarError
            ))
        }
        else
        {
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BasicActivity,
                R.color.colorSnackBarSuccess
            ))
        }
        snackBar.show()
    }
    fun showProgressDialog(context : Context)
    {

        mProgressDialog= Dialog(context)

        Log.d("inflate","here")
        mProgressDialog.setContentView(R.layout.progress_bar)

        Log.d("inflate","here after set content")

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        Log.d("progress dialog",mProgressDialog.show().toString())

        mProgressDialog.show()
    }

    fun hideProgressDialog()
    {
        Log.d("progress dialog","hide")
        mProgressDialog.dismiss()
    }
}