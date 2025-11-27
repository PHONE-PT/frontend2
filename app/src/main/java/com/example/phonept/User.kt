data class User(
    val email: String = "",
    val name: String = "",
    val nick: String = "",
    val pwd: String = "",
    val role: String = "", // 예: "trainer", "member"
    val height: Int = 0,
    val weight: Int = 0,
    val trainerId: String? = null,
    val createdAt: com.google.firebase.Timestamp? = null
    // ViewModel 코드에서 Timestamp를 사용하므로 필요
)