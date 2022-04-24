package tamhoang.ldpro4.util.extension

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipInputStream

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 11/10/2018.
 */
@Throws(IOException::class)
fun InputStream.unzip(destination: File) {
    val buffer = ByteArray(1024)
    val zis = ZipInputStream(this)
    var ze = zis.nextEntry
    while (ze != null) {
        val fileName = ze.name
        val newFile = File(destination, fileName)
        if (ze.isDirectory) {
            newFile.mkdirs()
        } else {
            File(newFile.parent).mkdirs()
            val fos = FileOutputStream(newFile)
            var len = 0
            while (zis.read(buffer).also { len = it } > 0) {
                fos.write(buffer, 0, len)
            }
            fos.close()
        }
        ze = zis.nextEntry
    }
    zis.closeEntry()
    zis.close()
    this.close()
}