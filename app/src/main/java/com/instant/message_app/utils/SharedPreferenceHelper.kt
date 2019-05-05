package com.instant.message_app.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceHelper {
    private  var mContext: Context
    private  var sharedPreferences: SharedPreferences
    private  var editor: SharedPreferences.Editor
    companion object {
        private  const val FILE_NAME = "sharedPreferences"
        private  const val USER_ID_KEY = "user_id"
        private  const val USER_NAME_KEY = "user_name"
        private  const val USER_SIGNATURE_KEY = "user_signature"
    }

    constructor(mContext: Context){
        this.mContext = mContext
        this.sharedPreferences = this.mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        this.editor = this.sharedPreferences.edit()
    }

    fun saveUserId(userId: Int) {
        editor.putInt(USER_ID_KEY, userId)
        editor.commit()
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt(USER_ID_KEY, 0)
    }

    fun saveUserName(username: String) {
        editor.putString(USER_NAME_KEY, username)
        editor.commit()
    }

    fun getUserName(): String {
        return sharedPreferences.getString(USER_NAME_KEY, "")
    }

    fun saveUserSignature(signature: String) {
        editor.putString(USER_SIGNATURE_KEY, signature)
        editor.commit()
    }

    fun getUserSignature(): String {
        return sharedPreferences.getString(USER_SIGNATURE_KEY, "")
    }

    fun clear() {
        editor.putInt(USER_ID_KEY, 0)
        editor.putString(USER_NAME_KEY, "")
        editor.putString(USER_SIGNATURE_KEY, "")
        editor.commit()
    }
}