package com.chris.thomson.midlandsriders.Fragments

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.chris.thomson.midlandsriders.Adapters.CalendarEventsAdapter
import com.chris.thomson.midlandsriders.Adapters.EventViewAdapter
import com.chris.thomson.midlandsriders.Adapters.NotificationsViewAdapter
import com.events.calendar.views.EventsCalendar
import com.chris.thomson.midlandsriders.Managers.FontsManager

import com.chris.thomson.midlandsriders.R
import com.chris.thomson.midlandsriders.ViewModels.Event
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_events.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CalendarFragment : Fragment(), EventsCalendar.Callback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private var firestoreDB: FirebaseFirestore? = null
    private var mAdapter: CalendarEventsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        mAdapter = CalendarEventsAdapter(context)
    }
    override fun onDayLongPressed(selectedDate: Calendar?) {
        Log.e("LONG", "CLICKED")
    }

    override fun onMonthChanged(monthStartDate: Calendar?) {
        Log.e("MON", "CHANGED")
        Log.d("MON", monthStartDate?.toString())
        val gc = monthStartDate as GregorianCalendar
        val month = gc.time.month
        Log.d("MONTH", month.toString())
        loadEventsList(month)
    }

    override fun onDaySelected(selectedDate: Calendar?) {
        Log.e("SHORT", "CLICKED")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_calendar, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //attachAdapter()
        bindCalendar()
        firestoreDB = FirebaseFirestore.getInstance()
        Log.d("CalendarFragment", "showing one calendar...")
    }
    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context

        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CalendarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                CalendarFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    fun bindCalendar() {

        val today = Calendar.getInstance()
        val end = Calendar.getInstance()
        eventsCalendar.setSelectedTextColor(Color.WHITE)
        end.add(Calendar.YEAR, 2)
        eventsCalendar.setSelectionMode(eventsCalendar.MULTIPLE_SELECTION)
        eventsCalendar.setToday(today)
        eventsCalendar.setMonthRange(today, end)
        eventsCalendar.setWeekStartDay(Calendar.SUNDAY, false)
        eventsCalendar.setCurrentSelectedDate(today)
        eventsCalendar.setDatesTypeface(FontsManager.getTypeface(FontsManager.SHADOW_CARD, this.context!!))
        eventsCalendar.setMonthTitleTypeface(FontsManager.getTypeface(FontsManager.SHADOW_CARD, this.context!!))
        eventsCalendar.setWeekHeaderTypeface(FontsManager.getTypeface(FontsManager.SHADOW_CARD, this.context!!))
        eventsCalendar.setCallback(this)

        /*
        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH, 2)
        eventsCalendar.addEvent(c)
        c.add(Calendar.DAY_OF_MONTH, 3)
        eventsCalendar.addEvent(c)
        c.add(Calendar.DAY_OF_MONTH, 4)
        eventsCalendar.addEvent(c)
        c.add(Calendar.DAY_OF_MONTH, 7)
        eventsCalendar.addEvent(c)
*/

        val dc = Calendar.getInstance()
        dc.add(Calendar.DAY_OF_MONTH, 2)
//        eventsCalendar.disableDate(dc)
//        eventsCalendar.disableDaysInWeek(Calendar.SATURDAY, Calendar.SUNDAY)
    }

    private fun attachAdapter() {
        val mLayoutManager = LinearLayoutManager(activity!!)
        calendar_events_recycler_view.layoutManager = mLayoutManager
        calendar_events_recycler_view.itemAnimator = DefaultItemAnimator()
        calendar_events_recycler_view.adapter = mAdapter
    }

    //Load events from Firestore
    private fun loadEventsList(selectedMonth: Int) {

        firestoreDB!!.collection("events")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val eventList = mutableListOf<Event>()

                        for (doc in task.result!!) {
                            val event = doc.toObject<Event>(Event::class.java)
                            event.id = doc.id

                            if (event.date?.month == selectedMonth) {
                                eventList.add(event)
                            }
                        }

                        eventList.forEach {
                            Log.d("EVENT", it.date?.toString())
                        }

                        mAdapter?.events = eventList
                        val mLayoutManager = LinearLayoutManager(activity!!)
                        calendar_events_recycler_view.layoutManager = mLayoutManager
                        calendar_events_recycler_view.itemAnimator = DefaultItemAnimator()
                        calendar_events_recycler_view.adapter = mAdapter
                    } else {
                        Toast.makeText(context,"Error fetching events", Toast.LENGTH_LONG).show()
                    }
                }
    }
}
