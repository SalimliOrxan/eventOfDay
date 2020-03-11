package com.orxan.eventofday.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.orxan.eventofday.R
import com.orxan.eventofday.activities.AppActivity
import kotlinx.android.synthetic.main.login.*

class LoginFragment : Fragment(), LoginView {

    private lateinit var presenter: LoginPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LoginPresenter(this)
        handleClicks()
    }


    private fun handleClicks() {
        forgot.setOnClickListener {
            presenter.forgot(email.text.toString().trim())
        }

        login.setOnClickListener {
            presenter.login(email.text.toString().trim(), password.text.toString().trim())
        }
    }

    override fun showMessage(message: String?) {
        if(message != null)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToApp() {
        startActivity(Intent(context, AppActivity::class.java))
    }
}