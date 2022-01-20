package com.example.connected.messages

import android.os.Bundle
import com.example.connected.R
import com.example.connected.base.BaseActivity
import com.example.connected.databinding.ActivityMessagesBinding
import org.koin.android.ext.android.inject

class MessagesActivity : BaseActivity() {
    private val messagesViewModel: MessagesViewModel by inject()
    private lateinit var activityMessagesBinding: ActivityMessagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMessagesBinding = ActivityMessagesBinding.inflate(layoutInflater)
        setContentView(activityMessagesBinding.root)

        initToolbar(activityMessagesBinding.toolbar, resources.getString(R.string.title_messages))
        initNavBar(activityMessagesBinding.navBar, 1)

    }
}