package com.example.phonept

/**
 * RecyclerView의 각 항목(루틴 카드)을 나타내는 데이터 클래스입니다.
 * 지금은 회원 이름만 저장하지만, 나중에 요일별 루틴 데이터 등을 추가할 수 있습니다.
 */
data class RoutineCard(
    val memberName: String
    // 예: val isMonDone: Boolean, val isTueDone: Boolean 등...
)