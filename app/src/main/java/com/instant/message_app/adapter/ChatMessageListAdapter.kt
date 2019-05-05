package com.instant.message_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.instant.message_app.R
import com.instant.message_app.entity.ChatMessage

class ChatMessageListAdapter : BaseAdapter {

      var mContext: Context
      var chatMessages: List<ChatMessage>

    constructor(context: Context, chatMessages: List<ChatMessage>){
        this.mContext = context
        this.chatMessages = chatMessages
    }

    override fun getCount(): Int {
        return chatMessages.size
    }

    override fun getItem(position: Int): Any {
        return chatMessages[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val chatMessage = getItem(position) as ChatMessage
        val holder: ViewHolder
        if (convertView == null) {
            holder = ViewHolder()
            if (chatMessage.isCurrent()) {
                convertView =
                    LayoutInflater.from(mContext).inflate(R.layout.chat_message_item_right_layout, parent, false)
                holder.name = convertView!!.findViewById(R.id.text_name)
                holder.message = convertView.findViewById(R.id.text_message)
                convertView.tag = holder
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.chat_message_item_left_layout, null)
                holder.name = convertView!!.findViewById(R.id.text_name)
                holder.message = convertView.findViewById(R.id.text_message)
                convertView.tag = holder
            }
        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.name?.text=chatMessage.getUser()?.getName()
        holder.message?.text=chatMessage.getMessage()

        return convertView
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatMessages[position].isCurrent() === true) 1 else 0
    }

    override fun getViewTypeCount(): Int {
        return 2
    }

     class ViewHolder {
        lateinit var name: TextView
         lateinit var message: TextView
    }

}