package com.example.phonept

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 회원가입 버튼 (R.id.regi_button) 클릭 시 register_first로 이동
        val regiButton: Button = findViewById(R.id.regi_button)
        regiButton.setOnClickListener {
            val intent = Intent(this, register_first::class.java)
            startActivity(intent)
        }

        // 로그인 버튼 (R.id.log_button) 클릭 시 TrainerHomeActivity로 이동
        val logButton: Button = findViewById(R.id.log_button)
        logButton.setOnClickListener {
            val intent = Intent(this, TrainerHomeActivity::class.java)
            startActivity(intent)
        }
    }
}