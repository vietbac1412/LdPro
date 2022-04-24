package tamhoang.ldpro4.util.extension

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.text.Editable
import org.joda.time.Interval
import tamhoang.ldpro4.R
import tamhoang.ldpro4.data.constants.Constants.Companion.GMT
import java.net.URLDecoder
import java.text.*
import java.util.*
import java.util.regex.Pattern


/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 15/03/2018.
 */
fun String.capitalFirst(): String {
    return if (this.length > 1) this[0].toUpperCase() + this.substring(1).toLowerCase()
        else this
}

fun String.numberFormat(): String {
    return NumberFormat.getNumberInstance(Locale.US).format(this)
}

fun Int.numberFormat(): String {
    return NumberFormat.getNumberInstance(Locale.US).format(this)
}

fun Float.numberFormat(): String {
    return NumberFormat.getNumberInstance(Locale.US).format(this)
}

fun Double.numberFormat(): String {
    return NumberFormat.getNumberInstance(Locale.US).format(this)
}

fun Long.numberFormat(): String {
    return NumberFormat.getNumberInstance(Locale.US).format(this)
}

fun Double.roundUpThousand(): Double {
    return Math.ceil(this / 1000) * 1000
}

fun Double.roundUp(unit: Int): Double {
    return Math.ceil(this / unit) * unit
}

fun Double.roundUpUnit(isCurrencyVnd: Boolean) = if (isCurrencyVnd) this.roundUpUnitK() else this.roundUpPercent()

fun Double.roundUpUnitK(): Double {
    //return this
    //return Math.ceil(this / 100) * 100
    return Math.ceil(this / 1) * 1
}

fun Double.roundUpPercent(): Double {
    return Math.ceil(this / 0.001) * 0.001
}

fun Double.roundDownUnit(isCurrencyVnd: Boolean) = if (isCurrencyVnd) this.roundDownUnitK() else this.roundDownPercent()

fun Double.roundDownUnitK(): Double {
    //return this
    //return Math.floor(this / 100) * 100
    return Math.floor(this / 1) * 1
}

fun Double.roundDownPercent(): Double {
    return Math.floor(this / 0.001) * 0.001
}

fun Double.formatQuantity(): String {
    val nf = NumberFormat.getNumberInstance(Locale.US)
    val formatter = nf as DecimalFormat
    formatter.applyPattern("0.###")
    return formatter.format(this)
}

fun Double.formatQuantityNegative(): String {
    val nf = NumberFormat.getNumberInstance(Locale.US)
    val formatter = nf as DecimalFormat
    formatter.applyPattern("0.##;-0.##")
    return formatter.format(this)
}

fun Double.formatPriceTimeDescription(): String {
    val nf = NumberFormat.getNumberInstance(Locale.US)
    val formatter = nf as DecimalFormat
    formatter.applyPattern("###,###,###")
    return formatter.format(this)
}

fun String.convertTextDecimalToDouble() : Double {
    return try {
        if (this != ".") this.replace(",", "").toDouble() else 0.0
    } catch (e: Exception) {
        0.0
    }
}

fun String.convertTextNumberToInt() : Int {
    return try {
        this.replace(",", "").toInt()
    } catch (e: Exception) {
        0
    }
}

fun String.convertUTF8ToLatin() : String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(temp)
            .replaceAll("")
            .replace("đ", "d")
            .replace("Đ", "D")
            .toLowerCase()
}

fun String.getTextImage() : String {
    var output = ""
    val listString = this.split(" ")
    val size = listString.size
    output = if (size >= 2) {
        "${listString[0][0]}${listString[1][0]}"
    } else {
        if (listString[0].length >= 2) {
            "${listString[0][0]}${listString[0][1]}"
        } else {
            "${listString[0][0]}"
        }
    }
    return output
}

fun Color.getRandom(random: Random): Int {
    return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
}

fun Double.convertTotalToPayCard(): String {
    return this.roundUpUnitK().toInt().toString()
}

//======================================== String To Date ==========================================
// String --> Date --> String
fun String.convertTimeZoneToString(): String {
    val df  = SimpleDateFormat("dd/MM/yyyy HH:mm")
    val date = this.convertTimeZoneToDateTime()
    return df.format(date)
}

fun String.convertTimeZoneToDateString(): String {
    val df  = SimpleDateFormat("dd/MM/yyyy")
    val date = this.convertTimeZoneToDateTime()
    return df.format(date)
}

fun String.convertDateToString() : String {
    val date = SimpleDateFormat("yyyy/MM/dd").parse(this)
    return SimpleDateFormat("dd/MM").format(date)
}
fun String.convertDateTimeToString() : String {
    val date = SimpleDateFormat("yyyy/MM/dd HH:mm").parse(this)
    return SimpleDateFormat("HH:mm").format(date)
}

