package com.example.phonept

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView // TextView를 사용하므로 import 추가
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View // View를 사용하므로 import 추가

/**
 * 트레이너 홈 화면 액티비티
 * - 루틴 목록을 표시하고, 루틴 등록 버튼 클릭 시 편집 화면으로 이동합니다.
 */
class TrainerHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trainer_home) // trainer_home.xml 레이아웃 사용

        // 1. RecyclerView 설정 (더미 데이터 사용)
        setupRecyclerView()

        // 2. '루틴 등록하기' 버튼 클릭 이벤트 설정
        val btnRegisterRoutine: Button = findViewById(R.id.btn_register_routine)
        btnRegisterRoutine.setOnClickListener {
            // TrainerEditActivity로 이동하는 Intent 생성
            val intent = Intent(this, TrainerEditActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * 루틴 목록 RecyclerView를 설정하고 더미 어댑터를 연결합니다.
     */
    private fun setupRecyclerView() {
        // trainer_home.xml에서 rv_routine_list ID를 사용한다고 가정
        val rvRoutineList: RecyclerView = findViewById(R.id.rv_routine_list)
        // 가로 스크롤 레이아웃 매니저 설정
        rvRoutineList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // 더미 루틴 데이터 목록 (회원 이름)
        val dummyRoutines = listOf("김철수", "이영희", "박민준", "최소미")

        // RecyclerView에 어댑터 연결
        rvRoutineList.adapter = RoutineCardAdapter(dummyRoutines)
    }

    /**
     * RecyclerView의 각 루틴 카드(item_routine_card.xml)에 대한 뷰 홀더
     */
    class RoutineCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // 회원 이름 텍스트 뷰
        val tvMemberNameCard: TextView = view.findViewById(R.id.tv_member_name_card)

        // 루틴 박스 텍스트 뷰 (예시로 첫 번째 행의 월요일, 수요일, 금요일만 참조)
        val tvBox1Mon: TextView = view.findViewById(R.id.tv_box1_mon)
        val tvBox1Wed: TextView = view.findViewById(R.id.tv_box1_wed)
        val tvBox1Fri: TextView = view.findViewById(R.id.tv_box1_fri)

        // 두 번째 행의 화요일 박스
        val tvBox2Tue: TextView = view.findViewById(R.id.tv_box2_tue)
    }

    /**
     * RecyclerView 어댑터
     */
    class RoutineCardAdapter(private val routines: List<String>) :
        RecyclerView.Adapter<RoutineCardViewHolder>() {

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): RoutineCardViewHolder {
            val view = android.view.LayoutInflater.from(parent.context)
                .inflate(R.layout.item_routine_card, parent, false)
            return RoutineCardViewHolder(view)
        }

        override fun onBindViewHolder(holder: RoutineCardViewHolder, position: Int) {
            val memberName = routines[position]

            // 1. 회원 이름 설정 (예: "김철수 회원")
            holder.tvMemberNameCard.text = "$memberName 회원"

            // 2. 루틴 박스에 더미 운동 표시 (예: "■" 또는 "●"로 체크 표시)
            // 첫 번째 회원은 월, 수, 금 루틴이 있다고 가정
            if (position == 0) {
                holder.tvBox1Mon.text = "●" // 체크
                holder.tvBox1Wed.text = "●" // 체크
                holder.tvBox1Fri.text = "●" // 체크
                holder.tvBox2Tue.text = "□" // 미체크
            } else if (position == 1) {
                // 두 번째 회원은 화요일에 루틴이 있다고 가정
                holder.tvBox1Mon.text = "□"
                holder.tvBox1Wed.text = "□"
                holder.tvBox1Fri.text = "□"
                holder.tvBox2Tue.text = "●"
            } else {
                // 나머지 회원은 모두 미체크
                holder.tvBox1Mon.text = "□"
                holder.tvBox1Wed.text = "□"
                holder.tvBox1Fri.text = "□"
                holder.tvBox2Tue.text = "□"
            }
            // 실제 앱에서는 루틴 데이터를 불러와서 요일별로 해당 TextView의 텍스트를 업데이트해야 합니다.
        }

        override fun getItemCount() = routines.size
    }
}