package com.example.himanshi2002.project2.Utils

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.activities.faculty.notices.AddNotices
import com.example.himanshi2002.project2.activities.faculty.assignment.CreateNewAssignment
import com.example.himanshi2002.project2.activities.student.ui.assignment.UploadAssignment
import com.example.himanshi2002.project2.activities.student.ui.home.CreateNewPost
import com.example.himanshi2002.project2.model.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class FireStoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()
    lateinit var fileUriString: String
    lateinit var a: String
    fun getCurrentStudent(): Task<DocumentSnapshot> {
        Log.d("getting ", "student details")
        return mFireStore.collection(Constants.STUDENT)
            .document(getCurrentUserID())
            .get()

    }

    private fun getCurrentUserID(): String {
        val currentStudent = FirebaseAuth.getInstance().currentUser

        var currentStudentID: String = " "
        if (currentStudent != null) {
            currentStudentID = currentStudent.uid
        }
        return currentStudentID
    }

    fun getCurrentFaculty(): Task<DocumentSnapshot> {

        return mFireStore.collection(Constants.FACULTY)
            .document(getCurrentUserID())
            .get()

    }

    fun addPost(text: String, title: String) {
        GlobalScope.launch {

            val student =
                FireStoreClass().getCurrentStudent().await().toObject(StudentDetails::class.java)!!
            val currentTime = System.currentTimeMillis()

            val post = PostDetails(text, title, student, currentTime, "Post$currentTime")
            mFireStore.collection(Constants.POST).document()
                .set(post)
        }
    }



    fun addFile(uri: String, desc: String){
        GlobalScope.launch {
            val faculty =
                FireStoreClass().getCurrentFaculty().await().toObject(FacultyDetails::class.java)!!
            val currentTime = System.currentTimeMillis()
            val facultyID = faculty.faculty_ID

            val file = NoticesDetails("Notice$currentTime", uri, desc, currentTime, facultyID, )
            mFireStore.collection(Constants.NOTICES).document()
                .set(file)
        }
    }

    fun uploadFileToCloudStorage(
        activity: Activity,
        imageFileUri: Uri?,
        title: String
    ) {
        val dialog = ProgressDialog.show(activity, "Loading...", "Please wait...", true)
        GlobalScope.launch(Dispatchers.IO) {


            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                Constants.FILE + System.currentTimeMillis() + "." + Constants.getFileExtension(
                    activity,
                    imageFileUri
                ))
            Log.d("crashes","here1")
            a = sRef
                .putFile(imageFileUri!!)
                .await() // await() instead of snapshot
                .metadata!!.reference!!
                .downloadUrl
                .await() // await the url
                .toString()
            Log.d("crashes","here2")
            withContext(Dispatchers.Main)
            {
                Log.d("activity is ",activity.toString())
                Log.d("crashes ","here23")

                when (activity) {
                    is CreateNewPost -> {

                        activity.imageUploadSuccess(a)

                        FireStoreClass().addPost(a, title)
                      dialog.dismiss()
                        Toast.makeText(activity, "Post added successfully", Toast.LENGTH_LONG)
                            .show()
                        activity.finish()
                    }
                    is AddNotices -> {
                        AddNotices().imageUploadSuccess(a)
                        FireStoreClass().addFile(a, title)
                        dialog.dismiss()
                        Toast.makeText(activity, "Notice added successfully", Toast.LENGTH_LONG)
                            .show()
                        activity.finish()
                    }

                }
            }
        }
    }

    fun uploadAssignmentToCloudStorage(
        activity: Activity,
        assFileUri: Uri?,
        assDes: String,
        assName: String,
        subName: String,
        lastDate: String
    ) {
        GlobalScope.launch(Dispatchers.IO)
        {

            Log.d("inside ", "upload assignment to cloud3")
            Log.d("uri is ", assFileUri.toString())
            Log.d("file extension is", Constants.getFileExtension(activity, assFileUri).toString())
            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                Constants.FILE + System.currentTimeMillis() + "." + Constants.getFileExtension(
                    activity,
                    assFileUri
                ))
            Log.d("sRef is ", sRef.toString())


            Log.d("task is ", "going")
            fileUriString = sRef
                .putFile(assFileUri!!)
                .await() // await() instead of snapshot
                .metadata!!.reference!!
                .downloadUrl
                .await() // await the url
                .toString()
            Log.d("file uri is ", fileUriString)
            withContext(Dispatchers.Main)
            {
                when (activity) {
                    is CreateNewAssignment -> {
                        activity.imageUploadSuccess(fileUriString)

                        FireStoreClass().addAssignment(fileUriString,
                            assDes,
                            assName,
                            subName,
                            lastDate)
                        Toast.makeText(activity, "file uploaded successfully", Toast.LENGTH_LONG)
                            .show()
                        activity.finish()
                    }
                }
            }

        }

    }


    fun addAssignment(
        assignmentUri: String,
        assDes: String,
        assName: String,
        subName: String,
        lastDate: String
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            val faculty =
                FireStoreClass().getCurrentFaculty().await().toObject(FacultyDetails::class.java)!!
            val currentTime = System.currentTimeMillis()
            /*    val facultyID = faculty.Faculty_ID*/

            val file = AssignmentDetails(
                faculty.faculty_ID+"-"+currentTime,
                assignmentUri,
                assDes,
                assName,
                faculty,
                subName,
                currentTime,
                lastDate
            )
            mFireStore.collection(Constants.ASSIGNMENT).document(faculty.faculty_ID+"-"+currentTime)
                .set(file)


        }
    }


    fun uploadAssignmentSolutionToCloudStorage(
        activity: Activity,
        assFileUri: Uri?,
        assID: String,

    ) {
        val dialog = ProgressDialog.show(activity, "Loading...", "Please wait...", true)
        GlobalScope.launch(Dispatchers.IO)
        {

            Log.d("inside ", "upload assignment to cloud3")
            Log.d("uri is ", assFileUri.toString())
            Log.d("file extension is", Constants.getFileExtension(activity, assFileUri).toString())


            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                Constants.FILE + System.currentTimeMillis() + "." + Constants.getFileExtension(
                    activity,
                    assFileUri
                ))
            Log.d("sRef is ", sRef.toString())


            Log.d("task is ", "going")
            fileUriString = sRef
                .putFile(assFileUri!!)
                .await() // await() instead of snapshot
                .metadata!!.reference!!
                .downloadUrl
                .await() // await the url
                .toString()
            Log.d("file uri is ", fileUriString)
            withContext(Dispatchers.Main)
            {
                when (activity) {
                    is UploadAssignment -> {
                        activity.imageUploadSuccess(fileUriString)

                        FireStoreClass().addAssignmentSolution(fileUriString,
                            assID,
                            )
                        dialog.dismiss()
                        Toast.makeText(activity, "File uploaded successfully", Toast.LENGTH_LONG)
                            .show()
                        activity.finish()
                    }
                }
            }

        }

    }

    private fun addAssignmentSolution(fileUriString: String, assID: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val student =
                FireStoreClass().getCurrentStudent().await().toObject(StudentDetails::class.java)!!
            val currentTime = System.currentTimeMillis()

            val s=student.student_ID

            mFireStore.collection(Constants.ASSIGNMENT).document(assID)
                .update("record.$s.file",fileUriString)

            mFireStore.collection(Constants.ASSIGNMENT).document(assID)
                .update("record.$s.submissionDate",currentTime.toString())

        }
    }
}