fun String.convertDateTimeToString2() : String {
    val date = SimpleDateFormat("dd/MM/yyyy HH:mm").parse(this)
    return date.convertToTimeZone()
}

fun String.convertTimeZoneToDateTime(): Date {
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSFFFFF'Z'")
        sdf.timeZone = TimeZone.getTimeZone(GMT)
        return sdf.parse(this)
    } catch (e: Exception) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'") //ios
        sdf.timeZone = TimeZone.getTimeZone(GMT)
        return sdf.parse(this)
    }
}

fun String.convertTimeZoneToHourString() : String {
    val hf = SimpleDateFormat("HH:mm")
    val date = this.convertTimeZoneToDateTime()
    return hf.format(date)
}

fun String.convertHourStringToDateString() : String {
    val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSFFFFF'Z'")
    df.timeZone = TimeZone.getTimeZone(GMT)
    val date = SimpleDateFormat("HH:mm").parse(this)
    return df.format(date)
}

/**
 * Lay gio tu dateConvert ghep voi ngay tu date
 * Tao ra mot vi tri ngay gio moi.
 * */
fun String.createDateTo2Date(date: Date): Date {
    val dateConvert = this.convertTimeZoneToDateTime()
    dateConvert.year = date.year
    dateConvert.month = date.month
    dateConvert.date = date.date
    return dateConvert
}

fun String.convertTimeZoneToDateTimeGMT(): Date {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSFFFFF")
    sdf.timeZone = TimeZone.getTimeZone(GMT)
    return sdf.parse(this)
}

fun String.convertStrDayToTimeZone(): String {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val date = sdf.parse(this)
        date.hours = 7
        date.convertToTimeZone()
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}
//============================================== End ===============================================

//======================================== Date To String ==========================================
fun Date.convertToTimeZone() : String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSFFFFF'Z'")
    sdf.timeZone = TimeZone.getTimeZone(GMT)

    return sdf.format(this)
}

fun Date.convertToStringDateFormat() : String {
    val sdfDate = SimpleDateFormat("dd/MM/yyyy")
    return sdfDate.format(this)
}

fun Date.convertToStringDateTimeFormat() : String {
    val sdfDate = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    return sdfDate.format(this)
}

fun Date.convertToStringDateTime() : String {
    val sdfDate = SimpleDateFormat("dd/MM/yyyy HH:mm")
    return sdfDate.format(this)
}

fun Date.convertToStringDateTimeFormat2() : String {
    val sdfDate = SimpleDateFormat("HH:mm dd/MM")
    return sdfDate.format(this)
}

fun Date.convertToStringWithDayInWeek() : String {
    val sdfDate = SimpleDateFormat("EEEE, dd/MM/yyyy")
    return sdfDate.format(this)
}

fun Date.convertToStringTimestampFormat() : String {
    val sdf = object : SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ") {
        override fun format(date: Date, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer {
            val toFix = super.format(date, toAppendTo, pos)
            return toFix.insert(toFix.length - 2, ':')
        }
    }

    return sdf.format(this)
}

fun Date.convertToStringWhenChooseProductIsTime(toDate: Date, content: String = "") : String {
    val sdfDate = SimpleDateFormat("dd/MM HH:mm")
    val description = "${sdfDate.format(this)}=>${sdfDate.format(toDate)} ($content)"
    return URLDecoder.decode(description, "utf-8")
}

fun Date.convertToStringWhenDescription(toDate: Date) : String {
    val sdfDate = SimpleDateFormat("dd/MM")
    val sdfHours = SimpleDateFormat("HH:mm")
    val description = "${sdfDate.format(this)} ${sdfHours.format(this)}=>${sdfHours.format(toDate)}"
    return URLDecoder.decode(description, "utf-8")
}

fun Date.convertToHoursString() : String {
    val sdfHours = SimpleDateFormat("HH:mm")
    return sdfHours.format(this)
}

fun Date.convertToDateString() : String {
    val df  = SimpleDateFormat("dd/MM/yyyy")
    return df.format(this)
}

fun Date.convertToBillNumberPayCard() : String {
    val sdfDate = SimpleDateFormat("MM_dd_HH_mm_ss")
    return "365_${sdfDate.format(this)}"
}
//============================================== End ===============================================
fun String.filterCreateDate(): String {
    return "CreatedDate+eq+'$this'"
}

fun String.filterDate(): String {
    return "PurchaseDate+eq+'$this'"
}

