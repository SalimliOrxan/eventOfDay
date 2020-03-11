package com.orxan.eventofday.register

interface RegisterView {

    fun back()
    fun showMessage(message : String?)
    fun navigateToLoginScreen()
}