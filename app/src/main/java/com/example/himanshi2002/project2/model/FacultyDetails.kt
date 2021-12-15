package com.example.himanshi2002.project2.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class FacultyDetails(
    val faculty_ID:String="",
    val name:String="",
    val email:String="",
    val gender:String="",
    val designation:String="",
    val qualification:String="",
    val Date_of_Birth:String="",
    val Phone_No:String="",
    val faculty_image:String=""
):Parcelable