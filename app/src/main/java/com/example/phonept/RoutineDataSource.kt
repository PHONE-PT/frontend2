package com.example.phonept

import java.util.concurrent.ConcurrentLinkedQueue

/**
 * 루틴 데이터를 관리하는 싱글톤 객체 (DB 역할을 시뮬레이션)
 * 앱이 재시작될 때까지 데이터를 유지하며, 모든 Activity에서 접근 가능합니다.
 */
object RoutineDataSource {
    private val routinesQueue: ConcurrentLinkedQueue<Routine> = ConcurrentLinkedQueue()

    init {
        // 초기 더미 데이터 (새로운 Routine 데이터 구조에 맞게 업데이트)
        routinesQueue.add(
            Routine(
                memberName = "김철수 회원",
                dailyRoutines = mapOf(
                    "월" to listOf("스쿼트", "데드리프트"),
                    "수" to listOf("벤치 프레스", "OHP", "플랭크"),
                    "금" to listOf("러닝")
                ),
                registrationTime = System.currentTimeMillis() - 86400000
            )
        )
        routinesQueue.add(
            Routine(
                memberName = "이영희 회원",
                dailyRoutines = mapOf(
                    "화" to listOf("이두 컬", "삼두 푸쉬다운", "휴식/스트레칭"),
                    "목" to listOf("런지", "숄더 프레스")
                ),
                registrationTime = System.currentTimeMillis() - 3600000
            )
        )
    }

    /**
     * 새로운 루틴을 목록의 맨 앞에 추가합니다.
     */
    fun addRoutine(routine: Routine) {
        // 새로운 루틴을 목록의 맨 앞에 추가하기 위해 임시 목록을 사용
        val tempList = mutableListOf(routine)
        tempList.addAll(routinesQueue)

        // 큐를 비우고 새 데이터를 순서대로 추가
        routinesQueue.clear()
        routinesQueue.addAll(tempList)
    }

    /**
     * 현재 저장된 모든 루틴 목록을 반환합니다. (최신순)
     */
    fun getAllRoutines(): List<Routine> {
        // 큐의 순서대로 리스트를 반환
        return routinesQueue.toList()
    }
}