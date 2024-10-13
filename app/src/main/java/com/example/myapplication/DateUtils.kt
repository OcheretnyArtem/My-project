package com.example.myapplication

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.format(formatter:SimpleDateFormat = defaultDateFormatter()): String = formatter.format(this)

fun defaultDateFormatter() = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())