package com.example.connected.messages

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.connected.R
import com.example.connected.base.BaseActivity
import com.example.connected.databinding.ActivityMessagesBinding

class MessagesActivity : BaseActivity() {
    private lateinit var messagesViewModel: MessagesViewModel
    private lateinit var binding: ActivityMessagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar(binding.toolbar, resources.getString(R.string.title_messages))
        initNavBar(binding.navBar, 1)

        messagesViewModel = ViewModelProvider(this)[MessagesViewModel::class.java]
    }
}