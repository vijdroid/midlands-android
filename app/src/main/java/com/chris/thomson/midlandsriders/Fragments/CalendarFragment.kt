package com.chris.thomson.midlandsriders.Fragments

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.events.calendar.views.EventsCalendar
import com.chris.thomson.midlandsriders.Managers.FontsManager

import com.chris.thomson.midlandsriders.R
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CalendarFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CalendarFragment : Fragment(), EventsCalendar.Callback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onDayLongPressed(selectedDate: Calendar?) {
        Log.e("LONG", "CLICKED")
    }

    override fun onMonthChanged(monthStartDate: Calendar?) {
        Log.e("MON", "CHANGED")
    }

    override fun onDaySelected(selectedDate: Calendar?) {
        Log.e("SHORT", "CLICKED")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_calendar, container, false)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindCalendar()


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




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
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

        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH, 2)
        eventsCalendar.addEvent(c)
        c.add(Calendar.DAY_OF_MONTH, 3)
        eventsCalendar.addEvent(c)
        c.add(Calendar.DAY_OF_MONTH, 4)
        eventsCalendar.addEvent(c)
        c.add(Calendar.DAY_OF_MONTH, 7)
        eventsCalendar.addEvent(c)


        val dc = Calendar.getInstance()
        dc.add(Calendar.DAY_OF_MONTH, 2)
//        eventsCalendar.disableDate(dc)
//        eventsCalendar.disableDaysInWeek(Calendar.SATURDAY, Calendar.SUNDAY)
    }
}
