package com.example.connected.utils

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object ConnectedUtils {

    private const val ONE_YEAR_IN_MILLIS = 31536000000
    private const val MIN_BIRTH_DATE = "1900-01-01"
    private val NAME_PATTERN: Pattern =
        Pattern.compile("^([ \\u00c0-\\u01ffa-zA-Z'\\-])+\$")
    private val PSEUDONYM_PATTERN: Pattern =
        Pattern.compile("([A-Za-z0-9_-]+)")

    fun filterValidateName(name: String): Boolean {
        return NAME_PATTERN.matcher(name).matches()
    }

    fun filterValidatePseudonym(pseudo: String): Boolean {
        return PSEUDONYM_PATTERN.matcher(pseudo).matches() && pseudo.length > 3
    }

    fun filterValidateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun filterValidatePassword(password: String): Boolean {
        return password.length in 7..26
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

    fun convertLongToTime(time: Long): String {
        return SimpleDateFormat("yyyy-MM-dd").format(Date(time))
    }

    fun convertDateToLong(date: String): Long {
        return SimpleDateFormat("yyyy-MM-dd").parse(date).time
    }

    fun getBirthMinDate(): Long {
        return convertDateToLong(MIN_BIRTH_DATE)
    }

    fun getBirthMaxDate(): Long {
        return System.currentTimeMillis() - ONE_YEAR_IN_MILLIS
    }

    fun booleanToInt(boolean: Boolean): Int {
        return if (boolean) 1 else 0
    }
}