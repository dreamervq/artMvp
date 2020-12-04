package com.example.mylibrary.listeners

import androidx.fragment.app.Fragment

interface ITabContent {
    fun getContent(pos: Int): Fragment?
}