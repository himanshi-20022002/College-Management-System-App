package com.example.himanshi2002.project2.model

data class PostDetails (

    val post_image:String="",
    val post_description:String="",
   val createdBy:StudentDetails=StudentDetails(),
    val createdAt:Long=0L,
    val postID:String=""

    )