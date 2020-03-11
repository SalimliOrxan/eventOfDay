package com.orxan.eventofday.creatingEvent

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class CreateEventPresenter(private val view : CreateEventView) {

    fun publish(mEvent : Event){
        val database = FirebaseDatabase.getInstance()
        val date = database.getReference("date")

        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                var last = p0.getValue(Int::class.java)

                if(last != null) last++
                else last = 1

                val event = date
                    .child(getCurrentDate())
                    .child("events")
                    .child(last.toString())

                event.child("title").setValue(mEvent.title)
                event.child("details").setValue(mEvent.details)
                event.child("username").setValue(mEvent.username)
                event.child("explanation").setValue(mEvent.explanation)

                date
                    .child(getCurrentDate())
                    .child("last")
                    .setValue(last)

                view.navigateToEventsPage()
            }
        }

        date
            .child(getCurrentDate())
            .child("last")
            .addListenerForSingleValueEvent(listener)
    }

    private fun getCurrentDate():String {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(date)
    }
}