fun Int.filterBranch(): String {
    return "BranchId+eq+$this"
}

fun String.convertToDate() : Date {
    val sdfDate = SimpleDateFormat("dd/MM/yyyy")
    return sdfDate.parse(this)
}

fun String.filterDateFromTo(to: String): String {
    // Xu ly tang them 1 ngay cho ngay den.
    val sdfDate = SimpleDateFormat("dd/MM/yyyy")
    val calendar = Calendar.getInstance()
    calendar.time = sdfDate.parse(to)
    calendar.add(Calendar.DATE, 1)
    val toCheck = sdfDate.format(calendar.time)

    return "(PurchaseDate+ge+'datetime''${this.convertToDate().convertToTimeZone()}'''+and+PurchaseDate+lt+'datetime''${toCheck.convertToDate().convertToTimeZone()}''')"
}

fun Int.convertVersionCodeToDateString(): String {
    val year = this / 10000
    val month = (this % 10000) / 100
    val date = (this % 10000) % 100
    return "${if(date < 10) "0$date" else date}/${if(month < 10) "0$month" else month}/$year"
}

class DateTimeUtils {
    companion object {
        fun dateTimeFace(context: Context, l: Long) : String {
            var d1 = (l / 60).toDouble()
            if (l < 5) {
                return context.getString(R.string.ngay_bay_gio)
            }
            if (l < 60) {
                return (StringBuilder()).append("$l").append(" ").append(context.getString(R.string.giay_truoc)).toString()
            }
            if (l < 60 * 2) {
                return context.getString(R.string.mot_phut_truoc)
            }
            if (d1 < 60) {
                return (StringBuilder()).append("$d1").append(" ").append(context.getString(R.string.phut_truoc)).toString()
            }
            if (d1 < 60 * 2) {
                return context.getString(R.string.mot_gio_truoc)
            }
            if (d1 < 60 * 24) {
                d1 = Math.floor(d1 / 60)
                return (StringBuilder()).append("$d1").append(" ").append(context.getString(R.string.gio_truoc)).toString()
            }
            if (d1 < 60 * 24 * 2) {
                return context.getString(R.string.hom_qua)
            }
            if (d1 < 60 * 24 * 7) {
                d1 = Math.floor(d1 / (60 * 24))
                return (StringBuilder()).append("$d1").append(" ").append(context.getString(R.string.hom_truoc)).toString()
            }
            if (d1 < 60 * 24 * 7 * 2) {
                return context.getString(R.string.tuan_truoc)
            }
            if (d1 < 60 * 24 * 31) {
                d1 = Math.floor(d1 / (60 * 24 * 7))
                return (StringBuilder()).append("$d1").append(" ").append(context.getString(R.string.tuan_truoc)).toString()
            }
            if (d1 < 60 * 24 * (30 + 31)) {
                return context.getString(R.string.thang_truoc)
            }
            if (d1 < 60 * 24 * (30 + 31)) {
                d1 = Math.floor(d1 / (60 * 24 * 30))
                return (StringBuilder()).append("$d1").append(" ").append(context.getString(R.string.thang_truoc)).toString()
            }
            if (d1 < 60 * 24 * (365 + 366)) {
                return context.getString(R.string.nam_ngoai)
            }
            return if (d1 > 60 * 24 * (365 + 366)) {
                d1 = Math.floor(d1 / (60 * 24 * 365))
                (StringBuilder()).append("$d1").append(" ").append(context.getString(R.string.nam_truoc)).toString()
            } else {
                context.getString(R.string.dang_cap_nhat)
            }
        }

        fun getDifferenceDate(startDate: Date, endDate: Date, resources: Resources) : String {
            var isUseSpace = false

            val interval = Interval(startDate.time, endDate.time)
            val period = interval.toPeriod()

            val years = if (period.years > 0) "${period.years} ${resources.getString(R.string.tieu_de_nam)}" else ""
            isUseSpace = isUseSpace || years.isNotEmpty()
            val months = if (period.months > 0) "${if(isUseSpace) " " else ""}${period.months} ${resources.getString(R.string.thang)}" else ""
            isUseSpace = isUseSpace || months.isNotEmpty()
            val days = if (period.days > 0) "${if(isUseSpace) " " else ""}${period.days} ${resources.getString(R.string.ngay_2)}" else ""
            isUseSpace = isUseSpace || days.isNotEmpty()
            val hours = if (period.hours > 0) "${if(isUseSpace) " " else ""}${period.hours} ${resources.getString(R.string.gio)}" else ""
            isUseSpace = isUseSpace || hours.isNotEmpty()
            val minutesAndSecond = period.minutes + (if (period.seconds > 0) 1 else 0)
            val minutes = if (minutesAndSecond > 0) "${if(isUseSpace) " " else ""}$minutesAndSecond ${resources.getString(R.string.phut)}" else ""
            //isUseSpace = isUseSpace || minutes.isNotEmpty()
            //val seconds = if (period.seconds > 0) "${if(isUseSpace) " " else ""}${period.seconds} ${resources.getString(R.string.giay)}" else ""
            return "$years$months$days$hours$minutes"
        }

        fun getDifferenceDateMinutes(startDate: Date, endDate: Date) : Int {
            return getDifferenceDateSeconds(startDate, endDate) / 60
        }

        fun getDifferenceDateSeconds(startDate: Date, endDate: Date) : Int {
            val diff = Math.abs(endDate.time - startDate.time)
            val diffSeconds = (diff / 1000)
            return  diffSeconds.toInt()
        }

        // Dau vao la so giay, tinh ra String: gio, phut, giay
        fun getDifferenceDate(secondsCount: Double, resources: Resources) : String {
            return getDifferenceDate(secondsCount.toInt(), resources)
        }
        fun getDifferenceDate(secondsCount: Int, resources: Resources) : String {
            // 2 thoi gia cung bat dau 1 luc.
            var start = Date()
            var end   = Date()
            // Cong them khoang thoi gian chenh lech
            var seconds = secondsCount % 60
            var minutes = secondsCount / 60 % 60
            var hours = secondsCount / 60 / 60

            end.hours   += hours
            end.minutes += minutes
            end.seconds += seconds

            return getDifferenceDate(start, end, resources)
        }
    }
}
//CharSequence to string
fun Editable.formatPriceDisplay(): String {
    val nf = NumberFormat.getNumberInstance(Locale.US)
    val formatter = nf as DecimalFormat
    formatter.applyPattern("###,###")
    var number = this.toString().convertTextDecimalToDouble()
    return formatter.format(number)
}

