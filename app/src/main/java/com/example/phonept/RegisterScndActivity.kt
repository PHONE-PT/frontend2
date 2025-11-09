package com.example.phonept

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class RegisterScndActivity : AppCompatActivity() {

    // 헬스장 선택 옵션 (더미 데이터)
    private val gymOptions = arrayOf(
        "강남 스포짐", "선릉 피트니스", "역삼 헬스클럽", "논현 퍼스널 트레이닝"
    )

    // 트레이너 선택 옵션 (더미 데이터)
    private val trainerOptions = arrayOf(
        "김민지 트레이너", "이준호 트레이너", "박세리 트레이너", "최영민 트레이너"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_scnd)

        // 1. 이전 화면(register_first)에서 전달된 값 받기
        // 1: 트레이너, 2: PT 회원 (노출 대상), 3: 개인 회원
        val receivedMemberType = intent.getIntExtra("SELECTED_OPTION_VALUE", 0)
        Log.d("RegisterScnd", "Received Member Type: $receivedMemberType (1:Trainer, 2:PT Member, 3:Individual Member)")

        // 2. 뷰 찾기
        val trainerTitle = findViewById<TextView>(R.id.textView8)
        val trainerLayout = findViewById<LinearLayout>(R.id.gridLayout8)

        // 3. 트레이너 항목 가시성 설정 (PT 회원인 경우(2)에만 보이도록 설정)
        if (receivedMemberType == 2) {
            // PT 회원은 담당 트레이너를 선택해야 하므로 보여줍니다.
            trainerTitle.visibility = View.VISIBLE
            trainerLayout.visibility = View.VISIBLE
            Log.d("RegisterScnd", "Member is PT Member (Type 2). Trainer field VISIBLE.")
        } else {
            // 트레이너(1) 또는 개인 회원(3) 등은 트레이너를 선택할 필요가 없으므로 숨깁니다.
            trainerTitle.visibility = View.GONE
            trainerLayout.visibility = View.GONE
            Log.d("RegisterScnd", "Member is Trainer or Individual Member (Type $receivedMemberType). Trainer field GONE.")
        }

        // 4. 다음 버튼 클릭 리스너 추가 (RegisterThirdActivity로 이동)
        val footerRootView: View? = findViewById(R.id.next2_button)

        footerRootView?.let { root ->
            val nextButton = root.findViewById<AppCompatButton>(R.id.footer_button_bg)

            nextButton?.setOnClickListener {
                val intent = Intent(this, RegisterThirdActivity::class.java)
                startActivity(intent)
            }
        }
    }

    /**
     * 범용적으로 AlertDialog를 생성하고 선택된 항목으로 TextView를 업데이트하는 헬퍼 함수
     */
    private fun showSelectorDialog(title: String, options: Array<String>, textViewId: Int) {
        val textView = findViewById<TextView>(textViewId)

        AlertDialog.Builder(this)
            .setTitle(title)
            .setItems(options) { dialog, which ->
                val selectedItem = options[which]
                textView.text = selectedItem
                textView.hint = null
                Log.d("Selector", "$title selected: $selectedItem")
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    /**
     * 헬스장 선택 드롭다운/다이얼로그 표시 (XML onClick="showGymSelector"와 연결)
     */
    fun showGymSelector(view: View) {
        showSelectorDialog(
            title = "헬스장 선택",
            options = gymOptions,
            textViewId = R.id.gym_select_text
        )
    }

    /**
     * 트레이너 선택 드롭다운/다이얼로그 표시 (XML onClick="showTrainerSelector"와 연결)
     */
    fun showTrainerSelector(view: View) {
        showSelectorDialog(
            title = "트레이너 선택",
            options = trainerOptions,
            textViewId = R.id.trainer_select_text
        )
    }
}