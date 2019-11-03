package ru.hotmule.beaconfinder.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.findbeacon.R
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("app:date")
fun setDate(textView: TextView, date: Date?) {
    val formatter = SimpleDateFormat("d MMMM yyyy, HH:mm:ss", Locale("ru"))
    date?.let { textView.text = formatter.format(date) }
}