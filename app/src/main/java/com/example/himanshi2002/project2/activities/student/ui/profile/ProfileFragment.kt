package com.example.himanshi2002.project2.activities.student.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.Utils.FireStoreClass
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.activities.BasicActivity
import com.example.himanshi2002.project2.model.CourseDetails
import com.example.himanshi2002.project2.model.StudentDetails
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import androidx.core.net.toUri
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.himanshi2002.project2.Utils.ConnectionReceiver
import com.example.himanshi2002.project2.Utils.GlideLoader
import java.lang.IllegalArgumentException


class ProfileFragment : Fragment()
{
    lateinit var broadcastReceiver: BroadcastReceiver
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View?
    {
        val view:View=inflater.inflate(R.layout.fragment_profile, container, false)
        broadcastReceiver= ConnectionReceiver()
        registerNetworkBroadcast()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val dialog = ProgressDialog.show(activity, "Loading...", "Please wait...", true)
        var studentD: StudentDetails = StudentDetails()
        val profileImage: ImageView = view.findViewById(R.id.profile_image)
        val profileName: TextView = view.findViewById(R.id.profile_name)
        val profileID: TextView = view.findViewById(R.id.profile_ID)
        val studentID: TextView = view.findViewById(R.id.ID)
        val fname: TextView = view.findViewById(R.id.f_name)
        val lname: TextView = view.findViewById(R.id.l_name)
        val gender: TextView = view.findViewById(R.id.Gender)
        val courseID: TextView = view.findViewById(R.id.course_id)
        val courseName: TextView = view.findViewById(R.id.course_name)
        val batch: TextView = view.findViewById(R.id.batch)
        val shift:TextView=view.findViewById(R.id.shift)
        val phone:TextView=view.findViewById(R.id.phone)
        val email:TextView=view.findViewById(R.id.email)
        val address:TextView=view.findViewById(R.id.address)
        val dob:TextView=view.findViewById(R.id.dob)


        GlobalScope.launch(Dispatchers.IO)
        {


            studentD =
                FireStoreClass().getCurrentStudent().await().toObject(StudentDetails::class.java)!!
            var course: CourseDetails = CourseDetails()
            val db: FirebaseFirestore = FirebaseFirestore.getInstance()

            try {
                val item = db.collection(Constants.COURSE)
                    .whereEqualTo("Course_ID", studentD.Course_ID)
                    .get().await()
                for (document in item) {
                    Log.d("inside", "for loop")
                    Log.d("document is ", "${document.id} => ${document.data}")
                    course = document.toObject(CourseDetails::class.java)
                    Log.d("course is ", course.Course_ID)

                }
            } catch (e: FirebaseFirestoreException) {
                Log.d("document5 is ", "Error getting documents: ")
            }
            withContext(Dispatchers.Main)
            {
                Log.d("inside","dispatcher main")
                dialog.dismiss()
                val imageUri: Uri =studentD.profile_image.toUri()
                GlideLoader(requireContext()).loadUserPicture(imageUri,profileImage)

                profileName.text = studentD.first_Name + " " + studentD.last_Name
                Log.d("profile name",profileName.text.toString())
                profileID.text = studentD.student_ID
                studentID.text = studentD.student_ID
                fname.text = studentD.first_Name
                lname.text = studentD.last_Name
                gender.text = studentD.gender
                courseID.text = studentD.Course_ID
                courseName.text = course.Course_Name
                batch.text = studentD.batch
              dob.text=studentD.date_of_birth
                address.text=studentD.address
                shift.text=studentD.shift
                /*phone.text=studentD.phone_no.toString()*/


                email.text=studentD.email
                Log.d("inside","dispatcher main")

            }
        }


    }


    override fun onStart() {
        super.onStart()
        broadcastReceiver= ConnectionReceiver()
        registerNetworkBroadcast()



    }

    override fun onStop() {
        super.onStop()
        unregisterNetwork()


    }

    private fun registerNetworkBroadcast()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            Log.d("inside ","register network")
            requireActivity().registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }

    private fun unregisterNetwork()
    {
        try{
            activity?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(broadcastReceiver) }
        }
        catch (e: IllegalArgumentException)
        {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterNetwork()
    }
}

