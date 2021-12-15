package com.example.himanshi2002.project2.activities.student.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.Utils.NoticesAdapter
import com.example.himanshi2002.project2.Utils.TimeStamp
import com.example.himanshi2002.project2.model.NoticesDetails
import com.example.himanshi2002.project2.model.PostDetails
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.squareup.picasso.Picasso


class PostAdapter(options: FirestoreRecyclerOptions<PostDetails>) :
    FirestoreRecyclerAdapter<PostDetails, PostAdapter.PostViewHolder>(options)
{

    override fun onCreateViewHolder(parent: ViewGroup,
    viewType: Int
    ): PostViewHolder {
        val viewHolder =  PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cardview_for_post, parent, false))

        return viewHolder
    }


    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var postImage: ImageView
        var postTitle:TextView
        var userName: TextView
        var createdAt: TextView

        var userImage: ImageView

        init {
            postImage= itemView.findViewById(R.id.postImage)
            postTitle=itemView.findViewById(R.id.postTitle)
           userName = itemView.findViewById(R.id.userName)
             createdAt= itemView.findViewById(R.id.createdAt)

             userImage = itemView.findViewById(R.id.userImage)

        }

    }



    override fun onBindViewHolder(holder: PostViewHolder, position: Int,model:PostDetails) {


        holder.postTitle.text = model.post_description

        holder.userName.text = model.createdBy.first_Name
        Log.d("post created by is ",holder.userName.toString())

        holder.postTitle.text=model.post_description


        Glide.with(holder.userImage.context).load(model.createdBy.profile_image).circleCrop().into(holder.userImage)

        Glide.with(holder.postImage.context).load(model.post_image).into(holder.postImage)
        holder.createdAt.text = TimeStamp.getTimeAgo(model.createdAt)


    }



}

















