package com.chris.thomson.midlandsriders.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
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


class EventViewAdapter(
        private val eventList: MutableList<Event>,
        private val context: Context,
        private val firestoreDB: FirebaseFirestore)
    : RecyclerView.Adapter<EventViewAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)

        return EventViewHolder(view).listen {position, type ->
            val event = eventList.get(position)
            goToEventDetail(event)
        }
    }

    private fun goToEventDetail(event: Event){
        val intent = Intent(context, EventDetailActivity::class.java)
        intent.putExtra("event_title", event.title)
        intent.putExtra("event_description", event.description)
        intent.putExtra("event_img", event.image)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        (context as Activity).overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
    }


    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]

        holder.title.text = event.title
        holder.description.text = event.shortDescription
        Picasso.get()
                .load(event.image)
                .error(R.drawable.mr_circular_logo)
                .into(holder.itemView.event_img)



    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    inner class EventViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var title: TextView
        internal var description: TextView
        internal var image: ImageView

        init {
            title = view.findViewById(R.id.event_title)
            description = view.findViewById(R.id.event_description)
            image = view.findViewById(R.id.event_img)

        }
    }

    fun <T: RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int ) -> Unit): T{
        itemView.setOnClickListener {
            event.invoke(adapterPosition, itemViewType)
        }
        return this
    }


}