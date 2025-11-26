package com.example.phonept

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class RegisterThirdActivity : AppCompatActivity() {

    private val goalOptions = arrayOf(
        "체지방 감량", "근력 향상", "체력 증진"
    )

    private val difficultyOptions = arrayOf(
        "상", "중", "하"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 요청하신 레이아웃 파일명으로 설정합니다.
        setContentView(R.layout.register_third)

        val footerRootView: View? = findViewById(R.id.next3_button)

        footerRootView?.let { root ->
            val nextButton = root.findViewById<AppCompatButton>(R.id.footer_button_bg)

            nextButton?.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    /**
     * 범용적으로 AlertDialog를 생성하고 선택된 항목으로 TextView를 업데이트하는 헬퍼 함수
     * @param title 다이얼로그의 제목
     * @param options 다이얼로그에 표시할 항목 배열
     * @param textViewId 선택 결과를 표시할 TextView의 ID
     */
    private fun showSelectorDialog(title: String, options: Array<String>, textViewId: Int) {
        val textView = findViewById<TextView>(textViewId)

        // AlertDialog.Builder를 사용하여 리스트 선택 다이얼로그 생성
        // 💡 오류를 해결하기 위해 스타일 인자(R.style.Theme_AppCompat_Light_Dialog_Alert)를 제거하고
        //    액티비티의 기본 테마를 사용하도록 수정했습니다.
        AlertDialog.Builder(this)
            .setTitle(title)
            .setItems(options) { dialog, which ->
                val selectedItem = options[which]
                textView.text = selectedItem // 선택된 항목으로 텍스트 업데이트
                Log.d("Selector", "$title selected: $selectedItem")
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // 💡 XML onClick 속성을 위한 드롭다운 핸들러 함수들

   /**
     * 운동목적(Goal) 선택 드롭다운/다이얼로그 표시
     */
    fun showGoalSelector(view: View) {
        showSelectorDialog(
            title = "운동 목적 선택",
            options = goalOptions,
            textViewId = R.id.goal_select_text
        )
    }

    /**
     * 난이도(Difficulty) 선택 드롭다운/다이얼로그 표시
     */
    fun showDifficultySelector(view: View) {
        showSelectorDialog(
            title = "난이도 선택",
            options = difficultyOptions,
            textViewId = R.id.difficulty_select_text
        )
    }


}