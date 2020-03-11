package com.orxan.eventofday.loginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.orxan.eventofday.R
import com.orxan.eventofday.login.LoginFragment
import com.orxan.eventofday.register.RegisterFragment
import kotlinx.android.synthetic.main.login_register.*

class LoginRegister : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleClicks()
    }



    private fun handleClicks() {
        loginR.setOnClickListener {
            login()
        }

        registerR.setOnClickListener {
            register()
        }
    }

    private fun login() {
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.startHolder, LoginFragment())
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun register() {
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.startHolder, RegisterFragment())
            ?.addToBackStack(null)
            ?.commit()
    }
}