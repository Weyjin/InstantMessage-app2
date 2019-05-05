package com.instant.message_app.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.instant.message_app.R
import com.instant.message_app.constants.SocketConstant
import com.instant.message_app.entity.GroupChat
import com.instant.message_app.ui.CircleImageView
import com.instant.message_app.utils.LoadImagesTask

class GroupChatRecyclerViewAdapter : RecyclerView.Adapter<GroupChatRecyclerViewAdapter.ViewHolder> {

     private var mContext: Context
     private var groupChats: List<GroupChat>
     private var inflater: LayoutInflater
     private var listener:OnItemClickListener? = null

    constructor(context: Context, groupChats: List<GroupChat>){
        this.mContext = context
        this.groupChats = groupChats
        this.inflater = LayoutInflater.from(mContext)
    }
    fun setOnItemClickListener(listener: GroupChatRecyclerViewAdapter.OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GroupChatRecyclerViewAdapter.ViewHolder {

        val view = inflater.inflate(R.layout.group_chat_item_layout, viewGroup, false)

        return GroupChatRecyclerViewAdapter.ViewHolder(view)

    }

    override fun onBindViewHolder(viewHolder: GroupChatRecyclerViewAdapter.ViewHolder, position: Int) {
        viewHolder.name.text=groupChats[position].getName()
        viewHolder.count.text=groupChats[position].getCount().toString()
        //viewHolder.synopsis.setText(groupChats.get(position).getName());
        viewHolder.itemView.setOnClickListener {this.listener?.onClick(position) }
        val uri = SocketConstant.HOST_NAME + "/" + groupChats[position].getImg()

        LoadImagesTask(viewHolder.img).execute(uri)

    }

    override fun getItemCount(): Int {
        return groupChats.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val name: TextView= itemView.findViewById(R.id.text_name)
         val synopsis: TextView = itemView.findViewById(R.id.text_synopsis)
         val count: TextView= itemView.findViewById(R.id.text_count)
         val img: CircleImageView= itemView.findViewById(R.id.img)
    }

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

}