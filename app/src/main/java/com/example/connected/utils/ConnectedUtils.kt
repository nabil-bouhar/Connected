package com.example.connected.utils

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import java.util.regex.Pattern

object ConnectedUtils {

    private val NAME_PATTERN: Pattern =
        Pattern.compile("([A-Z][a-z]*)([\\s'-][A-Z][a-z]*)*")

    fun filterValidateName(name: String): Boolean {
        return NAME_PATTERN.matcher(name).matches()
    }

    fun filterValidateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun filterValidatePassword(password: String): Boolean {
        return password.length in 7..26
    }

    fun filterValidatePassword(password1: String, password2: String): Boolean {
        return password1.length in 7..26 && password1 == password2
    }

    fun filterValidatePhoneNumber(number: String): Boolean {
        return android.util.Patterns.PHONE.matcher(number).matches()
    }

    fun changeActivityAndClearStack(
        context: Context,
        destination: Class<*>,
        toastMessage: String?
    ) {

        val shouldWaitToDisplayToastMessage: Long = if (toastMessage != null) 500 else 0

        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT)
                .show()
        }

        Handler(Looper.getMainLooper()).postDelayed(
            {
                // clear activities stack to update data !
                Intent(context, destination).let {
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(it)
                }
            }, shouldWaitToDisplayToastMessage
        )
    }
}