package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import ru.skillbranch.devintensive.R

class AvatarImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleArttr:Int = 0
) : ImageView(context, attrs, defStyleArttr) {

    init{
        if(attrs!=null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView)
            a.recycle()
        }
    }
}