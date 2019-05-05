package com.instant.message_app.activity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.instant.message_app.R
import com.instant.message_app.adapter.RecyclerViewAdapter
import com.instant.message_app.constants.SocketConstant
import com.instant.message_app.entity.ChatMessage
import com.instant.message_app.utils.JsonUtils
import com.instant.message_app.utils.SharedPreferenceHelper
import com.instant.message_app.websocket.WebSocketManager
import java.util.ArrayList

class ChatMessageActivity : AppCompatActivity(),WebSocketManager.ISocketListener{

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var message: EditText
    private lateinit var send: Button
    private val chatMessages = ArrayList<ChatMessage>()
    private var groupId: Int = 0
    private var userId:Int = 0
    private var currentId:Int = 0
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_message)

        //listView=findViewById(R.id.list_chat_message);
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        message = findViewById<EditText>(R.id.edit_chat_message)
        send = findViewById<Button>(R.id.button_send_message)

        val intent = getIntent()
        val bundle = intent.getExtras()
        groupId = bundle!!.getInt("groupId")
        userId = bundle!!.getInt("userId")
        currentId = SharedPreferenceHelper(this).getUserId()
        //adapter=new ChatMessageListAdapter(this,chatMessages);
        //listView.setAdapter(adapter);

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = linearLayoutManager

        adapter = RecyclerViewAdapter(this, chatMessages)
        recyclerView!!.adapter = adapter

        send!!.setOnClickListener {
            val msg = message!!.text.toString()

            val messageString =
                "{\"message\":\"" + msg + "\",\"currentUserId\":\"" + currentId + "_" + userId + "\",\"toUserId\":\"" + userId + "_" + currentId + "\"}"
            message!!.setText("")
            hideInput()
            WebSocketManager.getInstance().sendText(messageString)
        }

        WebSocketManager.getInstance()
            .connect(SocketConstant.CHAT_MESSAGE_ADDRESS + currentId + "_" + userId + "/" + currentId, this)
    }

     override fun onDestroy() {
        super.onDestroy()
        WebSocketManager.getInstance().disconnect()
    }


    override fun success(message: String) {
        println(message)

    }

    override fun error(message: String) {
        println(message)

    }

    override fun textMessage(s: String) {

        val chatMessage = JsonUtils.getChatMessage(s)
        chatMessages.add(chatMessage)

        try {
            // 新启动一个子线程
            Thread(Runnable {
                handler.post {
                    adapter!!.notifyDataSetChanged()
                    recyclerView!!.scrollToPosition(chatMessages.size - 1)
                    sendNotification(chatMessage.getUser()?.getName(), chatMessage?.getMessage())
                }
            }).start()
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun sendNotification(name: String?, message: String?) {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        var notification: Notification? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(name, name, NotificationManager.IMPORTANCE_LOW)

            notificationManager!!.createNotificationChannel(mChannel)
            notification = Notification.Builder(this, "1")
                .setChannelId(name)
                .setContentTitle(name)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.sym_action_email).build()
        } else {
            val notificationBuilder = NotificationCompat.Builder(this, "1")
                .setContentTitle(name)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.sym_action_email)
                .setOngoing(true)
            notification = notificationBuilder.build()
        }
        notificationManager!!.notify(111111, notification)


        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val rt = RingtoneManager.getRingtone(getApplicationContext(), uri)
        rt.play()

    }


    protected fun hideInput() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        val v = getWindow().peekDecorView()
        if (null != v) {
            imm!!.hideSoftInputFromWindow(v!!.getWindowToken(), 0)
        }
    }
}