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

// 💡 변수 이름 변경: selectedOptionValue -> memberType
private var memberType: Int? = null

// 💡 변수 이름 변경: isTextModified -> memberBool
private var memberBool: Boolean = false

class register_first : AppCompatActivity() {

    // 뷰 변수 선언
    private lateinit var titleTextView: TextView
    private lateinit var option1RadioButton: RadioButton
    private lateinit var option2RadioButton: RadioButton
    private lateinit var radioGroup: RadioGroup
    private lateinit var nextButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.register_first)
        super.onCreate(savedInstanceState)

        // 1. 모든 뷰 초기화
        radioGroup = findViewById(R.id.radioGroupOptions)
        titleTextView = findViewById(R.id.title)
        option1RadioButton = findViewById(R.id.radioOption1)
        option2RadioButton = findViewById(R.id.radioOption2)

        val footerRootView: View? = findViewById(R.id.next1_button)
        footerRootView?.let { root ->
            nextButton = root.findViewById(R.id.footer_button_bg)

            // 2. Next Button 클릭 리스너 설정
            nextButton.setOnClickListener {
                handleNextButtonClick()
            }
        }

        // 3. RadioGroup 선택 리스너 설정 (선택 값만 갱신)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // 💡 memberType에 값 저장
            memberType = when (checkedId) {
                R.id.radioOption1 -> 1
                R.id.radioOption2 -> 2
                else -> null
            }
            Log.d("RadioSelect", "선택 값 갱신: $memberType, 상태: $memberBool")
        }
    }

    override fun onRestart() {
        super.onRestart()

        // 1. 상태 변수 초기화
        memberBool = false
        memberType = null
        Log.d("Lifecycle", "memberBool, memberType 초기화됨 (onRestart)")

        // 2. 텍스트 초기화 (옵션 2 선택 후 텍스트가 변경된 상태였을 경우)
        try {
            titleTextView.text = getString(resources.getIdentifier("reg_first_title", "string", packageName))
            option1RadioButton.text = getString(resources.getIdentifier("left_option", "string", packageName))
            option2RadioButton.text = getString(resources.getIdentifier("right_option", "string", packageName))
        } catch (e: Exception) {
            Log.e("ResourceError", "Failed to load string resources onRestart", e)
            titleTextView.text = "회원 종류를 선택해 주세요."
            option1RadioButton.text = "트레이너 (1)" // 트레이너 (1)
            option2RadioButton.text = "헬스장 회원 (2/3)" // 헬스장 회원 (2/3)
        }

        // 3. 라디오 버튼 선택도 초기화 (혹시 모를 잔여 선택 방지)
        radioGroup.clearCheck()
    }

    /**
     * Next 버튼 클릭 이벤트 처리
     */
    private fun handleNextButtonClick() {
        // 💡 memberBool을 기준으로 로직 분기
        when (memberBool) {

            // A. 텍스트가 변경되지 않은 첫 번째 단계 (회원 종류 선택)
            false -> {
                when (memberType) {
                    1 -> {
                        // 옵션 1 선택: 트레이너 (최종 값 1) -> 다음 화면 바로 이동
                        // 트레이너는 세부 유형이 없다고 가정하고 바로 이동합니다.
                        startRegisterScndActivity(memberType)
                    }
                    2 -> {
                        // 옵션 2 선택: 헬스장 회원 (PT 회원 / 개인 회원) -> 텍스트 변경 및 상태 전환
                        changeTextAndResetOptions()
                    }
                    else -> {
                        // 아무것도 선택 안 됨
                        Toast.makeText(this, "옵션을 선택해 주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // B. 텍스트가 변경된 두 번째 단계 (헬스장 회원 세부 유형 선택)
            true -> {
                if (memberType != null) {
                    // 🚨 수정: 헬스장 회원 세부 유형 값 조정
                    // 1 (내부) -> 2 (PT 회원)
                    // 2 (내부) -> 3 (개인 회원)
                    val finalMemberTypeForClient = memberType!! + 1
                    startRegisterScndActivity(finalMemberTypeForClient)
                } else {
                    Toast.makeText(this, "새로운 옵션을 다시 선택해 주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * 옵션 2 선택 시 텍스트를 변경하고 라디오 버튼 선택을 초기화하는 함수
     */
    private fun changeTextAndResetOptions() {
        // 1. 텍스트 변경
        try {
            titleTextView.text = getString(resources.getIdentifier("reg_first_title1", "string", packageName))
            option1RadioButton.text = getString(resources.getIdentifier("left_option1", "string", packageName))
            option2RadioButton.text = getString(resources.getIdentifier("right_option1", "string", packageName))
        } catch (e: Exception) {
            Log.e("ResourceError", "Failed to load string resources changeText", e)
            titleTextView.text = "헬스장 회원 세부 유형을 선택해 주세요."
            option1RadioButton.text = "PT 회원 (2)" // PT 회원 (2)
            option2RadioButton.text = "개인 회원 (3)" // 개인 회원 (3)
        }

        // 2. 라디오 버튼 선택 초기화
        radioGroup.clearCheck()

        // 3. 상태 변수 및 값 초기화
        memberBool = true
        memberType = null

        Toast.makeText(this, "옵션이 변경되었습니다. 다시 선택해 주세요.", Toast.LENGTH_LONG).show()
    }

    /**
     * RegisterScndActivity로 이동하며 선택된 값을 전달하는 함수
     */
    private fun startRegisterScndActivity(value: Int?) {
        val intent = Intent(this, RegisterScndActivity::class.java)

        if (value != null) {
            intent.putExtra("SELECTED_OPTION_VALUE", value)
            Log.d("Action", "RegisterScndActivity로 이동, 최종 값: $value")
            Toast.makeText(this, "다음 화면 이동. MemberType: $value", Toast.LENGTH_SHORT).show()
        }

        startActivity(intent)
    }
}