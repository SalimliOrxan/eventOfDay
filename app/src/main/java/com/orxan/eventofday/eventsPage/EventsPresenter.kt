package com.orxan.eventofday.eventsPage

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.orxan.eventofday.creatingEvent.Event
import java.util.*
import kotlin.collections.ArrayList

class EventsPresenter(private val view : EventsView) {

    fun doLike(likes : String, likeCount : Int, selectedDate : String, position : Int){
        val database = FirebaseDatabase.getInstance()
        val date = database.getReference("date")

        val user = FirebaseAuth.getInstance().currentUser

        if(user != null){
            val data = likes + user.uid + " "
            date
                .child(selectedDate)
                .child("events")
                .child((position + 1).toString())
                .child("likes")
                .setValue(data)

            date
                .child(selectedDate)
                .child("events")
                .child((position + 1).toString())
                .child("likeCount")
                .setValue(likeCount + 1)
        }
    }

    fun doUnLike(likes : String, likeCount : Int, selectedDate : String, position : Int){
        val database = FirebaseDatabase.getInstance()
        val date = database.getReference("date")

        val user = FirebaseAuth.getInstance().currentUser

        if(user != null){
            val data = likes.replace(user.uid + " ", "")

            date
                .child(selectedDate)
                .child("events")
                .child((position + 1).toString())
                .child("likes")
                .setValue(data)

            val a = if(likeCount > 0) likeCount - 1 else 0

            date
                .child(selectedDate)
                .child("events")
                .child((position + 1).toString())
                .child("likeCount")
                .setValue(a)
        }
    }

    fun getEventDates(){
        val database = FirebaseDatabase.getInstance()
        val date = database.getReference("date")
        var count : Long = 0
        var a : Long = 0

        val dates = ArrayList<String>()

        val childListener = object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val data = p0.key
                if(data != null) dates.add(data)

                a++
                if(a == count){
                    date.removeEventListener(this)
                    view.initSpinner(dates)
                }
            }

            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

            override fun onChildRemoved(p0: DataSnapshot) {}
        }

        val childrenCountListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                count = p0.childrenCount
                date.removeEventListener(this)
                date.addChildEventListener(childListener)
            }
        }

        date.addListenerForSingleValueEvent(childrenCountListener)
    }

    fun getEvents(day : String){
        val database = FirebaseDatabase.getInstance()
        val date = database.getReference("date")

        val list = ArrayList<Event>()

        val childListener = object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val data = p0.getValue(Event::class.java)

                if(data != null)
                    list.add(data)

                System.out.println("onChildAdded - onChildAdded")
                view.fillEventList(list)
            }

            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val data = p0.getValue(Event::class.java)
                if(data != null)
                    view.applyChangeEvent(data)
            }

            override fun onChildRemoved(p0: DataSnapshot) {}
        }

        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.hasChild(day)){
                    date
                        .child(day)
                        .child("events")
                        .addChildEventListener(childListener)
                } else view.setVisibility()
                date.removeEventListener(this)
            }
        }

        date.addListenerForSingleValueEvent(listener)
    }
}