package com.example.phonept

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * 루틴을 편집하거나 새로 등록하는 화면입니다.
 * trainer_edit.xml 레이아웃을 사용합니다.
 */
class TrainerEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.trainer_edit) // trainer_edit 레이아웃 사용

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.trainer_edit_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}