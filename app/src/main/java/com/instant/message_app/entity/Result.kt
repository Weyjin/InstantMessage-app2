package com.instant.message_app.entity

class Result {
    private var id: Int = 0
    private var describe: String? = null
    private var name: String? = null
    private var img: String? = null

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getDescribe(): String? {
        return describe
    }

    fun setDescribe(describe: String) {
        this.describe = describe
    }

    fun getName(): String? {

        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getImg(): String? {

        return img
    }

    fun setImg(img: String) {
        this.img = img
    }
}