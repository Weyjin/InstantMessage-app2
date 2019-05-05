package com.instant.message_app.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.instant.message_app.R
import com.instant.message_app.constants.SocketConstant
import com.instant.message_app.entity.ChatMessage
import com.instant.message_app.ui.CircleImageView
import com.instant.message_app.utils.LoadImagesTask

class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

     var chatMessages: List<ChatMessage>
     var context:Context

    constructor(context : Context,chatMessages:List<ChatMessage>){
        this.chatMessages=chatMessages
        this.context = context
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.chat_message_item_left_layout, viewGroup, false)
        when(viewType){
            1->{
                return ViewHolder(view)
            }
            0->{
             view = LayoutInflater.from(context).inflate(R.layout.chat_message_item_right_layout, viewGroup, false)
            return ViewHolder(view)
        }
        }
        return ViewHolder(view)

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text=chatMessages[position].getUser()?.getName()
        viewHolder.message.text=chatMessages[position].getMessage()
        val uri = SocketConstant.HOST_NAME + "/" + chatMessages[position].getUser()?.getImg()
        LoadImagesTask(viewHolder.img).execute(uri)
    }

    override fun getItemViewType(position: Int): Int {
        if(chatMessages[position].isCurrent()){
            return 0
        }else{
            return 1
        }
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }


   inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val name:TextView=itemView.findViewById(R.id.text_name)
            val message:TextView=itemView.findViewById(R.id.text_message)
            val img:CircleImageView=itemView.findViewById(R.id.img)

    }
}