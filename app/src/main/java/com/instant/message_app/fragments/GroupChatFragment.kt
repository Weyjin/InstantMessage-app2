package com.instant.message_app.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.instant.message_app.R
import com.instant.message_app.adapter.GroupChatRecyclerViewAdapter
import com.instant.message_app.entity.GroupChat
import com.instant.message_app.entity.Result
import com.instant.message_app.utils.HttpUtils
import com.instant.message_app.utils.JsonUtils
import com.instant.message_app.utils.SharedPreferenceHelper

class GroupChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GroupChatRecyclerViewAdapter
    private lateinit var helper: SharedPreferenceHelper
    private lateinit var listener: OnItemClickListener


    companion object {
        fun newInstance(): GroupChatFragment {
            val fragment = GroupChatFragment()
            val args = Bundle()
            fragment.arguments=args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        helper = SharedPreferenceHelper(context)
        listener = context as OnItemClickListener
        MyTask().execute()
    }

    override fun onCreateView(
        inflater: LayoutInflater?, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(R.layout.fragment_group_chat, container, false)
        if (view != null) {
            recyclerView = view.findViewById(R.id.recyclerView)
        }
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager

        return view
    }


    private inner class MyTask : AsyncTask<String, Int, String>() {
        //执行后台任务前做一些UI操作
        override fun onPreExecute() {

        }

        //执行后台任务（耗时操作）,不可在此方法内修改UI
        override fun doInBackground(vararg params: String): String? {
            val id = helper.getUserId()
            try {
                return HttpUtils.getGroupChats(id.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        //执行完后台任务后更新UI
        override fun onPostExecute(r: String?) {
            println(r)

            if (r == null) {
                Toast.makeText(context, "服务器出现异常", Toast.LENGTH_SHORT).show()
            } else {
                val groupChats = JsonUtils.getGroupChats(r)
                adapter = GroupChatRecyclerViewAdapter(context, groupChats)
                adapter.setOnItemClickListener(object : GroupChatRecyclerViewAdapter.OnItemClickListener {
                    override fun onClick(position: Int) {
                        val groupChat = groupChats[position]
                        listener.onGroupChatItemClick(groupChat)
                    }
                })
                recyclerView!!.adapter = adapter

            }


        }

        //取消执行中的任务时更改UI
        override fun onCancelled() {

        }
    }

    interface OnItemClickListener {
        fun onGroupChatItemClick(groupChat: GroupChat)
    }


}