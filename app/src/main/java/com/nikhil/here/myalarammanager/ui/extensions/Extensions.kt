package com.nikhil.here.myalarammanager.ui.extensions

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: String, length: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, length).show()