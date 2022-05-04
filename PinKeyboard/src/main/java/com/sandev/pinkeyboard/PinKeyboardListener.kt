package com.sandev.pinkeyboard

interface PinKeyboardListener {
    fun onPinKeyClicked(pinNumber: Int)
    fun onLeftButtonClicked()
    fun onRightButtonClicked()
}