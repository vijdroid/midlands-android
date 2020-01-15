package com.chris.thomson.midlandsriders.Managers

import com.chris.thomson.midlandsriders.ViewModels.Event

class EventManager {

    companion object {

        fun initializeEvents(): Array<Event> {

            val events = mutableListOf<Event>()

            return events.toTypedArray()
        }
    }

}