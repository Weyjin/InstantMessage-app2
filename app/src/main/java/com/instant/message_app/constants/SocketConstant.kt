package com.instant.message_app.constants

class SocketConstant {
    companion object {
        const val HOST = "127.0.0.1:8080"//你自己的地址
        const val HOST_NAME = "http://$HOST"

        const val CHAT_MESSAGE_ADDRESS = "ws://$HOST/websocket/OneToOne/"
        const val GROUP_CHATS_MESSAGE_ADDRESS = "ws://$HOST/websocket/OneToMultiple/_g"
        const val SCAN_CODE_ADDRESS = "ws://$HOST/websocket/ScanCode/"


        const val GET_GROUPS_ADDRESS = "$HOST_NAME/api/getGroups"

        const val GET_GROUP_CHATS_ADDRESS = "$HOST_NAME/api/getGroupChats"

        const val LOGIN_ADDRESS = "$HOST_NAME/api/login"
    }
}