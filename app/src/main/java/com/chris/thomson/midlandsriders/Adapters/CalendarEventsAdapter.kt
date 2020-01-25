package com.chris.thomson.midlandsriders.Adapters


import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.chris.thomson.midlandsriders.EventDetailActivity
import com.chris.thomson.midlandsriders.R
import com.chris.thomson.midlandsriders.ViewModels.Event
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.event_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class CalendarEventsAdapter(context: Context?)
    : RecyclerView.Adapter<CalendarEventsAdapter.CalendarEventViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var events = emptyList<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarEventViewHolder {
        val view = inflater.inflate(R.layout.calendar_event_item, parent, false)
        return CalendarEventViewHolder(view)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: CalendarEventViewHolder, position: Int) {
        //holder.title.text = "There's a bike race in November!"
        //holder.body.text = "Everyone is welcome to attend on that day."
        val current = events[position]
        holder.title.text = current.title
        holder.date.text = current.date.toString()
    }

    inner class CalendarEventViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var title: TextView
        internal var date: TextView

        init {
            title = view.findViewById(R.id.calendar_event_title)
            date = view.findViewById(R.id.calendar_event_date)
        }
    }
}
