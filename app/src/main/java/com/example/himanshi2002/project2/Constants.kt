package com.example.himanshi2002.project2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap


object Constants {

    //collection names
    const val STUDENT:String="student"
    const val FACULTY:String="Faculty"
    const val SUBJECT:String="Subject"
    const val ATTENDANCE:String="Attendance"
    const val POST:String="Post"
    const val TIMETABLE: String="Time Table"
    const val NOTICES: String="Notices"
    const val ASSIGNMENT:String="Assignment"



    const val PREFERENCES:String="MyPrefs"
    const val USERNAME:String="logged_in_student"
    const val ROLL_NO:String="logged_in_roll_no"
    const val COURSE:String="Course"

    //details of collections
    const val STUDENT_DETAILS:String="student_details"
    const val COURSE_DETAILS:String="course_details"
    const val FACULTY_DETAILS:String="faculty_details"

    //for external storage permission
    const val ADD_PERMISSION_CODE=2

    //for image request
    const val PICK_IMAGE_REQUEST_CODE=1

    //for file request
    const val PICK_FILE_REQUEST_CODE=1

    //for image
    const val IMAGE:String="image"

    //for file
    const val FILE:String="file"



    fun showImageChooser(activity:Activity)
    {
        val galleryIntent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?):String?
    {

        Log.d("inside ","get file extension")
        Log.d("uri is ",uri.toString())
        Log.d("inside ",MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!)).toString())
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

    fun showFileChooser(activity:Activity) {
        Log.d("activity is ","show file chhoser")

        val galleryIntent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT

        // We will be redirected to choose pdf

        // We will be redirected to choose pdf
        galleryIntent.type = "application/pdf"
        activity.startActivityForResult(galleryIntent, PICK_FILE_REQUEST_CODE)


       /* val galleryIntent1= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "application/pdf"
        activity.startActivityForResult(galleryIntent, PICK_FILE_REQUEST_CODE)*/
    }




}