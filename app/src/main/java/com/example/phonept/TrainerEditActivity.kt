package com.example.phonept

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * 트레이너 루틴 편집/등록 화면 액티비티
 * - 루틴을 수정/등록하고, 완료 시 홈 화면으로 돌아갑니다.
 */
class TrainerEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trainer_edit) // trainer_edit.xml 레이아웃 사용

        // 1. '루틴 등록하기' 버튼 설정
        val btnRegisterRoutine: Button = findViewById(R.id.btn_register_routine)
        btnRegisterRoutine.setOnClickListener {
            // 실제로는 여기에 루틴 저장 로직이 들어갑니다.


            // 현재 액티비티를 종료하고 이전 액티비티(TrainerHomeActivity)로 돌아갑니다.
            finish()
        }

        // 2. '취소' 버튼 설정
        val btnCancelEdit: Button = findViewById(R.id.btn_cancel_edit)
        btnCancelEdit.setOnClickListener {
            Toast.makeText(this, "루틴 등록이 취소되었습니다.", Toast.LENGTH_SHORT).show()

            // 현재 액티비티를 종료하고 이전 액티비티(TrainerHomeActivity)로 돌아갑니다.
            finish()
        }
    }
}