package com.example.himanshi2002.project2.Utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.himanshi2002.project2.R
import java.lang.Exception
import android.app.Activity
import android.content.ComponentName

import android.app.ActivityManager








class ConnectionReceiver : BroadcastReceiver()
{




    lateinit var mContext : Context

     private var mAlertDialog:Dialog? =null


    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("inside","connection receiver 1")
        if (context != null) {
            Log.d("inside","connection receiver 2")
            mContext=context
        }
if(context!=null)
{

    if(isConnected(context))
    {
        Log.d("inside","connection receiver 4")
       /* Toast.makeText(context,"Internet Connected", Toast.LENGTH_LONG).show()*/
        hideDialog()
    }

    else
    {
        Log.d("is not", "connected")
        showDialog(context)
    }
}


    }

    private fun isConnected(c: Context):Boolean
    {
        return try {
            val cm: ConnectivityManager = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo : NetworkInfo? =cm.activeNetworkInfo
            networkInfo!=null && networkInfo.isConnectedOrConnecting
        } catch (e: NullPointerException) {
            e.printStackTrace()
            false
        }

    }

    private fun showDialog(context: Context)
    {
        Log.d("context is ",context.toString())
        val activity = context as Activity
        Log.d("activity is",activity.toString())
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val cn = am.getRunningTasks(1)[0].topActivity
        Log.d("cn is ",cn.toString())
        mAlertDialog=Dialog(context)
        mAlertDialog!!.setContentView(R.layout.alert_dialog_layout)
        mAlertDialog!!.setCancelable(false)
        mAlertDialog!!.setCanceledOnTouchOutside(false)

        if(!activity.isFinishing) {
            mAlertDialog!!.show()
        }

        else{
            Log.d("exception","not show dialog")

        }
    }
    fun hideDialog()
    {
        mAlertDialog?.dismiss()
        Log.d("progress dialog","hide")
    }

}
