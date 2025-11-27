package com.example.phonept

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // 💡 레이아웃에 존재하는 이메일과 비밀번호 입력 필드를 가정합니다.
    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 1. Firebase 인스턴스 초기화
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // 2. 뷰 초기화 (activity_main.xml에 해당 ID가 있다고 가정)
        // 🚨 이메일/비밀번호 입력 필드 ID가 다르면 XML에 맞게 수정해야 합니다.
        emailEdit = findViewById(R.id.email_edit)
        passwordEdit = findViewById(R.id.pwd_edit)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 3. 회원가입 버튼 클릭 리스너
        val regiButton: Button = findViewById(R.id.regi_button)
        regiButton.setOnClickListener {
            val intent = Intent(this, register_first::class.java)
            startActivity(intent)
        }

        // 4. 로그인 버튼 클릭 시 로그인 처리 함수 호출
        val logButton: Button = findViewById(R.id.log_button)
        logButton.setOnClickListener {
            performLogin()
        }

        // 5. 앱 시작 시 이미 로그인된 사용자가 있는지 확인
        //checkLoggedInUser()
    }

    /**
     * 이미 로그인된 사용자가 있다면 Firestore 역할을 확인하고 홈 화면으로 이동합니다.

    private fun checkLoggedInUser() {
        if (auth.currentUser != null) {
            Log.d(TAG, "User already logged in. Checking role.")
            // 로그인 상태라면 사용자 UID를 사용하여 Firestore에서 역할을 확인합니다.
            redirectToHome(auth.currentUser!!.uid)
        }
    }
    */
    /**
     * 이메일과 비밀번호를 사용하여 Firebase 로그인을 시도합니다.
     */
    private fun performLogin() {
        val email = emailEdit.text.toString().trim()
        val password = passwordEdit.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호를 모두 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // Firebase Authentication: 이메일/비밀번호로 로그인
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    user?.let {
                        // 성공 후, Firestore에서 사용자 역할 확인
                        redirectToHome(it.uid)
                    }
                } else {
                    // 로그인 실패
                    Log.w(TAG, "signInWithEmail:failure", task.exception)

                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "존재하지 않는 이메일 계정입니다."
                        is FirebaseAuthInvalidCredentialsException -> "이메일 또는 비밀번호가 올바르지 않습니다."
                        else -> "로그인 실패: ${task.exception?.localizedMessage}"
                    }
                    Toast.makeText(baseContext, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }

    /**
     * Firestore에서 사용자 역할을 확인하고 적절한 홈 화면으로 이동합니다.
     */
    private fun redirectToHome(uid: String) {
        // Firestore 'users' 컬렉션에서 해당 UID의 문서를 가져옵니다.
        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // 문서가 존재하면 'role' 필드 값을 가져옵니다.
                    val role = document.getString("role")

                    val intent = when (role) {
                        "Trainer" -> {
                            Log.d(TAG, "User role is Trainer. Navigating to TrainerHomeActivity.")
                            Intent(this, TrainerHomeActivity::class.java)
                        }
                        "Member" -> {
                            Log.d(TAG, "User role is Member. Navigating to MemberHomeActivity.")
                            Intent(this, MemberHomeActivity::class.java)
                        }
                        else -> {
                            Log.e(TAG, "User role not found or unknown: $role. Defaulting to MemberHomeActivity.")
                            Toast.makeText(this, "사용자 역할 정보가 불분명합니다. 일반 회원 홈으로 이동합니다.", Toast.LENGTH_LONG).show()
                            Intent(this, MemberHomeActivity::class.java)
                        }
                    }
                    startActivity(intent)
                    finish() // MainActivity를 스택에서 제거
                } else {
                    Log.e(TAG, "User document not found for UID: $uid")
                    Toast.makeText(this, "사용자 정보를 찾을 수 없습니다. 다시 로그인해 주세요.", Toast.LENGTH_LONG).show()
                    auth.signOut() // 문서가 없으면 로그아웃 처리하여 다시 로그인하게 함
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching user document: $exception")
                Toast.makeText(this, "로그인 후 사용자 정보 확인 중 오류 발생. 네트워크 상태를 확인하세요.", Toast.LENGTH_LONG).show()
            }
    }
}