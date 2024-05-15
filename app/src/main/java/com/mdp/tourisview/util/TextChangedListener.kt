package com.mdp.tourisview.util

import android.text.Editable
import android.text.TextWatcher

abstract class TextChangedListener: TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable?) {}
}