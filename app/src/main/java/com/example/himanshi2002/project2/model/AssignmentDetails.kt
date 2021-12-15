package com.example.himanshi2002.project2.model

data class AssignmentDetails (
    val assignment_ID:String="",
    val assignment_File:String="",
    val assignment_Desc:String="",
    val assignment_Name:String="",
    val createdBy:FacultyDetails=FacultyDetails(),
    val Subject_Name:String="",
    val date_of_assigned:Long=0L,
    val date_of_submission:String="",
    var record:Map<String,Map<String,String>> = mutableMapOf(),
    var isExpandable:Boolean=false
)