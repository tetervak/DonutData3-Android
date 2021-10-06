package ca.tetervak.donutdata3.ui.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("donutImage")
fun bindDonutImage(imageView: ImageView, fileName: String?){
    if(fileName is String){
        val filePath = "images/donuts/$fileName"
        imageView.context.assets.open(filePath).use{
            val drawable = Drawable.createFromStream(it,null)
            imageView.setImageDrawable(drawable)
        }
    }
}