package com.instant.message_app.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.instant.message_app.R
import com.instant.message_app.constants.SocketConstant
import com.instant.message_app.entity.GroupChat
import com.instant.message_app.entity.ScanCode
import com.instant.message_app.fragments.ContactsFragment
import com.instant.message_app.fragments.GroupChatFragment
import com.instant.message_app.fragments.HomeFragment
import com.instant.message_app.utils.DisplayUtil
import com.instant.message_app.utils.SharedPreferenceHelper
import com.instant.message_app.websocket.WebSocketManager
import org.apache.commons.codec.binary.Base64
import java.util.*
import kotlin.collections.ArrayList

//TODO: onLayout time too long
class MainActivity : AppCompatActivity(), ContactsFragment.ContactsListener,HomeFragment.LogoutListener, GroupChatFragment.OnItemClickListener, WebSocketManager.ISocketListener {

    private lateinit var viewPager: ViewPager
    private lateinit var groupChat: TextView
    private lateinit var contacts:TextView
    private lateinit var home:TextView
    private lateinit var textViews: MutableList<TextView>
    private lateinit var helper: SharedPreferenceHelper
    companion object {
        private  const val TAG = "MainActivity"
    }
    private lateinit var mContext: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this
        helper = SharedPreferenceHelper(this)

        viewPager = findViewById(R.id.viewpager)
        groupChat = findViewById(R.id.text_group_chat)
        contacts = findViewById(R.id.text_contacts)
        home = findViewById(R.id.text_home)
        textViews = ArrayList()
        textViews.add(groupChat)
        textViews.add(contacts)
        textViews.add(home)

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.offscreenPageLimit=3
        viewPager.currentItem = 1
        select(1)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(i: Int) {
                selectNone()
                select(i)
            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })
        bindClick()
    }

    private fun bindClick() {
        for (i in textViews.indices) {
            textViews[i].setOnClickListener {
                val current = viewPager.currentItem
                if (current != i) {
                    viewPager.currentItem = i
                }
            }
        }
    }

    private fun selectNone() {
        for (t in textViews) {
            t.setTextColor(this.resources.getColor(R.color.textSelectNone))
        }
    }

    private fun select(index: Int) {
        val textView = textViews[index]
        textView.setTextColor(this.resources.getColor(R.color.textSelect))
    }

    override fun toMessage(groupId: Int, userId: Int) {
        val intent = Intent(this, ChatMessageActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("groupId", groupId)
        bundle.putInt("userId", userId)
        intent.putExtras(bundle)
        startActivity(intent)


    }

    override fun logout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("提示")
        builder.setMessage("确定退出吗？")
        builder.setPositiveButton("确定") { dialog, which ->
            val alertDialog = builder.show()
            alertDialog.dismiss()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            helper.clear()
            finish()
        }
        builder.setNegativeButton("取消") { dialog, which ->
            val alertDialog = builder.show()
            alertDialog.dismiss()
        }
        builder.show()
    }

    private lateinit var dialog: AlertDialog
    private fun showDialog(content: String) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_scan_code_confirm_layout, null)
        val builder = AlertDialog.Builder(this, R.style.DialogTheme)

        val heightPixels = DisplayUtil.getHeight(this)

        val textView = view.findViewById<TextView>(R.id.text_login_message)
        val ok = view.findViewById<Button>(R.id.button_ok)
        val cancel = view.findViewById<Button>(R.id.button_cancel)
        ok.setOnClickListener {
            val code = UUID.randomUUID().toString()
            val scanCode = ScanCode()
            scanCode.setCode(content)
            val id = helper.getUserId()
            scanCode.setId(id)
            val json = JSON.toJSONString(scanCode)

            val encodeString = encode(json)
            Log.i(TAG, encodeString)
            WebSocketManager.getInstance()
                .connect(SocketConstant.SCAN_CODE_ADDRESS + code + "/" + encodeString, mContext)
            dialog.dismiss()
        }
        cancel.setOnClickListener { v -> dialog.dismiss() }
        textView.minHeight = heightPixels
        builder.setView(view)

        dialog = builder.create()
        val window = dialog.window
        window!!.setGravity(Gravity.BOTTOM)
        window.setWindowAnimations(R.style.dialog_animation)
        window.decorView.setPadding(0, 0, 0, 0)
        val lp = window.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = lp

        dialog.show()
    }

    private fun encode(code: String): String {
        var b = code.toByteArray()
        val base64 = Base64()
        b = base64.encode(b)
        return String(b)
    }

    //TODO:扫码成功之后，连接socket,弹出登录界面
    override fun resultContent(content: String) {

        showDialog(content)

    }

    override fun onDestroy() {
        super.onDestroy()
        WebSocketManager.getInstance().disconnect()
    }


    override fun onGroupChatItemClick(groupChat: GroupChat) {
        val intent = Intent(this, GroupChatsMessageActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("groupId", groupChat.getId())
        intent.putExtras(bundle)
        startActivity(intent)
    }


    override fun success(message: String) {
        Log.i(TAG, message)
        //连接成功之后关闭
        WebSocketManager.getInstance().disconnect()
    }

    override fun error(message: String) {
        Log.i(TAG, message)

    }

    override fun textMessage(message: String) {
        Log.i(TAG, message)

    }


     class ViewPagerAdapter: FragmentStatePagerAdapter{

         companion object {
             val groupChatFragment=GroupChatFragment.newInstance()
             val contactsFragment=ContactsFragment.newInstance()
             val homeFragment=HomeFragment.newInstance()
         }

         constructor(fm: FragmentManager) : super(fm)

         override fun getItem(i: Int): Fragment {
             when(i){
                 0->return groupChatFragment
                 1->return contactsFragment
             }
             return homeFragment
         }

         override fun getCount(): Int {
             return 3
         }

         override fun destroyItem(container: View?, position: Int, `object`: Any?) {
             //super.destroyItem(container, position, `object`)
         }
     }


















}