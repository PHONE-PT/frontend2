package com.example.phonept

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.phonept.databinding.RegisterThirdBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore

class RegisterThirdActivity : AppCompatActivity() {

    private val TAG = "RegisterThirdActivity"
    private lateinit var binding: RegisterThirdBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private var email: String = ""
    private var passwordValue: String = ""
    private var userRole: String = "" // 💡 [수정] 사용자 역할(trainer/member) 저장을 위한 변수 추가

    companion object {
        // 🔑 수정: RegisterScndActivity와 동일한 키를 사용하도록 변경
        const val EXTRA_EMAIL = "EMAIL_INPUT"
        const val EXTRA_PASSWORD = "PASSWORD_INPUT"
        const val USER_ROLE_KEY = "USER_ROLE" // 💡 [수정] User Role Key 추가
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Binding 초기화
        binding = RegisterThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Firebase 인스턴스 초기화
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // 3. Intent 데이터 추출 (이제 정확한 키로 데이터를 가져옵니다)
        email = intent.getStringExtra(EXTRA_EMAIL) ?: ""
        passwordValue = intent.getStringExtra(EXTRA_PASSWORD) ?: ""
        // 💡 [수정] 이전 화면에서 전달받은 사용자 역할(Role)을 추출합니다.
        userRole = intent.getStringExtra(USER_ROLE_KEY) ?: "member"

        // 🚨 테스트용 로그: 제대로 가져왔는지 확인
        Log.d(TAG, "Received Email: $email")
        Log.d(TAG, "Received Role: $userRole") // 💡 [수정] Role 로그 추가
        Log.d(TAG, "Received Password: ${passwordValue.take(1)}***${passwordValue.takeLast(1)}")


        // 4. 운동 목표 및 난이도 선택 리스너 추가
        binding.goalSelectText.setOnClickListener {
            showGoalSelectionDialog()
        }

        binding.difficultySelectText.setOnClickListener {
            showDifficultySelectionDialog()
        }

        // 5. 회원가입 완료 버튼 리스너
        binding.next3Button.setOnClickListener {
            Log.e(TAG, "회원가입 완료 버튼 클릭됨")
            completeRegistration()
        }
    }

    /**
     * 운동 목표를 선택할 수 있는 다이얼로그를 표시합니다.
     */
    private fun showGoalSelectionDialog() {
        val goals = arrayOf("체중 감량 (다이어트)", "근력 증가 (벌크업)", "체력 향상", "재활/자세 교정")
        AlertDialog.Builder(this)
            .setTitle("운동 목표 선택")
            .setItems(goals) { _, which ->
                binding.goalSelectText.text = goals[which]
            }
            .setNegativeButton("취소", null)
            .show()
    }

    /**
     * 운동 난이도를 선택할 수 있는 다이얼로그를 표시합니다.
     */
    private fun showDifficultySelectionDialog() {
        val difficulties = arrayOf("초급 (운동 경험 없음)", "중급 (규칙적인 운동 중)", "고급 (매우 강도 높은 운동 가능)")
        AlertDialog.Builder(this)
            .setTitle("운동 난이도 선택")
            .setItems(difficulties) { _, which ->
                binding.difficultySelectText.text = difficulties[which]
            }
            .setNegativeButton("취소", null)
            .show()
    }


    private fun completeRegistration() {

        // 1. Firebase Auth를 위한 필수 입력값 검증
        if (email.isBlank() || passwordValue.isBlank()) {
            Log.e(TAG, "Auth Error: Email or password is blank. (Intent Data Missing)")
            Toast.makeText(this, "이메일 또는 비밀번호 정보가 누락되었습니다. 이전 단계로 돌아가 확인해 주세요.", Toast.LENGTH_LONG).show()
            return
        }

        // 2. 추가 입력 데이터 추출 및 유효성 검사
        val heightStr = binding.heightEdit.text.toString().trim()
        val weightStr = binding.weightEdit.text.toString().trim()
        val goal = binding.goalSelectText.text.toString().trim()
        val difficulty = binding.difficultySelectText.text.toString().trim()

        val height = heightStr.toIntOrNull()
        val weight = weightStr.toIntOrNull()

        // 3. 키, 몸무게, 목표, 난이도 데이터 유효성 검사
        // "목표 선택"은 XML에서 설정된 힌트 텍스트일 가능성이 높아 추가했습니다.
        if (height == null || weight == null || goal.isEmpty() || difficulty.isEmpty() || goal == "목표 선택" || difficulty == "난이도 선택") {
            Toast.makeText(this, "키, 몸무게, 운동 목표, 난이도를 모두 올바르게 입력해 주세요.", Toast.LENGTH_LONG).show()
            return
        }

        // 4단계: Firebase Authentication으로 계정 생성
        auth.createUserWithEmailAndPassword(email, passwordValue)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 계정 생성 성공
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    user?.let {
                        // 5단계: Firestore에 사용자 프로필 저장
                        // 💡 [수정] userRole을 인수로 전달합니다.
                        saveUserProfileToFirestore(it.uid, height, weight, goal, difficulty, userRole)
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK) // 이전 화면 스택 정리
                    startActivity(intent)
                    finish()
                } else {
                    // 계정 생성 실패
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)

                    // 사용자에게 구체적인 실패 메시지 표시
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthWeakPasswordException -> "비밀번호가 너무 약합니다. 6자 이상으로 설정해 주세요."
                        is FirebaseAuthInvalidCredentialsException -> "이메일 형식이 잘못되었거나 유효하지 않습니다."
                        is FirebaseAuthUserCollisionException -> "이미 가입된 이메일 계정입니다."
                        else -> "회원가입 실패: ${task.exception?.localizedMessage}"
                    }

                    Toast.makeText(baseContext, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }

    /**
     * Firestore에 사용자 프로필을 저장합니다.
     * @param role RegisterFirstActivity에서 선택된 최종 사용자 역할 ("trainer" 또는 "member")
     */
    private fun saveUserProfileToFirestore(uid: String, height: Int, weight: Int, goal: String, difficulty: String, role: String) { // 💡 [수정] role 인수를 받도록 변경
        val userProfile = hashMapOf(
            "email" to email,
            "height" to height,
            "weight" to weight,
            "goal" to goal,
            "difficulty" to difficulty,
            "role" to role, // 💡 [수정] 전달받은 role 값을 사용하여 Firestore에 저장합니다.
            "createdAt" to System.currentTimeMillis()
        )

        db.collection("users").document(uid)
            .set(userProfile)
            .addOnSuccessListener {
                Log.d(TAG, "User profile successfully written! Role: $role")
                Toast.makeText(this, "회원가입 및 프로필 저장이 완료되었습니다!", Toast.LENGTH_LONG).show()

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing user profile document", e)
                Toast.makeText(this, "회원가입은 되었으나 프로필 저장에 실패했습니다: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }
}