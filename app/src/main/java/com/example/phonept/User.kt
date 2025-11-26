data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val role: String = "", // 예: "trainer", "member"
    val trainerId: String? = null,
    val createdAt: com.google.firebase.Timestamp? = null
    // ViewModel 코드에서 Timestamp를 사용하므로 필요
)