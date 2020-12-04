package com.example.mylibrary.listeners

import android.os.Message

interface IWeakHandler {
    fun handleMessage(msg: Message?)
}