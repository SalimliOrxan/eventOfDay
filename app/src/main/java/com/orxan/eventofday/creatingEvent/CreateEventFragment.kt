package com.orxan.eventofday.creatingEvent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.orxan.eventofday.R
import com.orxan.eventofday.eventsPage.EventsFragment
import kotlinx.android.synthetic.main.create_event.*

class CreateEventFragment : Fragment(), CreateEventView {

    companion object {
        private val games : ArrayList<String> = ArrayList()
    }

    private var percent : String? = null
    private lateinit var presenter : CreateEventPresenter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.create_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addingEventRec.layoutManager = LinearLayoutManager(view.context)
        addingEventRec.adapter = MyAdapter(view.context)

        presenter = CreateEventPresenter(this)

        initSpinner(view.context)
        handleClicks()
    }


    private fun handleClicks() {
        var username : String? = null
        var event = Event()

        addEvent.setOnClickListener {
            val mTitle       = title.text.toString().trim()
            val mExplanation = explanation.text.toString().trim()
            val home         = clubHome.text.toString().trim()
            val away         = clubAway.text.toString().trim()
            val types        = type.text.toString().trim()

            if(mTitle.isNotEmpty() && mExplanation.isNotEmpty() && home.isNotEmpty() && away.isNotEmpty() && types.isNotEmpty()) {
                val game = "$home - $away  $types     $percent"
                games.add(game)
                event = Event()

                if(username == null){
                    val database = FirebaseDatabase.getInstance()
                    val users = database.getReference("users")

                    val listener = object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {}

                        override fun onDataChange(p0: DataSnapshot) {
                            username = p0.getValue(String::class.java)
                            event.username = username
                        }
                    }

                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if(currentUser != null){
                        users
                            .child(currentUser.uid)
                            .child("username")
                            .addListenerForSingleValueEvent(listener)
                    }
                }

                var details : String? = null
                games.forEach {s: String ->
                    details += s + "\n"
                }
                event.title       = mTitle
                event.details     = details
                event.username    = username
                event.explanation = mExplanation

                addingEventRec.adapter?.notifyItemInserted(games.size - 1)
                clubHome.text.clear()
                clubAway.text.clear()
                type.text.clear()
            } else showMessage("fill blanks")
        }

        publish.setOnClickListener {
            if(games.size > 0){
                games.clear()
                presenter.publish(event)
            } else showMessage("event is empty")
        }
    }

    private fun initSpinner(context: Context) {
        val percents = resources.getStringArray(R.array.percents)
        val adapter = ArrayAdapter<String>(context, R.layout.item_spinner, percents)
        trust.adapter = adapter
        trust.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p: AdapterView<*>?) {}

            override fun onItemSelected(p: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                percent = percents[position]
            }
        }
    }

    private class MyAdapter(context : Context) : RecyclerView.Adapter<MyHolder>() {
        val mContext : Context = context

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val v : View = LayoutInflater.from(mContext).inflate(R.layout.item_added_event, parent, false)
            return MyHolder(v)
        }

        override fun getItemCount(): Int {
            return games.size
        }

        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            holder.name.text = games[position]

            holder.remove.setOnClickListener {
                games.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }

    private class MyHolder(v : View) : RecyclerView.ViewHolder(v) {
        val name   : TextView = v.findViewById(R.id.itemAdded)
        val remove : ImageButton = v.findViewById(R.id.remove)
    }

    override fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToEventsPage() {
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.appHolder, EventsFragment())
            ?.commit()
    }
}