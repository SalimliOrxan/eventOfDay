package com.orxan.eventofday.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.orxan.eventofday.R
import com.orxan.eventofday.login.LoginFragment
import kotlinx.android.synthetic.main.register.*

class RegisterFragment : Fragment(), RegisterView {

    private lateinit var presenter : RegisterPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = RegisterPresenter(this)
        handleClicks()
    }



    private fun handleClicks() {
        back.setOnClickListener {
            back()
        }

        register.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val name      = username.text.toString().trim()
        val mail      = email.text.toString().trim()
        val password  = password.text.toString().trim()
        val passwordA = passwordAgain.text.toString().trim()

        presenter.register(name, mail, password, passwordA)
    }

    override fun back() {
        fragmentManager?.popBackStack()
    }

    override fun showMessage(message: String?) {
        if(message != null)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToLoginScreen() {
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.startHolder, LoginFragment())
            ?.commit()
    }
}