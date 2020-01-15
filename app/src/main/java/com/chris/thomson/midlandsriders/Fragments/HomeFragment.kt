package com.chris.thomson.midlandsriders.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.chris.thomson.midlandsriders.EventDetailActivity
import com.chris.thomson.midlandsriders.Helpers.DateHelper
import com.chris.thomson.midlandsriders.Helpers.LoaderHelper

import com.chris.thomson.midlandsriders.R
import com.chris.thomson.midlandsriders.ViewModels.Event
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var nextEvent: Event? = null
    private var pStatus = 0
    private val handler = Handler()
    private var firestoreDB: FirebaseFirestore? = null
    private var loader: LoaderHelper? = null
    private var event: Event = Event()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        loader = LoaderHelper(activity)

        firestoreDB = FirebaseFirestore.getInstance()
        var view = inflater.inflate(R.layout.fragment_home, container, false)

        view.txtNextEventDetails.setOnClickListener{
            v ->
           goToEventDetail()
        }

        getNextEvent()

        return view
    }


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


    private fun setUpText(event:Event){

        val daysBetween = DateHelper.getDaysBetween(event.date)

        txtTimeLeft.text = daysBetween.toString()

        if (daysBetween <= 1)
            txtLapseLeft.text = getString(R.string.day)
        txtNextEventDetails.text = event.title

    }

    private fun goToEventDetail(){
        val intent = Intent(context, EventDetailActivity::class.java)
        intent.putExtra("event_title", this.event.title)
        intent.putExtra("event_description", this.event.description)
        intent.putExtra("event_img", this.event.image)
        startActivity(intent)
        activity!!.overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
    }

    private fun getNextEvent() {

        loader!!.showDialog()

        firestoreDB!!.collection("events")
                .whereGreaterThanOrEqualTo("date", Date())
                .limit(1)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result!!) {
                            this.event = doc.toObject<Event>(Event::class.java)
                            this.event.id = doc.id
                            setUpText(event)
                        }

                    } else {
                        Toast.makeText(context,"Error fetching event", Toast.LENGTH_LONG).show()
                    }
                   loader!!.hideDialog()
                }


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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }



}
