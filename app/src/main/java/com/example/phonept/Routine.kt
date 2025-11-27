package com.example.phonept

/**
 * 루틴 항목의 데이터를 담는 데이터 클래스
 * @param memberName 루틴을 등록하는 회원 이름
 * @param dailyRoutines 요일별 루틴 목록 (Key: 요일 이름, Value: 해당 요일 운동 목록)
 * @param registrationTime 루틴이 등록된 시간 (정렬 기준)
 */
data class Routine(
    val memberName: String,
    // Key: "월", "화", "수", "목", "금"
    // Value: ["벤치 프레스", "스쿼트", "러닝", ...]
    val dailyRoutines: Map<String, List<String>>,
    val registrationTime: Long = System.currentTimeMillis()
)