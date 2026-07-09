package com.manual.mediation.library.sotadlib.utils

import android.content.Context
import android.content.SharedPreferences

class PrefHelper(context: Context) {
    companion object{
        const val KEY_SELECTED_LANGUAGE_ = "KEY_SELECTED_LANGUAGE_"
    }
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("SOTAdLib", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun putString(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    fun getStringDefault(key: String, default: String): String? {
        return sharedPreferences.getString(key, default)
    }

    fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    fun getBooleanDefault(key: String, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    fun clear() {
        editor.clear().apply()
    }

    fun getSelectedLanguageCode(): String {
        return sharedPreferences.getString(KEY_SELECTED_LANGUAGE_, "en") ?: "en"
    }

    fun setSelectedLanguageCode(languageCode: String) {
        sharedPreferences.edit().putString(KEY_SELECTED_LANGUAGE_, languageCode).apply()
    }
}