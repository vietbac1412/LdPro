package tamhoang.ldpro4.util.extension

import android.graphics.*
import android.text.TextPaint
import android.util.Base64
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 11/10/2018.
 */
fun String.MD5(): String {
    return try {
        val digest = MessageDigest.getInstance("MD5")
        val inputBytes = this.toByteArray()
        val hashBytes = digest.digest(inputBytes)

        byteArrayToHex(hashBytes)
    } catch (e: NoSuchAlgorithmException) {
        ""
    }
}

private fun byteArrayToHex(a: ByteArray): String {
    val sb = StringBuilder(a.size * 2)
    for (b in a) sb.append(String.format("%02x", b and (0xff.toByte())))

    return sb.toString()
}

fun String.isNetworkConnected(): Boolean {
    try {
        val process = java.lang.Runtime.getRuntime().exec("ping -c 1 $this")
        val returnVal = process.waitFor()
        return (returnVal == 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

fun String.generateBarcode(): Bitmap? {
//    val multiFormatWriter = MultiFormatWriter()
//    try {
//        val bitMatrix = multiFormatWriter.encode(this, BarcodeFormat.QR_CODE, 200, 200)
//        val barcodeEncoder = BarcodeEncoder()
//        return barcodeEncoder.createBitmap(bitMatrix)
//    } catch (e: WriterException) {
//        e.printStackTrace()
//    }
    return null
}

fun String.generateBarcode128(): Bitmap? {
//    val multiFormatWriter = MultiFormatWriter()
//    try {
//        val bitMatrix = multiFormatWriter.encode(this, BarcodeFormat.CODE_128, 200, 50)
//        val barcodeEncoder = BarcodeEncoder()
//        return barcodeEncoder.createBitmap(bitMatrix)
//    } catch (e: WriterException) {
//        e.printStackTrace()
//    }
    return null
}

fun String.generateBitmap(): Bitmap? {
    try {
        val pureBase64Encoded = this.substring(this.indexOf(",")  + 1)
        val decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun String.encodeBase64(): String {
    if (this.isNullOrEmpty()) return ""

    return try {
        val data = this.toByteArray(Charsets.UTF_8)
        Base64.encodeToString(data, Base64.DEFAULT)
    } catch (e: UnsupportedEncodingException) {
        ""
    }
}

fun String.decodeBase64(): String {
    if (this.isNullOrEmpty()) return ""

    return try {
        val data = Base64.decode(this, Base64.DEFAULT)
        String(data, Charsets.UTF_8)
    } catch (e: UnsupportedEncodingException) {
        ""
    }
}

fun String.isBase64(): Boolean {
    return this.matches(Regex("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)\$"))
}

fun String.acronym(): String {
    var output = ""
    this.trim().split(" ").forEach {
        if (it.isNotEmpty()) {
            output += "${it[0]}"
        }
    }
    return output
}

fun String.splitCalculation(): String {
    var output = ""
    var count  = 0
//    this.trim().split("").reversed().forEach {
//        if (it == Numbers.N0.value || it == Numbers.N1.value
//                || it == Numbers.N2.value || it == Numbers.N3.value
//                || it == Numbers.N4.value || it == Numbers.N5.value
//                || it == Numbers.N6.value || it == Numbers.N7.value
//                || it == Numbers.N8.value || it == Numbers.N9.value) {
//            output += if (count == 3) {
//                count = 1
//                ",$it"
//            } else {
//                count += 1
//                it
//            }
//        } else {
//            output += it
//            count = 0
//        }
//    }

    return output.reversed()
}

fun String.convertJsonObjectToString(key: String): String{
    var count = 1
    var newContent = this
    try {
        run lit@ {
            this.substringAfter("$key\":{").forEachIndexed { index, char ->
                if (char == '{') count++
                if (char == '}') count--
                if (count == 0) {
                    val newIndex = this.substringBefore("$key\":{").length + "$key\":{".length + index + 1
                    newContent = this.substring(0, newIndex - index - 1 ).replace("$key\":{", "$key\":\"{")
                    newContent += this.substring(newIndex - index - 1 , newIndex).replace("\"","'")
                    newContent += "\"" + this.substring(newIndex)
                    return@lit
                }
            }
        }
    } catch (e: Exception){}
    return newContent
}

//Draw text (if text's length > 16 characters) for lcd display
fun String.drawTextToBitmap() : Bitmap? {
    return try {
        val paint = TextPaint()
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK
        paint.textSize = 20f
        val topString = this.substringBefore("\n")
        val botString = this.substringAfter("\n") //Hien thi toi da 2 dong
        if (botString != "" ) {
            val stringLong = if (topString.length > botString.length) topString else botString
            val bound = Rect()
            paint.getTextBounds(topString, 0, topString.length, bound)
            val height = bound.height()
            val bitmap = Bitmap.createBitmap(
                paint.measureText(stringLong).toInt(),
                80,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)
            canvas.drawText(topString, 0f, height.toFloat(), paint)
            canvas.drawText(botString, 0f, 80f - height, paint)
            bitmap
        }
        else {
            val bitmap = Bitmap.createBitmap(
                paint.measureText(this).toInt(),
                40,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)
            canvas.drawText(topString, 0f, 20f, paint)
            bitmap
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        null
    }
}