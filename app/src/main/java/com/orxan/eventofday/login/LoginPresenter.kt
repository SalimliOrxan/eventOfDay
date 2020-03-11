package com.orxan.eventofday.login

import com.google.firebase.auth.FirebaseAuth

class LoginPresenter(private val view : LoginView) {

    fun forgot(email : String){
        val auth = FirebaseAuth.getInstance()

        auth.sendPasswordResetEmail(email).addOnCompleteListener {task ->
            if(task.isSuccessful){
                view.showMessage("Reset email was sent")
            } else view.showMessage(task.exception?.message)
        }
    }

    fun login(email : String, password : String){
        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->
            if(task.isSuccessful){
                val user = auth.currentUser
                if(user != null){
                    if(user.isEmailVerified){
                        view.navigateToApp()
                    } else {
                        auth.signOut()
                        view.showMessage("please verify email")
                    }
                }
            } else view.showMessage(task.exception?.message)
        }
    }
}