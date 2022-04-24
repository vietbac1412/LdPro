package tamhoang.ldpro4.util.extension

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.TextAppearanceSpan
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import tamhoang.ldpro4.R
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.math.pow

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 06/03/2018.
 */
fun ImageView.loadImage(url: String) {
    Glide.with(context)
            .load(url)
            .placeholder(R.color.white)
            .error(R.color.white)
            .centerCrop()
            .into(this)
}

fun ImageView.loadImage(url: String, drawableError: Drawable) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Glide.with(context)
                .load(url)
                .placeholder(drawableError)
                .error(drawableError)
                .centerCrop()
                .into(this)
    } else {
        Glide.with(context)
                .load(url)
                .placeholder(R.color.white)
                .error(R.color.white)
                .centerCrop()
                .into(this)
    }
}

fun ImageView.loadImage(url: String, drawableError: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Glide.with(context)
                .load(url)
                .placeholder(drawableError)
                .error(drawableError)
                .centerCrop()
                .into(this)
    } else {
        Glide.with(context)
                .load(url)
                .placeholder(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) drawableError else R.color.white)
                .error(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) drawableError else R.color.white)
                .centerCrop()
                .into(this)
    }
}

fun ImageView.loadImageSession(url: String) {
    Glide.with(context)
            .load(url)
//            .placeholder(R.drawable.logo_365_long)
//            .error(R.drawable.logo_365_long)
            .centerCrop()
            .into(this)
}

fun ImageView.loadImageBannerVNPay(url: String) {
    Glide.with(context)
            .load(url)
//            .placeholder(R.drawable.logo_365_long)
//            .error(R.drawable.logo_365_long)
            .fitCenter()
            .into(this)
}

fun ImageView.setBlackAndWhiteFilter() {
    val brightness = 10f // change values to suite your need
    val colorMatrix = floatArrayOf(
            0.33f, 0.33f, 0.33f, 0f, brightness,
            0.33f, 0.33f, 0.33f, 0f, brightness,
            0.33f, 0.33f, 0.33f, 0f, brightness, 0f, 0f, 0f, 1f, 0f)
    val colorFilter: ColorFilter = ColorMatrixColorFilter(colorMatrix)
    this.colorFilter = colorFilter
}

fun Bitmap.toStringBase64(): String {
    // Convert bitmap to Base64 encoded image for web
    try {
        val byteArrayOutputStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val imageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
        return "data:image/png;base64,$imageBase64"
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun View.getBitmapFromView(): Bitmap? {
    try {
        val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        this.layout(this.left, this.top, this.right, this.bottom)
        this.draw(canvas)

        return bitmap
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun createDrawable(context: Context, id: Int): Drawable? {
    val version = Build.VERSION.SDK_INT
    return if (version >= 21) {
        return ContextCompat.getDrawable(context, id)
    } else {
        return context.resources.getDrawable(id)
    }
}

//can not use for property android:textAllCaps="true" with low Api

fun TextView.setHighlight(context: Context, searchText: String?, latinSearch: Boolean = false, textColor: Int = R.color.white, backgroundColor: Int = R.color.red_tomato) {
    if (searchText != null && searchText.isNotEmpty() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val fullText = this.text.toString()
        val startPos: Int = if (latinSearch)
            try {
                fullText.toLowerCase(Locale.US).convertUTF8ToLatin().indexOf(searchText.toLowerCase(Locale.US).convertUTF8ToLatin())
            } catch (e: java.lang.Exception){
                fullText.toLowerCase(Locale.US).indexOf(searchText.toLowerCase(Locale.US))
            }
        else
            fullText.toLowerCase(Locale.US).indexOf(searchText.toLowerCase(Locale.US))

        val endPos: Int = startPos + searchText.length
        if (startPos >= 0 && endPos <= this.length()) {
            val spannable: Spannable = SpannableString(fullText)
            val textColorState = ColorStateList(arrayOf(intArrayOf()), intArrayOf(context.getColor(textColor)))
            val textSpan = TextAppearanceSpan(null, Typeface.BOLD, -1, textColorState, null)
            val backgroundSpan = BackgroundColorSpan(context.getColor(backgroundColor))
            spannable.setSpan(backgroundSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(textSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            this.text = spannable
        }
    }
}

//chinh do tuong phan anh bitmap
fun Bitmap.createContract(contrast : Double) : Bitmap {
    val bitmapOut = Bitmap.createBitmap(this.width, this.height, this.config)
    val valueContrast = ((100 + contrast) / 100.0).pow(2.0)

    for (x in 0 until width) {
        for (y in 0 until height) {
            val pixel = this.getPixel(x, y)
            val A = Color.alpha(pixel)
            var R = Color.red(pixel)
            R = (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0).toInt()
            if (R < 0) R = 0
            else if (R > 255) R = 255

            var G = Color.green(pixel)
            G = (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0).toInt()
            if (G < 0) G = 0
            else if (G > 255) G = 255

            var B = Color.blue(pixel)
            B = (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0).toInt()
            if (B < 0) B = 0
            else if (B > 255) B = 255

            bitmapOut.setPixel(x, y, Color.argb(A, R, G, B))
        }
    }
    return bitmapOut
}