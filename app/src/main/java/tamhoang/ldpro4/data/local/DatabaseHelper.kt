package tamhoang.ldpro4.data.local

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.squareup.sqlbrite2.BriteDatabase
import io.reactivex.Observable
import timber.log.Timber
import tamhoang.ldpro4.data.constants.Constants
import tamhoang.ldpro4.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseHelper
@Inject
constructor(private val db: BriteDatabase,
            private val dbOpenHelper: DbOpenHelper,
            private val preferencesHelper: PreferencesHelper) {

    private fun deleteAllTable() {
        val transaction = db.newTransaction()

        try {
            val contentValues = ContentValues()
            contentValues.put("locale", "en_us")
            db.insert("android_metadata", contentValues)

//            val serverEvent  = db.delete(ServerEvents.TABLE_NAME, null)

            transaction.markSuccessful()
        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            transaction.end()
            //db.close()
        }
    }


}