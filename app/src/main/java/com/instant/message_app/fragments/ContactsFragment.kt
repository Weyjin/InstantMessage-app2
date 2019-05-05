package com.instant.message_app.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Toast
import com.instant.message_app.R
import com.instant.message_app.adapter.ExpandableListAdapter
import com.instant.message_app.entity.Group
import com.instant.message_app.utils.HttpUtils
import com.instant.message_app.utils.JsonUtils

class ContactsFragment : Fragment() {

    private lateinit var adapter: ExpandableListAdapter
    private lateinit var expandableListView: ExpandableListView
    private lateinit var listener: ContactsListener



    companion object {
        fun newInstance(): ContactsFragment {
            val fragment = ContactsFragment()
            val args = Bundle()

            fragment.arguments=args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = context as ContactsListener

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater?.inflate(R.layout.fragment_contacts, container, false)

            expandableListView = view?.findViewById(R.id.expandable_list)!!


        expandableListView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val group = adapter.getGroup(groupPosition) as Group
            val result = adapter.getChild(groupPosition, childPosition)
            listener.toMessage(group.getId(), result.getId())

            true
        }
        MyTask().execute()
        return view
    }

    private inner class MyTask : AsyncTask<String, Int, String>() {
        //执行后台任务前做一些UI操作
        override fun onPreExecute() {

        }

        //执行后台任务（耗时操作）,不可在此方法内修改UI
        override fun doInBackground(vararg params: String): String? {
            try {
                return HttpUtils.getGroups()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        //执行完后台任务后更新UI
        override fun onPostExecute(result: String?) {
            println(result)
            if (result == null) {
                Toast.makeText(context, "服务器出现异常", Toast.LENGTH_SHORT).show()
            } else {
                val groups = JsonUtils.getGroups(result)
                adapter = ExpandableListAdapter(context, groups)
                expandableListView.setAdapter(adapter)
            }


        }

        //取消执行中的任务时更改UI
        override fun onCancelled() {

        }
    }

    interface ContactsListener {
        fun toMessage(groupId: Int, userId: Int)
    }

}