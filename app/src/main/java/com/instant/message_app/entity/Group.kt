package com.instant.message_app.entity

class Group {
    private var id: Int = 0
    private var name: String? = null
    private var users: List<Result>? = null

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

    fun getUsers(): List<Result>? {
        return users
    }

    fun setUsers(users: List<Result>) {
        this.users = users
    }
}