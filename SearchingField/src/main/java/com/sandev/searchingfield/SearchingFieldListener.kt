package com.sandev.searchingfield

interface SearchingFieldListener {
    fun onTextChanged(currentText: String, previousText: String)
    fun onClosePressed()
    fun onClearPressed()
}