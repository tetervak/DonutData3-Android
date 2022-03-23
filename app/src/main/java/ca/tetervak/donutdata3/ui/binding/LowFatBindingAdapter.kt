package ca.tetervak.donutdata3.ui.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import ca.tetervak.donutdata3.R

@BindingAdapter("lowFatValue")
fun bindLowFatValue(textView: TextView, value: Boolean?){
    if (value is Boolean) {
        val resources = textView.resources
        textView.text =
            if (value) {
                resources.getString(R.string.low_fat)
            } else {
                resources.getString(R.string.not_low_fat)
            }
    }
}