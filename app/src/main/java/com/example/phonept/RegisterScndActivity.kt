package com.example.phonept

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class RegisterScndActivity : AppCompatActivity() {

    private val TAG = "RegisterScnd"

    // 💡 Companion Object에 상수 정의 (안전한 Intent Key 사용을 위함)
    companion object {
        const val MEMBER_TYPE_KEY = "SELECTED_OPTION_VALUE" // Member Type (Int: 1, 2, 3)
        const val USER_ROLE_KEY = "USER_ROLE"               // User Role (String: "trainer", "member")

        // 💡 새로 추가된 Extra Key 정의
        const val EMAIL_KEY = "EMAIL_INPUT"
        const val NICKNAME_KEY = "NICKNAME_INPUT"
        const val PASSWORD_KEY = "PASSWORD_INPUT"
        const val GYM_NAME_KEY = "GYM_NAME_INPUT"
        const val TRAINER_NAME_KEY = "TRAINER_NAME_INPUT"
    }

    // 뷰 변수 선언
    private lateinit var emailEdit: EditText
    private lateinit var nickEdit: EditText
    private lateinit var pwdEdit: EditText
    private lateinit var pwdCorEdit: EditText
    private lateinit var gymSelectText: TextView
    private lateinit var trainerSelectText: TextView

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
        val receivedMemberType = intent.getIntExtra(MEMBER_TYPE_KEY, 0)
        val receivedUserRole = intent.getStringExtra(USER_ROLE_KEY) ?: "unknown"

        Log.d(TAG, "Received Member Type: $receivedMemberType, Role: $receivedUserRole")

        // 2. 뷰 찾기 및 초기화 (EditText 4개, TextView 2개)
        emailEdit = findViewById(R.id.email_edit)
        nickEdit = findViewById(R.id.nick_edit)
        pwdEdit = findViewById(R.id.pwd_edit)
        pwdCorEdit = findViewById(R.id.pwdcor_edit)
        gymSelectText = findViewById(R.id.gym_select_text)
        trainerSelectText = findViewById(R.id.trainer_select_text)

        val trainerTitle = findViewById<TextView>(R.id.textView8)
        val trainerLayout = findViewById<LinearLayout>(R.id.gridLayout8)

        // 3. 트레이너 항목 가시성 설정 (PT 회원인 경우(Type 2)에만 보이도록 설정)
        val isPtMember = (receivedMemberType == 2)
        if (isPtMember) {
            trainerTitle.visibility = View.VISIBLE
            trainerLayout.visibility = View.VISIBLE
            Log.d(TAG, "Member is PT Member. Trainer field VISIBLE.")
        } else {
            trainerTitle.visibility = View.GONE
            trainerLayout.visibility = View.GONE
            Log.d(TAG, "Member is $receivedUserRole (Type $receivedMemberType). Trainer field GONE.")
        }

        // 4. 다음 버튼 클릭 리스너 추가 (RegisterThirdActivity로 이동 및 데이터 전달)
        val footerRootView: View? = findViewById(R.id.next2_button)

        footerRootView?.let { root ->
            val nextButton = root.findViewById<AppCompatButton>(R.id.footer_button_bg)

            nextButton?.setOnClickListener {
                if (validateInput(isPtMember)) {
                    sendDataToRegisterThird(receivedMemberType, receivedUserRole, isPtMember)
                }
            }
        }
    }

    /**
     * 모든 필수 입력 필드의 유효성을 검사합니다.
     */
    private fun validateInput(isPtMember: Boolean): Boolean {
        val email = emailEdit.text.toString().trim()
        val nick = nickEdit.text.toString().trim()
        val pwd = pwdEdit.text.toString()
        val pwdCor = pwdCorEdit.text.toString()
        val gym = gymSelectText.text.toString().trim()

        if (email.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (nick.isEmpty()) {
            Toast.makeText(this, "닉네임을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pwd.isEmpty() || pwdCor.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력 및 확인해 주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pwd != pwdCor) {
            Toast.makeText(this, "비밀번호와 확인 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return false
        }
        val defaultGymText = gymSelectText?.hint?.toString() ?: ""

        if (gym.isEmpty() || gym == defaultGymText) {
            Toast.makeText(this, "소속 헬스장 정보를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        // PT 회원인 경우에만 트레이너 필드 검사
        if (isPtMember) {
            val trainer = trainerSelectText.text.toString().trim()
            if (trainer.isEmpty() || trainer == trainerSelectText.hint.toString()) {
                Toast.makeText(this, "담당 트레이너를 선택해 주세요.", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        return true
    }


    /**
     * 유효성 검사를 통과한 모든 데이터를 RegisterThirdActivity로 전달합니다.
     */
    private fun sendDataToRegisterThird(memberType: Int, userRole: String, isPtMember: Boolean) {

        val intent = Intent(this, RegisterThirdActivity::class.java)

        // 1. 이전 단계에서 받은 필수 데이터 전달
        intent.putExtra(MEMBER_TYPE_KEY, memberType)
        intent.putExtra(USER_ROLE_KEY, userRole)

        // 2. 현재 화면에서 입력받은 데이터 전달
        intent.putExtra(EMAIL_KEY, emailEdit.text.toString().trim())
        intent.putExtra(NICKNAME_KEY, nickEdit.text.toString().trim())
        // 비밀번호는 보안상 Hash 처리해야 하나, 예제 흐름을 위해 평문 전달
        intent.putExtra(PASSWORD_KEY, pwdEdit.text.toString())

        // 헬스장 정보 전달
        intent.putExtra(GYM_NAME_KEY, gymSelectText.text.toString().trim())

        // 트레이너 정보 (PT 회원일 경우에만 실제 선택된 값, 아니면 빈 문자열 전달)
        val trainerName = if (isPtMember) trainerSelectText.text.toString().trim() else ""
        intent.putExtra(TRAINER_NAME_KEY, trainerName)

        Log.d(TAG, "Data passing to Third Activity: Email=${emailEdit.text}, Gym=${gymSelectText.text}, Trainer=$trainerName")

        startActivity(intent)
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
                textView.hint = null // 힌트 텍스트 제거
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