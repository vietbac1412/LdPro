package tamhoang.ldpro4.util.extension

import kotlin.experimental.and

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 04/06/2018.
 */

// https://www.programiz.com/kotlin-programming/examples/convert-byte-array-hexadecimal.
// https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java/9855338#9855338
fun ByteArray.toHex() : String {
    val hexArray = "0123456789ABCDEF".toCharArray()
    val hexChars = CharArray(this.size * 2)
    for (j in this.indices) {
        val v = (this[j] and 0xFF.toByte()).toInt()

        hexChars[j * 2] = hexArray[v ushr 4]
        hexChars[j * 2 + 1] = hexArray[v and 0x0F]
    }
    return String(hexChars)
}