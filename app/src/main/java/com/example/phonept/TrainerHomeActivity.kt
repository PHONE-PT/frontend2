package com.example.phonept

import android.content.Intent // Intent 사용을 위한 import 추가
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
    // *주의*: 이 Activity의 컴파일을 위해 RoutineCard와 RoutineAdapter의 선언을 임시로 주석 처리하거나,
    // 해당 클래스가 프로젝트에 정의되어 있어야 합니다. 여기서는 기능을 위해 유지합니다.
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

        // --- RecyclerView 설정 (기존 코드 유지) ---
        routineRecyclerView = findViewById(R.id.rv_routine_list)
        val layoutManager = GridLayoutManager(this, 2)
        routineRecyclerView.layoutManager = layoutManager

        // *주의*: 이 줄이 에러를 발생시키지 않으려면 RoutineAdapter와 RoutineCard가 정의되어야 합니다.
        // 현재는 구현이 없는 상태입니다.
        // routineAdapter = RoutineAdapter(routineDataList)
        // routineRecyclerView.adapter = routineAdapter


        // --- 버튼 기능 구현: Navigation으로 변경 ---
        val registerButton: AppCompatButton = findViewById(R.id.btn_register_routine)

        registerButton.setOnClickListener {
            // 요청 사항: "루틴 등록하기" 버튼 클릭 시 TrainerEditActivity로 이동
            val intent = Intent(this, TrainerEditActivity::class.java)
            startActivity(intent)
        }

        // (선택 사항) 초기 테스트용 데이터 추가
        // addInitialData() // 데이터 클래스 누락으로 인해 실행 불가
    }

    // 루틴 등록/추가 관련 기존 로직 제거 (이제 Activity 전환을 담당)
    /*
    private fun addInitialData() { ... }
    private fun addNewRoutineCard() { ... }
    */
}