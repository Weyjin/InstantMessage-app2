package com.instant.message_app.entity

class ChatMessage {
    private var message: String? = null
    private var user: Result? = null
    private var isCurrent: Boolean = false

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun getUser(): Result? {
        return user
    }

    fun setUser(user: Result) {
        this.user = user
    }

    fun isCurrent(): Boolean {
        return isCurrent
    }

    fun setCurrent(current: Boolean) {
        isCurrent = current
    }
}