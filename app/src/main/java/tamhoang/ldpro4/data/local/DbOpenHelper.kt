package tamhoang.ldpro4.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import timber.log.BuildConfig
import timber.log.Timber
import tamhoang.ldpro4.data.model.*
import tamhoang.ldpro4pos365.injection.ApplicationContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 22/02/2018.
 */
@Singleton
class DbOpenHelper
@Inject constructor(@ApplicationContext private val context: Context,
                    private val mPreferencesHelper: PreferencesHelper)
    : SQLiteOpenHelper(context, mPreferencesHelper.formatNameDB(), null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 34
        //22 New Extra, 23 sync count payment, 24 treeview, 25 promotion, 26 update partner, 27 big update, 28 Description table,
        //29 is hidden product, 30 duplicate order, 31 fix promotion, 32 position product, 33 product config, 34 type payResult
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        //Sử dụng khóa ngoại.
        db.execSQL("PRAGMA foreign_keys=ON;")
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.beginTransaction()
        try {
            db.execSQL("insert into android_metadata values ('en_us');")

            db.execSQL(Room.CREATE_TABLE)
            db.execSQL(RoomGroup.CREATE_TABLE)
            db.execSQL(Categorie.CREATE_TABLE)
            db.execSQL(Product.CREATE_TABLE)
            db.execSQL(Product.CREATE_TABLE_TODAY)
//            db.execSQL(Product.CREATE_UNIQUE)
            db.execSQL(TreeView.CREATE_TABLE)
            db.execSQL(Partner.CREATE_TABLE)

            db.execSQL(ServerEvents.CREATE_TABLE)
            db.execSQL(ServerEvents.CREATE_UNIQUE)

            db.execSQL(PaymentData.CREATE_TABLE)
            db.execSQL(RoomHistory.CREATE_TABLE)

            db.execSQL(PayResult.CREATE_TABLE)

            db.execSQL(Extra.CREATE_TABLE)

            db.execSQL(ProductPromotion.CREATE_TABLE)

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            db.endTransaction()
        }
    }

    // https://github.com/riggaroo/AndroidDatabaseUpgrades
    // https://riggaroo.co.za/android-sqlite-database-use-onupgrade-correctly/
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Timber.e("Updating table from $oldVersion to $newVersion")
         if (BuildConfig.DEBUG) {
            db.dropTable(PaymentData.TABLE_NAME, true)
            db.dropTable(RoomHistory.TABLE_NAME, true)
            db.dropTable(PayResult.TABLE_NAME, true)
            db.dropTable(ServerEvents.TABLE_NAME, true)
            db.dropTable(Extra.TABLE_NAME, true)
            debug(db)
         } else {
             release(db, oldVersion, newVersion)
         }
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Remove super()
    }

    private fun debug(db: SQLiteDatabase) {
        dropData(db)
    }

    private fun release(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        for (i in oldVersion until newVersion) {
            val migrationName = String.format("db/from_%d_to_%d.sql", i, i + 1)
            Timber.e("Looking for migration file: ${migrationName.toString()}")
            readAndExecuteSQLScript(db, context, migrationName)
        }
    }

    fun dropData(db: SQLiteDatabase, isAll: Boolean = false) {
        if (isAll) {
            db.dropTable(PaymentData.TABLE_NAME, true)
            db.dropTable(RoomHistory.TABLE_NAME, true)
            db.dropTable(PayResult.TABLE_NAME, true)
            db.dropTable(ServerEvents.TABLE_NAME, true)
            db.dropTable(Extra.TABLE_NAME, true)
        }
        db.dropTable(ServerEvents.TABLE_NAME, true)
        db.dropTable(Partner.TABLE_NAME, true)
        db.dropTable(TreeView.TABLE_NAME, true)
        db.dropTable(Product.TABLE_NAME_TODAY, true)
        db.dropTable(Product.TABLE_NAME, true)
        db.dropTable(Categorie.TABLE_NAME, true)
        db.dropTable(RoomGroup.TABLE_NAME, true)
        db.dropTable(Room.TABLE_NAME, true)
        db.dropTable(Extra.TABLE_NAME, true)
        db.dropTable(ProductPromotion.TABLE_NAME, true)
        onCreate(db)
    }

    private fun SQLiteDatabase.dropTable(tableName: String, ifExists: Boolean = false) {
        val escapedTableName = tableName.replace("`", "``")
        val ifExistsText = if (ifExists) "IF EXISTS" else ""
        execSQL("DROP TABLE $ifExistsText `$escapedTableName`;")
    }

    private fun readAndExecuteSQLScript(db: SQLiteDatabase, context: Context, fileName: String) {
        if (fileName.isEmpty()) {
            Timber.e("SQL script file name is empty")
            return
        }

        Timber.e("Script found. Executing...")
        val assetManager = context.assets
        var reader: BufferedReader? = null

        try {
            val iS = assetManager.open(fileName)
            val isr = InputStreamReader(iS)
            reader = BufferedReader(isr)
            executeSQLScript(db, reader)
        } catch (e: IOException) {
            Timber.e("IOException: $e")
        } finally {
            reader?.let {
                try {
                    reader.close()
                } catch (e: IOException) {
                    Timber.e("IOException: $e")
                }
            }
        }

    }

    @Throws(IOException::class)
    private fun executeSQLScript(db: SQLiteDatabase, reader: BufferedReader) {
        var statement = StringBuilder()

        val reader = BufferedReader(reader)
        reader.lineSequence().forEach {
            statement.append(it)
            statement.append("\n")
            if (it.endsWith(";")) {
                Timber.e("Update: $statement")
                db.execSQL(statement.toString())
                statement = StringBuilder()
            }
        }
    }
}