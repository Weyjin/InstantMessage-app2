package com.instant.message_app.utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.instant.message_app.entity.*
import java.util.ArrayList

class JsonUtils {
    companion object {
        fun getChatMessage(json: String): ChatMessage {
            val `object` = JSONObject.parseObject(json)
            val chatMessage = ChatMessage()
            chatMessage.setMessage(`object`.getString("message"))
            chatMessage.setCurrent(`object`.getBoolean("current")!!)
            chatMessage.setUser(getUser(`object`.getString("user")))
            return chatMessage
        }

        fun getGroups(json: String): List<Group> {

            val groups = ArrayList<Group>()
            val objects = JSON.parseArray(json)
            for (i in objects.indices) {
                val `object` = objects[i] as JSONObject
                val group = Group()
                group.setId(Integer.parseInt(`object`["id"].toString()))
                group.setName(`object`["name"].toString())
                val users = getUsers(`object`["users"].toString())
                group.setUsers(users)
                groups.add(group)
            }

            return groups
        }

        private fun getUser(json: String): Result {
            val `object` = JSONObject.parseObject(json)
            val result = Result()
            result.setName(`object`.getString("name"))
            result.setImg(`object`.getString("img"))
            return result
        }

       fun getLoginResult(json: String): LoginResult {
            val `object` = JSONObject.parseObject(json)
            val result = LoginResult()

            val msg = `object`.getString("msg")
            if (msg == "error") {
                result.setMsg(`object`.getString("msg"))
                return result
            } else {
                result.setUserName(`object`.getString("userName"))
                result.setSignature(`object`.getString("signature"))
                result.setUserId(`object`.getInteger("userId")!!)
                result.setMsg(`object`.getString("msg"))
                return result
            }

        }


        private fun getUsers(json: String): List<Result> {
            val list = ArrayList<Result>()
            val jsonArray = JSON.parseArray(json)

            for (i in jsonArray.indices) {
                val result = Result()
                result.setId(jsonArray.getJSONObject(i).getInteger("id")!!)
                result.setName(jsonArray.getJSONObject(i).getString("name"))
                result.setImg(jsonArray.getJSONObject(i).getString("img"))
                result.setDescribe(jsonArray.getJSONObject(i).getString("describe"))
                list.add(result)
            }
            return list
        }


        fun getGroupChats(json: String): List<GroupChat> {
            val groupChats = ArrayList<GroupChat>()
            val jsonArray = JSON.parseArray(json)

            for (i in jsonArray.indices) {
                val groupChat = GroupChat()
                groupChat.setId(jsonArray.getJSONObject(i).getInteger("id")!!)
                groupChat.setName(jsonArray.getJSONObject(i).getString("name"))
                groupChat.setImg(jsonArray.getJSONObject(i).getString("img"))
                groupChat.setCount(jsonArray.getJSONObject(i).getInteger("count")!!)
                groupChats.add(groupChat)
            }

            return groupChats

        }
    }

}