package com.example.himanshi2002.project2.model
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
class StudentDetails(
    val student_ID: String = "",
    val first_Name: String = "",
    val last_Name: String = "",
    val gender: String = "",
    val Course_ID:String="",
    /*val Course_Name:String="",*/
    val batch:String="",
    val email:String="",
    val profile_image:String="",
    val date_of_birth:String="",
    val address:String="",
    val shift:String="",
    /*val phone_no:Long = 0L*/

):Parcelable