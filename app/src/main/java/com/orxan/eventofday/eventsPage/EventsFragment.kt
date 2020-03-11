package com.orxan.eventofday.eventsPage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.orxan.eventofday.R
import com.orxan.eventofday.creatingEvent.Event
import com.orxan.eventofday.creatingEvent.CreateEventFragment
import kotlinx.android.synthetic.main.events_page.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventsFragment : Fragment(), EventsView{

    companion object {
        private val mainEvents : ArrayList<Event> = ArrayList()
        private var selectedPosition = 0
        private lateinit var presenter: EventsPresenter
        private lateinit var selectedDate: String
    }

    private lateinit var mContext: Context


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.events_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = view.context

        eventRec.layoutManager = LinearLayoutManager(view.context)
        eventRec.adapter = MyAdapter(view.context)

        presenter = EventsPresenter(this)
        presenter.getEventDates()
        handleClicks()
    }


    private fun handleClicks(){
        createEvent.setOnClickListener {
            fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.appHolder, CreateEventFragment())
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    private class MyAdapter(context : Context) : RecyclerView.Adapter<MyHolder>() {
        val mContext : Context = context
        val likeColor   = ContextCompat.getColor(mContext, android.R.color.holo_red_dark)
        val unlikeColor = ContextCompat.getColor(mContext, android.R.color.black)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val v : View = LayoutInflater.from(mContext).inflate(R.layout.item_event, parent, false)
            return MyHolder(v)
        }

        override fun getItemCount(): Int {
            return mainEvents.size
        }

        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            holder.title.text       = mainEvents[position].title
            holder.username.text    = mainEvents[position].username
            holder.details.text     = mainEvents[position].details
            holder.explanation.text = mainEvents[position].explanation


            val count = mainEvents[position].likeCount
            if(count > 0){
                holder.likeCount.visibility = View.VISIBLE
                holder.likeCount.text = count.toString()
            } else holder.likeCount.visibility = View.GONE

            if(isLiked(mainEvents[position].likes)){
                holder.liked = true
                holder.like.setColorFilter(likeColor)
            } else {
                holder.liked = false
                holder.like.setColorFilter(unlikeColor)
            }

            holder.like.setOnClickListener {
                selectedPosition = position
                val likes = mainEvents[position].likes
                val likeCount = mainEvents[position].likeCount

                if(holder.liked){
                    holder.liked = false
                    holder.like.setColorFilter(unlikeColor)
                    presenter.doUnLike(likes, likeCount, selectedDate, position)
                } else {
                    holder.liked = true
                    holder.like.setColorFilter(likeColor)
                    presenter.doLike(likes, likeCount, selectedDate, position)
                }
            }
        }

        private fun isLiked(likes : String) : Boolean {
            val user = FirebaseAuth.getInstance().currentUser
            System.out.println("data - : $likes")
            if(user != null && likes.contains(user.uid)){
                return true
            }
            return false
        }
    }

    private class MyHolder(v : View) : RecyclerView.ViewHolder(v) {
        val title       : TextView = v.findViewById(R.id.eventTitle)
        val username    : TextView = v.findViewById(R.id.userName)
        val details     : TextView = v.findViewById(R.id.eventDetails)
        val explanation : TextView = v.findViewById(R.id.eventExplanation)
        val like        : ImageButton = v.findViewById(R.id.eventLike)
        val likeCount   : TextView = v.findViewById(R.id.likeCount)
        var liked = false
    }

    override fun fillEventList(events: ArrayList<Event>) {
        if(events.size > 0){
            eventRec.visibility    = View.VISIBLE
            emptyEvents.visibility = View.GONE

            mainEvents.clear()
            mainEvents.addAll(events)
            eventRec?.adapter?.notifyDataSetChanged()
        } else {
            eventRec.visibility    = View.GONE
            emptyEvents.visibility = View.VISIBLE
        }
    }

    override fun initSpinner(dates : ArrayList<String>){
        val today = getCurrentDate()
        if(!dates.contains(today)){
            dates.add(today)
            eventRec.visibility    = View.GONE
            emptyEvents.visibility = View.VISIBLE
        } else {
            eventRec.visibility    = View.VISIBLE
            emptyEvents.visibility = View.GONE
        }

        val adapter = ArrayAdapter<String>(mContext, R.layout.item_spinner, dates)
        dateFilter.adapter = adapter

        dateFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                selectedDate = dates[position]
                presenter.getEvents(selectedDate)
            }
        }

        dateFilter.setSelection(dates.size - 1)
    }

    override fun applyChangeEvent(event: Event) {
        mainEvents.removeAt(selectedPosition)
        mainEvents.add(selectedPosition, event)
        eventRec?.adapter?.notifyDataSetChanged()
    }

    override fun setVisibility() {
        eventRec.visibility    = View.GONE
        emptyEvents.visibility = View.VISIBLE
    }

    private fun getCurrentDate():String {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(date)
    }


}