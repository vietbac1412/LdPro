package tamhoang.ldpro4.util.extension

import java.lang.Exception
import java.util.*

const val KHONG = "không"
const val MOT = "một"
const val HAI = "hai"
const val BA = "ba"
const val BON = "bốn"
const val NAM = "năm"
const val SAU = "sáu"
const val BAY = "bảy"
const val TAM = "tám"
const val CHIN = "chín"
const val LAM = "lăm"
const val LE = "lẻ"
const val MUOI = "mươi"
const val MUOIF = "mười"
const val MOTS = "mốt"
const val TRAM = "trăm"
const val NGHIN = "nghìn"
const val TRIEU = "triệu"
const val TY = "tỷ"
const val VND = "đồng"

val number = arrayOf(
    KHONG, MOT, HAI, BA,
    BON, NAM, SAU, BAY, TAM, CHIN
)

fun Double.convertToVnWords(isVnd: Boolean = false): String {
    val kq = ArrayList<String>()
    val originString = this.toInt().toString()

    //Cắt thành các chuỗi nhỏ 3 chử số
    val List_Num: ArrayList<String> = Split(originString, 3)
    while (List_Num.size !== 0) {
        //Xét 3 số đầu tiên của chuổi (số đầu tiên của List_Num)
        when (List_Num.size % 3) {
            1 -> kq.addAll(read_3num(List_Num[0]))
            2 -> {
                val nghin: ArrayList<String> = read_3num(List_Num[0])
                if (!nghin.isEmpty()) {
                    kq.addAll(nghin)
                    kq.add(NGHIN)
                }
            }
            0 -> {
                val trieu: ArrayList<String> = read_3num(List_Num[0])
                if (!trieu.isEmpty()) {
                    kq.addAll(trieu)
                    kq.add(TRIEU)
                }
            }
        }

        //Xét nếu 3 số đó thuộc hàng tỷ
        if (List_Num.size === List_Num.size / 3 * 3 + 1 && List_Num.size !== 1) kq.add(TY)

        //Xóa 3 số đầu tiên để tiếp tục 3 số kế
        List_Num.removeAt(0)
    }
    var kqString = ""
    kq.forEach { kqString += "$it " }
    return kqString.capitalize() + if (isVnd) VND else ""
}

fun read_3num(a: String): ArrayList<String> {
    val kq = ArrayList<String>()
    var num = -1
    try {
        num = a.toInt()
    } catch (ex: Exception) {
    }
    if (num == 0) return kq
    var hang_tram = -1
    try {
        hang_tram = a.substring(0, 1).toInt()
    } catch (ex: Exception) {
    }
    var hang_chuc = -1
    try {
        hang_chuc = a.substring(1, 2).toInt()
    } catch (ex: Exception) {
    }
    var hang_dv = -1
    try {
        hang_dv = a.substring(2, 3).toInt()
    } catch (ex: Exception) {
    }

    if (hang_tram != -1) {
        kq.add(number.get(hang_tram))
        kq.add(TRAM)
    }
    when (hang_chuc) {
        -1 -> {
        }
        1 -> kq.add(MUOIF)
        0 -> if (hang_dv != 0) kq.add(LE)
        else -> {
            kq.add(number.get(hang_chuc))
            kq.add(MUOI)
        }
    }
    when (hang_dv) {
        -1 -> {
        }
        1 -> if (hang_chuc != 0 && hang_chuc != 1 && hang_chuc != -1) kq.add(MOTS) else kq.add(
            number.get(hang_dv)
        )
        5 -> if (hang_chuc != 0 && hang_chuc != -1) kq.add(LAM) else kq.add(number.get(hang_dv))
        0 -> if (kq.isEmpty()) kq.add(number.get(hang_dv))
        else -> kq.add(number.get(hang_dv))
    }
    return kq
}

fun Split(str: String, chunkSize: Int): ArrayList<String> {
    var str = str
    val du = str.length % chunkSize
    //Nếu độ dài chuỗi không phải bội số của chunkSize thì thêm # vào trước cho đủ.
    if (du != 0) for (i in 0 until chunkSize - du) str = "#$str"
    return splitStringEvery(str, chunkSize)
}

//Cắt ra thành chuỗi nhỏ
fun splitStringEvery(s: String, interval: Int): ArrayList<String> {
    val arrList = ArrayList<String>()
    val arrayLength = Math.ceil(s.length / interval.toDouble()).toInt()
    val result = arrayOfNulls<String>(arrayLength)
    var j = 0
    val lastIndex = result.size - 1
    for (i in 0 until lastIndex) {
        result[i] = s.substring(j, j + interval)
        j += interval
    }
    result[lastIndex] = s.substring(j)

    arrList.addAll(result.filterNotNull())
    return arrList
}