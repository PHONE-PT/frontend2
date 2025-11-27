package com.example.phonept

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

// 클래스 이름을 Kotlin/Android 관례에 따라 대문자로 시작하도록 변경
class register_first : AppCompatActivity() {

    // 💡 전역 변수를 클래스 내부의 멤버 변수로 변경하여 상태 관리 안정화
    // memberType: 최종 선택 값 (1: 트레이너, 2: PT 회원, 3: 개인 회원)
    private var memberType: Int? = null
    // isSecondStage: 현재 선택 단계 (false: 회원 종류, true: 헬스장 회원 세부 유형)
    private var isSecondStage: Boolean = false
    private val TAG = "RegisterFirst"


    // 뷰 변수 선언
    private lateinit var titleTextView: TextView
    private lateinit var option1RadioButton: RadioButton
    private lateinit var option2RadioButton: RadioButton
    private lateinit var radioGroup: RadioGroup
    private lateinit var nextButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_first)

        // 1. 모든 뷰 초기화
        radioGroup = findViewById(R.id.radioGroupOptions)
        titleTextView = findViewById(R.id.title)
        option1RadioButton = findViewById(R.id.radioOption1)
        option2RadioButton = findViewById(R.id.radioOption2)

        val footerRootView: View? = findViewById(R.id.next1_button)

        if (footerRootView == null) {
            Log.e(TAG, "❌ next1_button (include 태그 ID)를 찾을 수 없습니다. 레이아웃 확인 필요.")
            Toast.makeText(this, "버튼 연결 오류: next1_button ID를 찾을 수 없습니다.", Toast.LENGTH_LONG).show()
            return
        }

        footerRootView.let { root ->
            // register_footer.xml에 정의된 버튼 ID 사용
            val buttonInFooter: AppCompatButton? = root.findViewById(R.id.footer_button_bg)

            if (buttonInFooter == null) {
                Log.e(TAG, "❌ footer_button_bg (실제 버튼 ID)를 찾을 수 없습니다. register_footer.xml 확인 필요.")
                Toast.makeText(this, "버튼 연결 오류: footer_button_bg ID를 찾을 수 없습니다.", Toast.LENGTH_LONG).show()
                return
            }

            nextButton = buttonInFooter

            // 2. Next Button 클릭 리스너 설정
            nextButton.setOnClickListener {
                handleNextButtonClick()
            }
        }

        // 3. RadioGroup 선택 리스너 설정 (선택 값만 갱신)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            // 선택된 라디오 버튼의 ID에 따라 임시 memberType 값 저장
            memberType = when (checkedId) {
                R.id.radioOption1 -> 1
                R.id.radioOption2 -> 2
                else -> null
            }
            Log.d(TAG, "선택 값 갱신: $memberType, 상태: $isSecondStage")
        }

        // 초기 상태 설정
        updateUIForStage(isSecondStage)
    }

    override fun onRestart() {
        super.onRestart()

        // 1. 상태 변수 초기화
        isSecondStage = false
        memberType = null
        Log.d(TAG, "상태 초기화됨 (onRestart)")

        // 2. UI 초기 상태로 복구
        updateUIForStage(isSecondStage)

        // 3. 라디오 버튼 선택도 초기화
        radioGroup.clearCheck()
    }

    /**
     * Next 버튼 클릭 이벤트 처리
     */
    private fun handleNextButtonClick() {

        if (memberType == null) {
            Toast.makeText(this, "옵션을 선택해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // isSecondStage를 기준으로 로직 분기
        when (isSecondStage) {

            // A. 텍스트가 변경되지 않은 첫 번째 단계 (회원 종류 선택)
            false -> {
                when (memberType) {
                    1 -> {
                        // 옵션 1 선택: 트레이너 (최종 값 1) -> 다음 화면 바로 이동
                        startRegisterScndActivity(1) // 최종 값: 1
                    }
                    2 -> {
                        // 옵션 2 선택: 헬스장 회원 (PT 회원 / 개인 회원) -> 텍스트 변경 및 상태 전환
                        changeToSecondStage()
                    }
                }
            }

            // B. 텍스트가 변경된 두 번째 단계 (헬스장 회원 세부 유형 선택)
            true -> {
                // 헬스장 회원 세부 유형 값 조정
                // Stage 2에서 radioOption1(값 1) 선택 -> 최종 값 2 (PT 회원)
                // Stage 2에서 radioOption2(값 2) 선택 -> 최종 값 3 (개인 회원)
                val finalMemberType = memberType!! + 1
                startRegisterScndActivity(finalMemberType)
            }
        }
    }

    /**
     * 옵션 2 선택 시 텍스트를 변경하고 라디오 버튼 선택을 초기화하는 함수
     */
    private fun changeToSecondStage() {
        // 1. 상태 전환 및 값 초기화
        isSecondStage = true
        memberType = null

        // 2. UI 업데이트
        updateUIForStage(isSecondStage)

        // 3. 라디오 버튼 선택 초기화
        radioGroup.clearCheck()

        Toast.makeText(this, "헬스장 회원 세부 유형을 선택해 주세요.", Toast.LENGTH_LONG).show()
    }

    /**
     * 현재 단계에 맞게 UI 텍스트를 업데이트하는 통합 함수
     */
    private fun updateUIForStage(isSecondStage: Boolean) {
        if (!isSecondStage) {
            // 1단계: 회원 종류 선택
            titleTextView.text = getString(R.string.reg_first_title)
            option1RadioButton.text = getString(R.string.left_option)
            option2RadioButton.text = getString(R.string.right_option)
        } else {
            // 2단계: 헬스장 회원 세부 유형 선택
            titleTextView.text = getString(R.string.reg_first_title1)
            option1RadioButton.text = getString(R.string.left_option1)
            option2RadioButton.text = getString(R.string.right_option1)
        }
    }


    /**
     * RegisterScndActivity로 이동하며 선택된 최종 MemberType (Int)과 UserRole (String)을 전달하는 함수
     * @param value 최종 MemberType (1, 2, 3)
     */
    private fun startRegisterScndActivity(value: Int) {
        val intent = Intent(this, RegisterScndActivity::class.java)

        // 1. MemberType (숫자) 전달
        intent.putExtra(RegisterScndActivity.MEMBER_TYPE_KEY, value)

        // 2. 💡 UserRole (문자열) 계산 및 전달 (요청 사항)
        val role = when (value) {
            1 -> "trainer" // 1일 때 trainer
            in 2..3 -> "member" // 2 또는 3일 때 member
            else -> "unknown"
        }
        intent.putExtra(RegisterScndActivity.USER_ROLE_KEY, role)

        Log.d(TAG, "RegisterScndActivity로 이동. MemberType: $value, UserRole: $role")
        Toast.makeText(this, "다음 화면 이동. Role: $role", Toast.LENGTH_SHORT).show()

        startActivity(intent)
        // finish()는 주석 처리하여 뒤로 가기 버튼으로 돌아올 수 있게 유지
    }
}