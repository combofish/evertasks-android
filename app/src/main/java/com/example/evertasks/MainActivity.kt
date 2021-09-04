package com.example.evertasks

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.*

class MainActivity : AppCompatActivity(), View.OnFocusChangeListener {
    private val TAG = "MainActivity"
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private lateinit var editText1: EditText
    private lateinit var editText2: EditText
    private val HaveToFileName = "HaveTo.txt"
    private val WantToFileName = "WantTo.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        initView()
        addListener()
        refreshContent()
    }

    private fun readStringFromFile(fileName: String): Pair<String,Int> {
        if (!isExternalStorgeWritable()) return Pair("",0)
        val externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        Log.i(TAG, "readStringFromFile: dir = " + externalFilesDir?.absolutePath);

        if (!externalFilesDir?.exists()!!) return Pair("",0)

        val file = File(externalFilesDir, fileName)
        if (!file.exists()) return Pair("",0)

        var n = 0
        file.readLines().forEach {
            if (it != null && it != ""){
                n++
            }
        }
        return Pair(file.readText(),n)
    }

    private fun writeStringToFile(s: String, fileName: String) {
        if (!isExternalStorgeWritable()) {
            return
        }
        val externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
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

    override fun onFocusChange(p0: View?, p1: Boolean) {
        if (!p1) {
            Log.i(TAG, "edittext1: ${editText1.text}, edittext2: ${editText2.text}")
            if (p0?.id?.equals(editText1.id) == true) {
                writeStringToFile("${editText1.text}", HaveToFileName)
                refreshContent()
            } else if (p0?.id?.equals(editText2.id) == true) {
                writeStringToFile("${editText2.text}", WantToFileName)
                refreshContent()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun refreshContent() {
        val (s,n) = readStringFromFile(HaveToFileName)
        editText1.setText(s)
        textView1.text = "Sum: $n"

        val (s1,n1) = readStringFromFile(WantToFileName)
        editText2.setText(s1)
        textView2.text = "Sum: $n1"
    }

    private fun addListener() {
        editText1.onFocusChangeListener = this
        editText2.onFocusChangeListener = this
    }

    private fun initView() {
        textView1 = findViewById(R.id.have_to_sum)
        textView2 = findViewById(R.id.want_to_sum)

        editText1 = findViewById(R.id.add_content1)
        editText2 = findViewById(R.id.add_content2)
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