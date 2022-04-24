package tamhoang.ldpro4.util.extension

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 18/07/2018.
 */
inline fun <reified T> Gson.fromJson(json: String) =
        this.fromJson<T>(json, object : TypeToken<T>() {}.type)

inline fun <reified T> String.checkValueMoney() =
        if(this.isNullOrEmpty()) 0.0 else this.convertTextDecimalToDouble()

inline fun <reified T> String.checkValueInt() =
        if(this.isNullOrEmpty()) 0 else this.convertTextNumberToInt()

fun ClosedRange<Int>.random() =
        Random().nextInt((endInclusive + 1) - start) +  start
