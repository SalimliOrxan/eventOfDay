package com.orxan.eventofday.register

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterPresenter(private val view : RegisterView){

    fun register(username : String, email : String, password : String, passwordA : String){
        if(username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && passwordA.isNotEmpty()){
            if(password == passwordA){
                val auth = FirebaseAuth.getInstance()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {task ->
                        if(task.isSuccessful) {
                            val user = auth.currentUser

                            saveUserData(username, user?.uid)

                            user?.sendEmailVerification()?.addOnCompleteListener {task1 ->
                                if(task1.isSuccessful){
                                    view.showMessage("verification email was sent")
                                    view.navigateToLoginScreen()
                                } else view.showMessage("verification email sending was unsuccessful")
                            }
                        } else view.showMessage(task.exception?.message)
                    }
            } else view.showMessage("passwords didn't match")
        } else view.showMessage("fill blanks")
    }

    private fun saveUserData(username : String, UID : String?){
        if(UID != null){
            val database = FirebaseDatabase.getInstance()
            val users = database.getReference("users")
            users.child(UID).child("username").setValue(username)
        }
    }
}