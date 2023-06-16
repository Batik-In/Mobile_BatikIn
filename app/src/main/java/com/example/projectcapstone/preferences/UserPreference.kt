package com.example.projectcapstone.preferences

import android.content.Context
import com.example.projectcapstone.response.LoginResult

class UserPreference(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: LoginResult) {
        val editor = preferences.edit()
        editor.putString(NAME, value.name)
        editor.putString(USER_ID, value.userId)
        editor.putString(TOKEN, value.token)
        editor.apply()
    }

    fun getUser(): LoginResult {
        return LoginResult(
            name = preferences.getString(NAME, "") ?: "",
            userId = preferences.getString(USER_ID, "") ?: "",
            token = preferences.getString(TOKEN, "") ?: ""
        )
    }

    fun logOut() {
        val editor = preferences.edit()
        editor.clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val USER_ID = "user_id"
        private const val TOKEN = "toke"
    }
}