fun Editable.convertTextDecimalToDoubleDisplay() : String {
    return try {
        this.toString().replace(",","")
    } catch (e: Exception) {
        "0"
    }
}

fun Editable.convertToFloat(): Float {
    return try {
        this.toString().replace(",", "").toFloat()
    } catch (e: Exception) {
        0F
    }
}

fun Editable.convertToDouble(): Double {
    return try {
        this.toString().replace(",", "").toDouble()
    } catch (e: Exception) {
        0.0
    }
}

fun Editable.convertToInt(): Int {
    return try {
        this.toString().replace(",", "").replace(".","").toInt()
    } catch (e: Exception) {
        0
    }
}

//More
fun Int.convertDots200dpiToDp(): Float {
    return this / 8.0F
}

fun Float.convertDots200dpiToDp(): Float {
    return this / 8.0F
}

// OrderStock
fun Int.filterStatus() : String {
    return "Status+eq+$this"
}

// CashFlow
fun Int.filterCashFlowForm() : String {
    return "AccountingTransactionType+eq+$this"
}
fun String.filterAllCashFlowForm() : String {
    return "$this"
}

fun String.filterPartnerId() : String{
    return "PartnerId+eq+$this"
}

fun Int.filterCategory() : String {
    return "GroupId+eq+$this"
}

fun String.filterTransDate() : String {
    return "TransDate+eq+'$this'"
}

fun String.filterTransDateFrom() : String {
    return "TransDate+ge+'datetime''$this'''"
}

fun String.filterTransDateTo() : String {
    return "TransDate+lt+'datetime''$this'''"
}

fun Int.filterAccountingGroup() : String {
    return "GroupId+eq+$this"
}

// Voucher
fun Int.filterStatusVoucher() : String {
    return "Status+eq+$this"
}
fun String.filterAllStatusVoucher() : String {
    return "$this"
}
fun String.filterDateFromToVoucher(to: String): String {
    val sdfDate = SimpleDateFormat("dd/MM/yyyy")
    val calendar = Calendar.getInstance()
    calendar.time = sdfDate.parse(to)
    calendar.add(Calendar.DATE, 1)
    val toCheck = sdfDate.format(calendar.time)

    return "(CreatedDate+ge+'datetime''${this.convertToDate().convertToTimeZone()}'''+and+CreatedDate+lt+'datetime''${toCheck.convertToDate().convertToTimeZone()}''')"
}

fun String.filterDocumentDate(): String {
    return "DocumentDate+eq+'$this'"
}

fun Int.filterProductId() : String {
    return "ProductId+eq+$this"
}

//Convert Date to Calendar
fun Date.convertToCalendar() : Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar
}




