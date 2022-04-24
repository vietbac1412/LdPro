package tamhoang.ldpro4.util.extension

import android.content.Context
import retrofit2.HttpException
import timber.log.Timber
import tamhoang.ldpro4.R

fun Throwable.getMessageError(): String {
//    return if (this is HttpException) {
//        val errorBody = this.response()?.errorBody()?.string() ?: ""
//        ErrorResponse.convertStringToObject(errorBody).responseStatus.message
//    } else ""
    return ""
}

fun Throwable.getMessageError(context : Context): String {
//    return if (this is HttpException) {
//        val errorBody = this.response()?.errorBody()?.string() ?: ""
//        val message = ErrorResponse.convertStringToObject(errorBody).responseStatus.message
//        if (message == "") context.getString(R.string.thong_bao_loi_xu_ly_du_lieu) else message
//    } else context.getString(R.string.thong_bao_loi_xu_ly_du_lieu)
    return ""
}