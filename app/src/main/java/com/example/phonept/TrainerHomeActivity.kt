package com.example.phonept

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager

class TrainerHomeActivity : AppCompatActivity() {

    // 1. 데이터 리스트와 어댑터 변수 선언
    private lateinit var routineRecyclerView: RecyclerView
    private lateinit var routineAdapter: RoutineAdapter
    private val routineDataList = ArrayList<RoutineCard>()
    private var memberCount = 1 // 회원 번호 카운트용

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.trainer_home)

        // 시스템 바 Insets 설정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.trainer_home_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- RecyclerView 설정 ---

        // 2. RecyclerView 참조
        routineRecyclerView = findViewById(R.id.rv_routine_list)

        // 3. GridLayoutManager 설정 (2열, 세로 스크롤)
        val layoutManager = GridLayoutManager(this, 2)
        routineRecyclerView.layoutManager = layoutManager

        // 4. 어댑터 생성 및 RecyclerView에 연결 (이 부분이 핵심!)
        routineAdapter = RoutineAdapter(routineDataList)
        routineRecyclerView.adapter = routineAdapter

        // --- 버튼 기능 구현 ---
        val registerButton: AppCompatButton = findViewById(R.id.btn_register_routine)

        registerButton.setOnClickListener {
            // "루틴 등록하기" 버튼 클릭 시
            addNewRoutineCard()
        }

        // (선택 사항) 초기 테스트용 데이터 추가
        addInitialData()
    }

    private fun addInitialData() {
        // 앱 시작 시 보여줄 기본 데이터 (테스트용)
        routineDataList.add(RoutineCard("OOO 회원 1"))
        routineDataList.add(RoutineCard("OOO 회원 2"))
        routineAdapter.notifyDataSetChanged() // 어댑터에 데이터 변경 알림
        memberCount = 3
    }

    private fun addNewRoutineCard() {
        // 5. 새 루틴 카드를 리스트에 추가
        routineDataList.add(RoutineCard("OOO 회원 $memberCount"))
        memberCount++

        // 6. 어댑터에 데이터가 추가되었음을 알림 (새 아이템이 화면에 나타남)
        routineAdapter.notifyItemInserted(routineDataList.size - 1)

        // (선택 사항) 새 아이템이 추가된 위치로 스크롤
        routineRecyclerView.scrollToPosition(routineDataList.size - 1)
    }
}