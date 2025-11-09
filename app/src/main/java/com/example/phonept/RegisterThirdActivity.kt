package com.example.phonept

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class RegisterThirdActivity : AppCompatActivity() {

    // 1. 신장, 체중, 목적, 난이도에 대한 옵션 리스트 정의 (요청받은 내용 기반)
    private val heightOptions = arrayOf(
        "~ 165cm", "166 ~ 170cm", "171 ~ 175cm", "176 ~ 180cm", "181 ~ 185cm", "186cm ~"
    )

    // 사용자가 제시한 패턴(~80kg까지 5kg 단위, 이후 100kg~)에 따라 확장
    private val weightOptions = arrayOf(
        "~ 60kg", "61 ~ 65kg", "66 ~ 70kg", "71 ~ 75kg", "76 ~ 80kg",
        "81 ~ 85kg", "86 ~ 90kg", "91 ~ 95kg", "96 ~ 100kg", "100kg ~"
    )

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
     * 신장(Height) 선택 드롭다운/다이얼로그 표시
     */
    fun showHeightSelector(view: View) {
        showSelectorDialog(
            title = "신장 (cm) 선택",
            options = heightOptions,
            textViewId = R.id.height_select_text
        )
    }

    /**
     * 체중(Weight) 선택 드롭다운/다이얼로그 표시
     */
    fun showWeightSelector(view: View) {
        showSelectorDialog(
            title = "체중 (kg) 선택",
            options = weightOptions,
            textViewId = R.id.weight_select_text
        )
    }

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