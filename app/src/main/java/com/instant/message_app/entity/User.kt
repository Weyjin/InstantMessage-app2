package com.instant.message_app.entity

class User {
    private var id: Int = 0
    private var name: String? = null
    private var password: String? = null
    private var signature: String? = null


    constructor(name: String, password: String, signature: String){
        this.name = name
        this.password = password
        this.signature = signature
    }

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun getSignature(): String? {
        return signature
    }

    fun setSignature(signature: String) {
        this.signature = signature
    }
}