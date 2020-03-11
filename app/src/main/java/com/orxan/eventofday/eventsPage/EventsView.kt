package com.orxan.eventofday.eventsPage

import com.orxan.eventofday.creatingEvent.Event

interface EventsView {

    fun fillEventList(events : ArrayList<Event>)
    fun initSpinner(dates : ArrayList<String>)
    fun applyChangeEvent(event : Event)
    fun setVisibility()
}