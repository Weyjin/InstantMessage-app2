package com.instant.message_app.entity

class GroupChat {
    private var id: Int = 0
    private var name: String? = null
    private var count: Int = 0
    private var img: String? = null

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

    fun getCount(): Int {
        return count
    }

    fun setCount(count: Int) {
        this.count = count
    }

    fun getImg(): String {
        val imgs = arrayOf(
            "images/user1-default.jpg",
            "images/user2-default.jpg",
            "images/user3-default.jpg",
            "images/user4-default.jpg",
            "images/user5-default.jpg",
            "images/user6-default.jpg",
            "images/user7-default.jpg",
            "images/user8-default.jpg",
            "images/user9-default.jpg"
        )
        val random = (Math.random() * (imgs.size - 1)).toInt()
        return imgs[random]
    }

    fun setImg(img: String) {
        this.img = img
    }
}