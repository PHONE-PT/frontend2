package com.example.phonept

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager // 변경: GridLayoutManager를 사용하도록 import
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 * 트레이너 홈 화면 액티비티
 * - 루틴 목록을 표시하고, 루틴 등록 버튼 클릭 시 편집 화면으로 이동합니다.
 */
class TrainerHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 이전 단계에서 저장된 레이아웃 파일 (trainer_home_root.xml)을 사용한다고 가정합니다.
        setContentView(R.layout.trainer_home)

        // 1. RecyclerView 설정 (더미 데이터 사용)
        setupRecyclerView()

        // 2. '루틴 등록하기' 버튼 클릭 이벤트 설정
        val btnRegisterRoutine: Button = findViewById(R.id.btn_register_routine)
        btnRegisterRoutine.setOnClickListener {
            // TrainerEditActivity로 이동하는 Intent 생성 (TrainerEditActivity는 별도로 정의되어야 합니다.)
            val intent = Intent(this, TrainerEditActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * 루틴 목록 RecyclerView를 설정하고 더미 어댑터를 연결합니다.
     * XML에서 설정한 2열 그리드 레이아웃에 맞게 GridLayoutManager를 사용합니다.
     */
    private fun setupRecyclerView() {
        val rvRoutineList: RecyclerView = findViewById(R.id.rv_routine_list)

        // 🚨 수정된 부분: 2열 그리드 레이아웃 매니저 설정
        // GridLayoutManager(context, spanCount=2)를 사용하여 한 줄에 2개씩 배치합니다.
        rvRoutineList.layoutManager = GridLayoutManager(this, 2)

        // 더미 루틴 데이터 목록 (회원 이름) - 그리드 확인을 위해 항목 수를 늘렸습니다.
        val dummyRoutines = listOf("김철수", "이영희", "박민준", "최소미", "정하늘", "강바다", "윤지호")

        // RecyclerView에 어댑터 연결
        rvRoutineList.adapter = RoutineCardAdapter(dummyRoutines)
    }

    /**
     * RecyclerView의 각 루틴 카드(item_routine_card.xml)에 대한 뷰 홀더
     * (레이아웃 ID는 item_routine_card.xml에 존재한다고 가정하고 유지합니다.)
     */
    class RoutineCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // 회원 이름 텍스트 뷰
        val tvMemberNameCard: TextView = view.findViewById(R.id.tv_member_name_card)

        // 루틴 박스 텍스트 뷰 (예시로 월, 수, 금, 화만 참조)
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

            // 1. 회원 이름 설정
            holder.tvMemberNameCard.text = "$memberName 회원"

            // 2. 루틴 박스에 더미 운동 표시
            // 포지션에 따라 다른 루틴 상태를 표시하여 그리드 레이아웃을 확인합니다.
            when (position % 4) {
                0 -> { // 월, 수, 금 루틴
                    holder.tvBox1Mon.text = "●"
                    holder.tvBox1Wed.text = "●"
                    holder.tvBox1Fri.text = "●"
                    holder.tvBox2Tue.text = "□"
                }
                1 -> { // 화, 금 루틴
                    holder.tvBox1Mon.text = "□"
                    holder.tvBox1Wed.text = "□"
                    holder.tvBox1Fri.text = "●"
                    holder.tvBox2Tue.text = "●"
                }
                2 -> { // 월, 화 루틴
                    holder.tvBox1Mon.text = "●"
                    holder.tvBox1Wed.text = "□"
                    holder.tvBox1Fri.text = "□"
                    holder.tvBox2Tue.text = "●"
                }
                else -> { // 모두 미체크
                    holder.tvBox1Mon.text = "□"
                    holder.tvBox1Wed.text = "□"
                    holder.tvBox1Fri.text = "□"
                    holder.tvBox2Tue.text = "□"
                }
            }
        }

        override fun getItemCount() = routines.size
    }
}