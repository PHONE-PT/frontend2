package com.example.phonept

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button

/**
 * 트레이너 홈 화면 액티비티
 * - 등록된 루틴 목록을 RecyclerView로 표시하고, 편집 화면으로 이동합니다.
 */
class TrainerHomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var routineAdapter: RoutineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trainer_home)

        recyclerView = findViewById(R.id.rv_routine_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 초기 데이터 로드 및 어댑터 설정
        val initialRoutines = RoutineDataSource.getAllRoutines()
        routineAdapter = RoutineAdapter(initialRoutines)
        recyclerView.adapter = routineAdapter

        // 루틴 편집 화면으로 이동하는 버튼 설정
        val btnGoToEdit: Button = findViewById(R.id.btn_register_routine)
        btnGoToEdit.setOnClickListener {
            val intent = Intent(this, TrainerEditActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * 편집 화면에서 돌아올 때마다 onResume이 호출되어 데이터를 새로고침합니다.
     * 이를 통해 새로 추가된 루틴 항목이 즉시 화면에 반영됩니다.
     */
    override fun onResume() {
        super.onResume()
        // 데이터 소스에서 최신 데이터를 가져와 어댑터를 업데이트합니다.
        val updatedRoutines = RoutineDataSource.getAllRoutines()
        routineAdapter.updateRoutines(updatedRoutines)
    }
}