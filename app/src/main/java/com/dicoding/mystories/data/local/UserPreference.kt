package com.dicoding.mystories.data.local

import android.content.Context
import com.dicoding.mystories.model.UserModel

internal class UserPreference(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun setUser(value: UserModel) {
        val editor = preferences.edit()
        editor.putString(NAME, value.name)
        editor.putString(USERID, value.userId)
        editor.putString(TOKEN, value.token)
        editor.apply()
    }
    fun getUser(): UserModel {
        val model = UserModel()
        model.name = preferences.getString(NAME, "")
        model.userId = preferences.getString(USERID, "")
        model.token = preferences.getString(TOKEN, "")
        return model
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val USERID = "userId"
        private const val TOKEN = "token"
    }
}