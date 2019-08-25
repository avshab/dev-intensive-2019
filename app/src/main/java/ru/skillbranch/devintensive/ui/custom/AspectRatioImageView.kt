package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import ru.skillbranch.devintensive.R

class AspectRatioImageView @JvmOverloads constructor(
    context:Context,
    attrs:AttributeSet? = null,
    defStyleArttr:Int = 0
) : ImageView(context, attrs, defStyleArttr) {
    companion object {
        private const val DEFAULT_ASPECT_RATION = 1.78f
    }

    private var aspectRation = DEFAULT_ASPECT_RATION

    init{
        if(attrs!=null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView)
            aspectRation = a.getFloat(R.styleable.AspectRatioImageView_aspectRatio, DEFAULT_ASPECT_RATION)
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val newHeight = (measuredWidth/aspectRation).toInt()
        setMeasuredDimension(measuredWidth, newHeight)

    }
}