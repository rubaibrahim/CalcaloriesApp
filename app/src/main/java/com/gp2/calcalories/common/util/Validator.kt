package com.gp2.calcalories.common.util

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

class Validator {

    companion object {
        @Volatile
        private var INSTANCE: Validator? = null
        fun getInstance(): Validator {
            return INSTANCE ?: synchronized(    this) {
                val instance = Validator()
                INSTANCE = instance
                return instance
            }
        }
    }

    fun isEmpty(editText: EditText, error: String?): Boolean {
        if (editText.text.toString().trim { it <= ' ' }.isEmpty()) {
            editText.error = error
            editText.requestFocus()
            return true
        }
        return false
    }
    fun isEmpty(editText: TextInputLayout, error: String?): Boolean {
        // Clear error text
        editText.error = null
        if (editText.editText?.text.toString().trim { it <= ' ' }.isEmpty()) {
            editText.error = error
            editText.requestFocus()
            return true
        }
        return false
    }

    var emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+"
    fun emailValid(editText: TextInputLayout, error: String?): Boolean {
        editText.error = null
        if (editText.editText?.text.toString().trim { it <= ' ' }.isNotEmpty()
            && !editText.editText?.text.toString().trim { it <= ' ' }.matches(Regex(emailPattern))
        ) {
            editText.error = error
            editText.requestFocus()
            return true
        }
        return false
    }

    fun hasError(editText: EditText?, field: String, jsonObject: Map<String, Any>?): Boolean {
        if (editText != null && jsonObject!!.containsKey(field)) {
            editText.error = jsonObject[field].toString() + ""
            editText.requestFocus()
            return true
        }
        return false
    }
    fun hasError(editText: TextInputLayout?, field: String, jsonObject: Map<String, Any>?): Boolean {
        editText?.error = null
        if (editText != null && jsonObject!!.containsKey(field)) {
            editText.error = jsonObject[field].toString().replace("[","").replace("]","") + ""
            editText.requestFocus()
            return true
        }
        return false
    }
}