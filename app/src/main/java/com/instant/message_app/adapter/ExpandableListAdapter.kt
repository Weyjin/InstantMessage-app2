package com.instant.message_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.instant.message_app.R
import com.instant.message_app.constants.SocketConstant
import com.instant.message_app.entity.Group
import com.instant.message_app.ui.CircleImageView
import com.instant.message_app.utils.LoadImagesTask

class ExpandableListAdapter : BaseExpandableListAdapter {

     val mContext: Context
     val groups: List<Group>

    constructor(context: Context, groups: List<Group>){
        this.mContext = context
        this.groups = groups
    }

    override fun getGroupCount(): Int {
        return groups.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return groups[groupPosition].getUsers()!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return groups[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): com.instant.message_app.entity.Result {
        return groups[groupPosition].getUsers()!!?.get(childPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: GroupViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.group_item_layout, null)
            holder = GroupViewHolder()
            holder.mGroupName = convertView!!.findViewById(R.id.text_group_name)
            holder.mChildCount = convertView.findViewById(R.id.text_child_count)
            holder.mGroupArrow = convertView.findViewById(R.id.text_group_arrow)
            convertView.tag = holder
        } else {
            holder = convertView.tag as GroupViewHolder
        }
        val group = getGroup(groupPosition) as Group
        holder.mGroupName!!.text=group.getName()
        holder.mChildCount!!.text= group.getUsers()?.size.toString()
        if (isExpanded) {
            holder.mGroupArrow!!.text = "▾"
        } else {
            holder.mGroupArrow!!.text = "▸"
        }
        return convertView
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val holder: ChildViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.child_item_layout, null)
            holder = ChildViewHolder()
            holder.mChildName = convertView!!.findViewById(R.id.text_child_name)
            holder.circleImageView = convertView.findViewById(R.id.img)
            holder.mChildDescribe = convertView.findViewById(R.id.text_child_describe)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ChildViewHolder
        }
        val user = getChild(groupPosition, childPosition)
        holder.mChildName!!.text=user.getName()
        holder.mChildDescribe!!.text=user.getDescribe()
        val uri = SocketConstant.HOST_NAME + "/" + user.getImg()
        LoadImagesTask(holder.circleImageView).execute(uri)
        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    private class GroupViewHolder {
         lateinit var mGroupName: TextView
         lateinit var mChildCount: TextView
         lateinit var mGroupArrow: TextView
    }

    private class ChildViewHolder {
         lateinit var mChildName: TextView
         lateinit var mChildDescribe: TextView
         lateinit var circleImageView: CircleImageView
    }
}