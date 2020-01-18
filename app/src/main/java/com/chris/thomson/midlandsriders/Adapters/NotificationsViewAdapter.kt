package com.chris.thomson.midlandsriders.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chris.thomson.midlandsriders.EventDetailActivity
import com.chris.thomson.midlandsriders.R
import com.chris.thomson.midlandsriders.ViewModels.Event
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.event_item.view.*

class NotificationsViewAdapter()
    : RecyclerView.Adapter<NotificationsViewAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.title.text = "There's a bike race in November!"
        holder.body.text = "Everyone is welcome to attend on that day."
    }

    inner class NotificationViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var title: TextView
        internal var body: TextView

        init {
            title = view.findViewById(R.id.notification_title)
            body = view.findViewById(R.id.notification_body)
        }
    }
}