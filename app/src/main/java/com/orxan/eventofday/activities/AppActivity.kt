package com.orxan.eventofday.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.orxan.eventofday.R
import com.orxan.eventofday.eventsPage.EventsFragment

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.appHolder, EventsFragment())
            .commit()
    }
}