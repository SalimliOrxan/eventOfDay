package com.orxan.eventofday.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.orxan.eventofday.R
import com.orxan.eventofday.loginRegister.LoginRegister

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if(user != null){
            startActivity(Intent(this, AppActivity::class.java))
            finish()
        } else {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.startHolder, LoginRegister())
                .commit()
        }
    }
}
