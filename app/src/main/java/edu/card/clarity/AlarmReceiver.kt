package edu.card.clarity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // function called when alarm is triggered
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        println("Alarm Triggered: $message")
    }
}