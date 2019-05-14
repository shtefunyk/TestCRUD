package com.bshtef.testcrud.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText

fun EditText.onTextChanged(onTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            onTextChanged.invoke(p0.toString())
        }
        override fun afterTextChanged(editable: Editable?) {
        }
    })
}

fun View.toVisible(){
    this.visibility = View.VISIBLE
}

fun View.toGone(){
    this.visibility = View.GONE
}

