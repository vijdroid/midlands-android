package com.chris.thomson.midlandsriders.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.chris.thomson.midlandsriders.Adapters.EventViewAdapter
import com.chris.thomson.midlandsriders.Helpers.LoaderHelper


import com.chris.thomson.midlandsriders.R
import com.chris.thomson.midlandsriders.ViewModels.Event
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.fragment_events.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EventsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EventsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class EventsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null


    private var mAdapter: EventViewAdapter? = null

    private var firestoreDB: FirebaseFirestore? = null
    private var firestoreListener: ListenerRegistration? = null

    private var loader: LoaderHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loader = LoaderHelper(activity)

        firestoreDB = FirebaseFirestore.getInstance()
        loadEventsList()

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
         * @return A new instance of fragment EventsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                EventsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


    private fun loadEventsList() {

        loader!!.showDialog()

        firestoreDB!!.collection("events")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val eventList = mutableListOf<Event>()

                        for (doc in task.result!!) {
                            val event = doc.toObject<Event>(Event::class.java)
                            event.id = doc.id
                            eventList.add(event)
                        }

                        mAdapter = EventViewAdapter(eventList, activity!!, firestoreDB!!)
                        val mLayoutManager = LinearLayoutManager(activity!!)
                        events_recycler_view.layoutManager = mLayoutManager
                        events_recycler_view.itemAnimator = DefaultItemAnimator()
                        events_recycler_view.adapter = mAdapter
                    } else {
                        Toast.makeText(context,"Error fetching events", Toast.LENGTH_LONG).show()
                    }
                    loader!!.hideDialog(2)
                }
    }


}
