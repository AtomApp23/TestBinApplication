package ru.atomapp.testbinapplication.main.presentaion

import android.widget.TextView

data class BinUi(private val id: String, private val info: String) {

    fun map(bin: TextView, infoBin: TextView) {
       // TODO fix info and UI screen
        bin.text = id
        infoBin.text = info
    }
}
