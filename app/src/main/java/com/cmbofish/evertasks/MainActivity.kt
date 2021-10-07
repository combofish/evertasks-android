package com.cmbofish.evertasks

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.cmbofish.evertasks.tools.FileReadWrite
import java.io.File
import android.os.Build
import android.view.View

class MainActivity : AppCompatActivity(), TextWatcher {
    private val TAG = "MainActivity"
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private lateinit var editText1: EditText
    private lateinit var editText2: EditText
    private val HaveToFileName = "HaveTo.txt"
    private val WantToFileName = "WantTo.txt"

    private lateinit var f: File
    private lateinit var ftool: FileReadWrite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }

        f = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!
        ftool = FileReadWrite(f)

        initView()
        refreshContent()
    }

    @SuppressLint("SetTextI18n")
    private fun refreshContent() {
        var (s, n) = ftool.readStringFromFile(HaveToFileName)
        editText1.setText(s)
        textView1.text = "Sum: $n"

        var (s1, n1) = ftool.readStringFromFile(WantToFileName)
        editText2.setText(s1)
        textView2.text = "Sum: $n1"
    }

    private fun initView() {
        textView1 = findViewById(R.id.have_to_sum)
        textView2 = findViewById(R.id.want_to_sum)

        editText1 = findViewById(R.id.add_content1)
        editText2 = findViewById(R.id.add_content2)

        editText1.addTextChangedListener(this)
        editText2.addTextChangedListener(this)
    }

    override fun afterTextChanged(p0: Editable?) {
        val (s1, n1) = ftool.readStringFromFile(HaveToFileName)
        if (s1 != "${editText1.text}" || s1 == "") {
            ftool.writeStringToFile("${editText1.text}", HaveToFileName)
            val (s1, n1) = ftool.readStringFromFile(HaveToFileName)
            textView1.text = "Sum: $n1"
            Log.i(TAG, "${editText1.text} and n1: $n1")
        }

        val (s2, n2) = ftool.readStringFromFile(WantToFileName)
        if (s2 != "${editText2.text}" ||  s2 == "") {
            ftool.writeStringToFile("${editText2.text}", WantToFileName)
            val (s2, n2) = ftool.readStringFromFile(WantToFileName)
            textView2.text = "Sum: $n2"
            Log.i(TAG, "${editText2.text} and n1: $n2")
        }
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }


}