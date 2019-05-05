package com.instant.message_app.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.instant.message_app.R
import com.instant.message_app.utils.SharedPreferenceHelper
import com.instant.message_app.zxing.android.CaptureActivity

class HomeFragment : Fragment() {

    private lateinit var listener: LogoutListener
    private lateinit var helper: SharedPreferenceHelper


    companion object {
        fun newInstance(): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            fragment.arguments=args
            return fragment
        }
        private const val REQUEST_CODE_SCAN = 0x0000
        private const val DECODED_CONTENT_KEY = "codedContent"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = context as LogoutListener
        helper = SharedPreferenceHelper(context)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_home, container, false)
        val name = view.findViewById<TextView>(R.id.text_name)
        val signature = view.findViewById<TextView>(R.id.text_signature)
        val logout = view.findViewById<LinearLayout>(R.id.linear_logout)
        val sweepCode = view.findViewById<LinearLayout>(R.id.linear_sweep_code)

        name.text=(helper.getUserName())
        signature.text=(helper.getUserSignature())

        sweepCode.setOnClickListener {
            val intent = Intent(
                context,
                CaptureActivity::class.java
            )
            startActivityForResult(intent, REQUEST_CODE_SCAN)
        }
        logout.setOnClickListener { v -> listener.logout() }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                val content = data.getStringExtra(DECODED_CONTENT_KEY)
                listener.resultContent(content)
            }
        }
    }

    interface LogoutListener {
        fun logout()
        fun resultContent(content: String)
    }
}