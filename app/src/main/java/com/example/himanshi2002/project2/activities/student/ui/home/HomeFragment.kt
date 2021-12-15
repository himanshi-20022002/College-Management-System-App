package com.example.himanshi2002.project2.activities.student.ui.home

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.R
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.himanshi2002.project2.Utils.ConnectionReceiver
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.himanshi2002.project2.model.PostDetails
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class HomeFragment : Fragment() {


    private lateinit var recyclerViewPost: RecyclerView


    private lateinit var myAdapter: PostAdapter
    private lateinit var btn: FloatingActionButton
    lateinit var broadcastReceiver:BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

            setUpRecyclerView(view)

        btn = view.findViewById(R.id.floatingButton)
        btn.setOnClickListener {

            val i = Intent(activity, CreateNewPost::class.java)
            startActivity(i)

        }

        broadcastReceiver= ConnectionReceiver()
        registerNetworkBroadcast()


        return view


    }

    private fun setUpRecyclerView(view: View) {
        recyclerViewPost = view.findViewById(R.id.recyclerViewForPost)
        val query = FirebaseFirestore.getInstance().collection(Constants.POST).orderBy("createdAt",
            Query.Direction.DESCENDING)

        val option: FirestoreRecyclerOptions<PostDetails> =
            FirestoreRecyclerOptions.Builder<PostDetails>().setQuery(query,
                PostDetails::class.java).build()

        myAdapter = PostAdapter(option)
        recyclerViewPost.adapter = myAdapter
        recyclerViewPost.layoutManager = LinearLayoutManager(requireContext())
    }


    override fun onStart() {
        super.onStart()
        broadcastReceiver= ConnectionReceiver()
        registerNetworkBroadcast()
        myAdapter.startListening()


    }

    override fun onStop() {
        super.onStop()
        unregisterNetwork()

        myAdapter.stopListening()
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