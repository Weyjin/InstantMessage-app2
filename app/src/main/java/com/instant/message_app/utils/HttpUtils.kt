package com.instant.message_app.utils

import com.instant.message_app.constants.SocketConstant
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class HttpUtils {
    companion object {
        fun getGroups(): String? {
            val client = OkHttpClient()

            try {
                val request = Request.Builder().url(SocketConstant.GET_GROUPS_ADDRESS).build()
                var response: Response? = null
                response = client.newCall(request).execute()
                return if (response!!.isSuccessful) {
                    response.body().string()
                } else {
                    throw IOException("Unexpected code $response")
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null

        }


        fun getGroupChats(id: String): String? {
            val client = OkHttpClient()
            val formBody = FormBody.Builder()
                .add("userId", id)
                .build()

            val request = Request.Builder()
                .url(SocketConstant.GET_GROUP_CHATS_ADDRESS)
                .post(formBody)
                .build()

            var response: Response? = null
            try {
                response = client.newCall(request).execute()
                return if (response!!.isSuccessful) {
                    response.body().string()
                } else {
                    throw IOException("Unexpected code $response")
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null

        }


        fun login(name: String, password: String): String? {

            val client = OkHttpClient()
            val formBody = FormBody.Builder()
                .add("name", name)
                .add("password", password)
                .build()

            val request = Request.Builder()
                .url(SocketConstant.LOGIN_ADDRESS)
                .post(formBody)
                .build()

            var response: Response? = null
            try {
                response = client.newCall(request).execute()
                return if (response!!.isSuccessful) {
                    response.body().string()
                } else {
                    throw IOException("Unexpected code $response")
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null

        }
    }
}