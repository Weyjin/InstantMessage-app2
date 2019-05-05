package com.instant.message_app.entity

class LoginResult {
    private var userName: String? = null
    private var userId: Int = 0
    private var signature: String? = null
    private var msg: String? = null

    fun getUserName(): String? {
        return userName
    }

    fun setUserName(userName: String) {
        this.userName = userName
    }

    fun getUserId(): Int {
        return userId
    }

    fun setUserId(userId: Int) {
        this.userId = userId
    }

    fun getSignature(): String? {
        return signature
    }

    fun setSignature(signature: String) {
        this.signature = signature
    }

    fun getMsg(): String? {
        return msg
    }

    fun setMsg(msg: String) {
        this.msg = msg
    }
}