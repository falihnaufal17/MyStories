package com.dicoding.mystories.utils

import org.json.JSONException
import org.json.JSONObject

class ParseErrorMessage (
    private val errorBody: String?
) {
    fun parseErrorMessage(): String {
        return try {
            if (errorBody.isNullOrEmpty()) {
                "An unknown error occurred"
            } else {
                val jsonObject = JSONObject(errorBody)
                jsonObject.optString("message", "An unknown error occurred")
            }
        } catch (e: JSONException) {
            "An error occurred while parsing the error message"
        }
    }
}