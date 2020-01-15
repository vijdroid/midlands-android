package com.chris.thomson.midlandsriders

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event_detail.*

class EventDetailActivity : AppCompatActivity() {
    private var eventTitle : String = ""
    private var eventDescription : String = ""
    private var eventImg : String = ""
    companion object {
        val EVENT_TITLE = "event_title"
        val EVENT_DESCRIPTION = "event_description"
        val EVENT_IMAGE = "event_img"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.setTitle(null)

        setContentView(R.layout.activity_event_detail)

        this.eventTitle = intent.getStringExtra(EVENT_TITLE)
        this.eventDescription = intent.getStringExtra(EVENT_DESCRIPTION)
        this.eventImg = intent.getStringExtra(EVENT_IMAGE)

        event_detail_title.text = this.eventTitle
        event_detail_description.text = this.eventDescription

        Picasso.get()
                .load(this.eventImg)
                .placeholder(R.drawable.mr_circular_logo)
                .error(R.drawable.mr_circular_logo)
                .into(event_detail_img)



    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.stay, R.anim.slide_out_up)
        finish()
    }

}
