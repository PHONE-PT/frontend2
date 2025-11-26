package com.phonept.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import com.phonept.app.data.model.User
import kotlinx.coroutines.tasks.await
import java.util.Date

class AuthRepository {

    // Firebase 인스턴스 초기화
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    /**
     * 현재 로그인된 사용자를 가져옵니다.
     * @return Result<User>: 성공 시 User 객체, 실패 시 Exception.
     */
    suspend fun getCurrentUser(): Result<User> = runCatching {
        // 1. Firebase Auth에서 현재 사용자(FirebaseUser)를 가져옵니다.
        val firebaseUser = auth.currentUser ?: throw IllegalStateException("No authenticated user.")

        // 2. 해당 UID로 Firestore에서 추가 사용자 정보를 조회합니다.
        val documentSnapshot = usersCollection.document(firebaseUser.uid).get().await()

        // 3. Firestore 데이터를 User 모델로 변환합니다.
        documentSnapshot.toObject(User::class.java)
            ?: throw IllegalStateException("User profile not found in Firestore.")
    }

    /**
     * 이메일과 비밀번호로 로그인합니다.
     * @return Result<User>: 성공 시 User 객체, 실패 시 Exception.
     */
    suspend fun signIn(email: String, password: String): Result<User> = runCatching {
        // 1. Firebase Auth 로그인 실행
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw IllegalStateException("Login failed: UID is null.")

        // 2. 로그인 성공 후 Firestore에서 사용자 정보를 가져옵니다.
        val documentSnapshot = usersCollection.document(uid).get().await()

        // 3. Firestore 데이터를 User 모델로 변환합니다.
        documentSnapshot.toObject(User::class.java)
            ?: throw IllegalStateException("User profile not found in Firestore after sign in.")
    }

    /**
     * 이메일, 비밀번호 및 추가 정보로 회원가입합니다.
     * @return Result<User>: 성공 시 User 객체, 실패 시 Exception.
     */
    suspend fun signUp(
        email: String,
        password: String,
        name: String,
        phone: String,
        role: String,
        trainerId: String? = null
    ): Result<User> = runCatching {
        // 1. Firebase Auth로 새 계정 생성
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw IllegalStateException("Sign up failed: UID is null.")

        // 2. User 모델 생성
        val newUser = User(
            uid = uid,
            email = email,
            name = name,
            phone = phone,
            role = role,
            trainerId = trainerId,
            createdAt = Timestamp(Date())
        )

        // 3. Firestore에 사용자 추가 정보 저장
        usersCollection.document(uid).set(newUser).await()

        // 4. 생성된 User 객체 반환
        newUser
    }

    /**
     * 로그아웃합니다.
     */
    fun signOut() {
        auth.signOut()
    }

    /**
     * 비밀번호 재설정 이메일을 보냅니다.
     * @return Result<Unit>: 성공 시 Unit, 실패 시 Exception.
     */
    suspend fun resetPassword(email: String): Result<Unit> = runCatching {
        auth.sendPasswordResetEmail(email).await()
        Unit // 성공 시 Unit 반환
    }
}