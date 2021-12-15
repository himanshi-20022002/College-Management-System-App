package com.example.himanshi2002.project2.activities.faculty

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.net.toUri
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.Utils.FireStoreClass
import com.example.himanshi2002.project2.Utils.GlideLoader
import com.example.himanshi2002.project2.activities.LoginActivity
import com.example.himanshi2002.project2.model.CourseDetails
import com.example.himanshi2002.project2.model.FacultyDetails
import com.example.himanshi2002.project2.model.StudentDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext




class FacultyProfile : Fragment()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        (activity as DashboardFaculty?)
            ?.setActionBarTitle("Profile")


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_faculty_profile, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialog = ProgressDialog.show(activity, "Loading...", "Please wait...", true)

        var facultyD: FacultyDetails = FacultyDetails()
        val profileImage: ImageView = view.findViewById(R.id.profile_image)
        val profileName: TextView = view.findViewById(R.id.profile_name)
        val profileID: TextView = view.findViewById(R.id.profile_ID)
        val facultyID: TextView = view.findViewById(R.id.facultyID)
        val name: TextView = view.findViewById(R.id.facultyName)
        val email: TextView = view.findViewById(R.id.facultyEmail)
        val gender: TextView = view.findViewById(R.id.facultyGender)
        val designation: TextView = view.findViewById(R.id.facultyDesignation)
        val qualification: TextView = view.findViewById(R.id.facultyQualification)
        val dob: TextView = view.findViewById(R.id.facultyDOB)
        val phoneNo: TextView =view.findViewById(R.id.facultyPhone)

        GlobalScope.launch(Dispatchers.IO)
        {

            Log.d("inside", "launch")
            facultyD =
                FireStoreClass().getCurrentFaculty().await().toObject(FacultyDetails::class.java)!!
            Log.d("inside", facultyD.toString())

            withContext(Dispatchers.Main)
            {

                dialog.dismiss()
                val imageUri: Uri =facultyD.faculty_image.toUri()
                GlideLoader(requireContext()).loadUserPicture(imageUri,profileImage)


                Log.d("inside","dispatcher main")
                /*Picasso.get().load(facultyD.faculty_image).into(profileImage)*/
                profileName.text = facultyD.name
                Log.d("profile name",profileName.text.toString())
                profileID.text = facultyD.faculty_ID
                facultyID.text = facultyD.faculty_ID
                name.text = facultyD.name
                email.text=facultyD.email
                gender.text = facultyD.gender
                designation.text = facultyD.designation
                qualification.text = facultyD.qualification

                Log.d("inside","dispatcher main")
            }
        }

    }

}