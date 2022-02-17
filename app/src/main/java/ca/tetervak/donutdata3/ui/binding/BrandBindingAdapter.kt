package ca.tetervak.donutdata3.ui.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import ca.tetervak.donutdata3.R
import ca.tetervak.donutdata3.model.Brand

@BindingAdapter("brandName")
fun bindBrandName(textView: TextView, brand: Brand?){
    if(brand is Brand){
       val brandNames = textView.resources.getStringArray(R.array.donut_brand_name)
       textView.text = brandNames[brand.ordinal]
    }
}
