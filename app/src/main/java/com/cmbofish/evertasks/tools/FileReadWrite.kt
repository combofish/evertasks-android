package com.cmbofish.evertasks.tools

import android.os.Environment
import android.util.Log
import java.io.File

class FileReadWrite(f: File) {

    private val externalFilesDir = f
    private val TAG = "FileReadWrite"

    fun readStringFromFile(fileName: String): Pair<String, Int> {
        if (!isExternalStorgeWritable()) return Pair("", 0)
        Log.i(TAG, "readStringFromFile: dir = " + externalFilesDir?.absolutePath);

        if (!externalFilesDir?.exists()!!) return Pair("", 0)

        val file = File(externalFilesDir, fileName)
        if (!file.exists()) return Pair("", 0)

        var n = 0
        file.readLines().forEach {
            if (it != null && it != "") {
                n++
            }
        }
        return Pair(file.readText(), n)
    }

    fun writeStringToFile(s: String, fileName: String) {
        if (!isExternalStorgeWritable()) {
            return
        }
        //  val externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        Log.i(TAG, "writeStringToFile: dir = " + externalFilesDir?.absolutePath);

        if (externalFilesDir?.exists() == false) {
            externalFilesDir?.mkdirs()
        }

        val file = File(externalFilesDir, fileName)
        if (file.exists()) {
            file.delete();
        }

        file.writeText(s)
    }

    private fun isExternalStorgeWritable(): Boolean {
        val externalStorageState = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == externalStorageState) {
            return true
        }
        Log.i(TAG, "is $externalStorageState")
        return false
    }
}