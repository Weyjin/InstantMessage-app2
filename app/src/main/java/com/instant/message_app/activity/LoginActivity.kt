package com.instant.message_app.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.instant.message_app.R
import com.instant.message_app.utils.HttpUtils
import com.instant.message_app.utils.JsonUtils
import com.instant.message_app.utils.SharedPreferenceHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var password:EditText
    private lateinit var login: Button
    private lateinit var helper: SharedPreferenceHelper
    val mContext by lazy { this }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        name = findViewById(R.id.edit_name)
        password = findViewById(R.id.edit_password)
        login = findViewById(R.id.button_login)

        helper = SharedPreferenceHelper(this@LoginActivity)

        login.setOnClickListener {
            val nameText = name.text.toString()
            val passwordText = password.text.toString()
            MyTask().execute(nameText, passwordText)
        }
    }

   inner class MyTask : AsyncTask<String, Int, String>() {

        private lateinit var pDialog: ProgressDialog
        //执行后台任务前做一些UI操作
        override fun onPreExecute() {
            pDialog = ProgressDialog(mContext)

            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            pDialog.setMessage("正在登录……")
            pDialog.setCanceledOnTouchOutside(false)
            pDialog.show()

        }
        //执行后台任务（耗时操作）,不可在此方法内修改UI
        override fun doInBackground(vararg params: String): String? {
            try {
                return HttpUtils.login(params[0], params[1])
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        //执行完后台任务后更新UI
        override fun onPostExecute(result: String?) {
            pDialog.dismiss()
            println(result)
            if (result == null) {
                Toast.makeText(this@LoginActivity, "服务器出现异常", Toast.LENGTH_SHORT).show()
            } else {
                val loginResult = JsonUtils.getLoginResult(result)
                if (loginResult.getMsg() == "success") {

                    helper.saveUserId(loginResult.getUserId())
                    helper.saveUserName(loginResult.getUserName().toString())
                    helper.saveUserSignature(loginResult.getSignature().toString())
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "登录失败", Toast.LENGTH_SHORT).show()
                }
            }


        }

        //取消执行中的任务时更改UI
        override fun onCancelled() {

        }
    }


}