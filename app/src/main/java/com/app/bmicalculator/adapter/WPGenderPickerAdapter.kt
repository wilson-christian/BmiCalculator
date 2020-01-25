package com.app.bmicalculator.adapter

import com.super_rabbit.wheel_picker.WheelAdapter

class WPGenderPickerAdapter : WheelAdapter() {
    override fun getValue(position: Int): String {
        return when (position) {
            0 -> "Male"
            1 -> "Female"
            else -> ""
        }
    }

    override fun getPosition(vale: String): Int {
        return when (vale) {
            "Male" -> 0
            "Female" -> 1
            else -> 0
        }
    }

    override fun getTextWithMaximumLength(): String {
        return "Female"
    }
}