package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import ru.skillbranch.devintensive.R
import android.graphics.*
import android.graphics.RectF
import androidx.annotation.DrawableRes
import android.graphics.drawable.Drawable
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.annotation.Dimension.DP
import androidx.appcompat.widget.AppCompatImageView


class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleArttr:Int = 0
) : AppCompatImageView(context, attrs, defStyleArttr) {

    companion object {
        //private val SCALE_TYPE = ScaleType.CENTER_CROP
        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private const val COLOR_DRAWABLE_DIMENSION = 2
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2
        //private const val DEFAULT_CIRCLE_BACKGROUND_COLOR = Color.TRANSPARENT

        class InitialsDrawable(
            private val initials: String,
            private val color: Int
        ) : Drawable() {

            private val paint = Paint()

            init {
                val size = 112
                setBounds(0, 0, size.toInt(), size.toInt())

                paint.textSize = 48f
                paint.isAntiAlias = true
                paint.isFakeBoldText = true
                paint.setShadowLayer(6f, 0f, 0f, Color.BLACK)
                paint.style = Paint.Style.FILL
                paint.textAlign = Paint.Align.CENTER
            }

            override fun draw(canvas: Canvas) {
                paint.color = this.color
                canvas.drawRect(
                    0f,
                    0f,
                    bounds.width().toFloat(),
                    bounds.height().toFloat(),
                    paint
                )

                paint.color = Color.WHITE
                canvas.drawText(
                    initials,
                    0,
                    initials.length,
                    bounds.centerX().toFloat(),
                    bounds.centerY().toFloat() - paint.ascent() / 2,
                    paint
                )
            }

            override fun setAlpha(alpha: Int) {
                paint.alpha = alpha
            }

            override fun getOpacity(): Int {
                return PixelFormat.TRANSLUCENT
            }

            override fun setColorFilter(colorFilter: ColorFilter?) {
                paint.colorFilter = colorFilter
            }
        }
    }
    private var mBorderColor = DEFAULT_BORDER_COLOR
    private var mBorderWidth = DEFAULT_BORDER_WIDTH

    private val mShaderMatrix = Matrix()
    private val mBitmapPaint = Paint()
    private val mBorderPaint = Paint()

    private var mBitmap : Bitmap? = null
    private var mBitmapShader : BitmapShader? = null

    private val mDrawableRect = RectF()
    private val mBorderRect = RectF()

    private var mDrawableRadius: Float = 0.0f
    private var mBorderRadius: Float = 0.0f

    private var mBitmapWidth: Int = 0
    private var mBitmapHeight: Int = 0

    private var mReady: Boolean = false
    private var mSetupPending: Boolean = false
    //private var mBorderOverlay = false

    private var mCircleBackgroundColor = Color.BLACK
    private val mCircleBackgroundPaint = Paint()

    private var mColorFilter: ColorFilter? = null

    init{
        if (attrs!=null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView/*, defStyle, 0*/)
            mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH)
            mBorderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)

            a.recycle()

            //super.setScaleType(SCALE_TYPE)
            //outlineProvider = OutlineProvider()
        }
        init()
    }

    private fun init() {
        mReady = true

        if (mSetupPending) {
            setup()
            mSetupPending = false
        }
    }

    @Dimension(unit = DP) fun getBorderWidth():Int{
        return mBorderWidth
    }

    fun setBorderWidth(@Dimension(unit = DP) borderWidth:Int) {
        if (borderWidth == mBorderWidth)
            return
        mBorderWidth = borderWidth
        setup()
    }

    fun getBorderColor():Int{
        return mBorderColor
    }

    fun setBorderColor(hex:String){
        setColor(Color.parseColor(hex))
    }

    fun setBorderColor(@ColorRes borderColor: Int){
        setColor(resources.getColor(borderColor, context.theme))
    }

    private fun setColor(color: Int) {
        if (color == mBorderColor)
            return
        mBorderColor = color
        mBorderPaint.color = mBorderColor
        invalidate()
    }

    /*override fun getScaleType() = SCALE_TYPE
    override fun setScaleType(scaleType: ScaleType) {
        if (scaleType != SCALE_TYPE) {
            throw IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType))
        }
    }
    override fun setAdjustViewBounds(adjustViewBounds: Boolean) {
        if (adjustViewBounds) {
            throw IllegalArgumentException("adjustViewBounds not supported.")
        }
    }*/

    override fun onDraw(canvas: Canvas) {
        if (mBitmap == null)
            return

        canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, mBitmapPaint)
        if (mBorderWidth > 0) {
            canvas.drawCircle(mBorderRect.centerX(), mBorderRect.centerY(), mBorderRadius, mBorderPaint)
        }
    }

    private fun setup() {
        if (!mReady) {
            mSetupPending = true
            return
        }

        if (width == 0 && height == 0)
            return

        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = mBorderColor
        val borderWidth = mBorderWidth * resources.displayMetrics.density
        mBorderPaint.strokeWidth = borderWidth

        mBorderRect.set(calculateBounds())
        mBorderRadius =
            Math.min((mBorderRect.height() - mBorderWidth) / 2.0f, (mBorderRect.width() - mBorderWidth) / 2.0f)

        mDrawableRect.set(mBorderRect)
        mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f)

        mCircleBackgroundPaint.style = Paint.Style.FILL
        mCircleBackgroundPaint.isAntiAlias = true
        mCircleBackgroundPaint.color = mCircleBackgroundColor

        if (mBitmap == null) {
            invalidate()
            return
        }

        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        mBitmapPaint.isAntiAlias = true
        mBitmapPaint.shader = mBitmapShader

        mBitmapHeight = mBitmap!!.height
        mBitmapWidth = mBitmap!!.width

        /*if (!isBorderOverlay && borderWidth > 0) {
            drawableRect.inset(borderWidth - 1.0f, borderWidth - 1.0f)
        }*/
        applyColorFilter()
        updateShaderMatrix()
        invalidate()
    }

    private fun calculateBounds(): RectF {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = Math.min(availableWidth, availableHeight)

        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f

        return RectF(left, top, left + sideLength, top + sideLength)
    }

    private fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f

        mShaderMatrix.set(null)

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / mBitmapHeight.toFloat()
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f
        } else {
            scale = mDrawableRect.width() / mBitmapWidth.toFloat()
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f
        }

        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate((dx + 0.5f).toInt() + mDrawableRect.left, (dy + 0.5f).toInt() + mDrawableRect.top)

        mBitmapShader!!.setLocalMatrix(mShaderMatrix)
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null)
            return null

        if (drawable is BitmapDrawable)
            return drawable.bitmap

        try {
            val bitmap: Bitmap

            if (drawable is ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG)
            } else {
                bitmap = Bitmap.createBitmap(drawable.bounds.width(), drawable.bounds.height(), BITMAP_CONFIG)
            }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    private fun initializeBitmap() {
        mBitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        initializeBitmap()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        initializeBitmap()
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        initializeBitmap()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        initializeBitmap()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setup()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        setup()
    }

    override fun setColorFilter(cf: ColorFilter) {
        if (cf === mColorFilter)
            return

        mColorFilter = cf
        applyColorFilter()
        invalidate()
    }

    override fun getColorFilter() = mColorFilter

    private fun applyColorFilter() {
        mBitmapPaint.colorFilter = colorFilter
    }

    /*@SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return inTouchableArea(event.x, event.y) && super.onTouchEvent(event)
    }
    private fun inTouchableArea(x: Float, y: Float): Boolean {
        val x2 = (x - borderRect.centerX()).toDouble().pow(2.0)
        val y2 = (y - borderRect.centerY()).toDouble().pow(2.0)
        return x2 + y2 <= borderRadius.toDouble().pow(2.0)
    }
    private inner class OutlineProvider : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            val bounds = Rect()
            borderRect.roundOut(bounds)
            outline.setRoundRect(bounds, bounds.width() / 2f)
        }
    }*/
}