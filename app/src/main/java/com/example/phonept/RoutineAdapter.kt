package com.example.phonept

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.phonept.R
import com.example.phonept.Routine

/**
 * 트레이너 홈 화면의 루틴 목록을 위한 RecyclerView Adapter
 * 새로운 레이아웃(미니 캘린더 형태)에 맞게 데이터 바인딩 로직을 수정했습니다.
 */
class RoutineAdapter(private var routines: List<Routine>) :
    RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    class RoutineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // 회원 이름 (카드뷰 상단)
        val memberName: TextView = view.findViewById(R.id.tv_member_name_card)

        // 루틴 박스 15개와 더보기 표시를 위한 TextView들을 초기화
        val dayViews = mutableMapOf<String, List<TextView>>()
        val moreIndicator: TextView = view.findViewById(R.id.tv_more_routine_indicator)

        init {
            // 요일별 TextView ID를 찾아 View 객체로 저장합니다.
            val days = listOf("월", "화", "수", "목", "금")

            // 레이아웃에 정의된 3개 행에 해당하는 ID 목록
            val idsMon = listOf(R.id.tv_box1_mon, R.id.tv_box2_mon, R.id.tv_box3_mon)
            val idsTue = listOf(R.id.tv_box1_tue, R.id.tv_box2_tue, R.id.tv_box3_tue)
            val idsWed = listOf(R.id.tv_box1_wed, R.id.tv_box2_wed, R.id.tv_box3_wed)
            val idsThu = listOf(R.id.tv_box1_thu, R.id.tv_box2_thu, R.id.tv_box3_thu)
            val idsFri = listOf(R.id.tv_box1_fri, R.id.tv_box2_fri, R.id.tv_box3_fri)

            dayViews["월"] = idsMon.map { view.findViewById(it) }
            dayViews["화"] = idsTue.map { view.findViewById(it) }
            dayViews["수"] = idsWed.map { view.findViewById(it) }
            dayViews["목"] = idsThu.map { view.findViewById(it) }
            dayViews["금"] = idsFri.map { view.findViewById(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        // 기존의 routine_item.xml을 새로운 레이아웃으로 교체
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_routine_card, parent, false)
        return RoutineViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val routine = routines[position]
        holder.memberName.text = routine.memberName

        var hasMoreRoutines = false
        val allDays = listOf("월", "화", "수", "목", "금")
        val maxRows = 3 // 레이아웃에 표시되는 최대 행 수

        // 1. 요일별 루틴 박스 업데이트
        allDays.forEach { day ->
            // 해당 요일의 루틴 리스트
            val exercises = routine.dailyRoutines[day] ?: emptyList()
            // 해당 요일의 TextView 리스트 (3개)
            val textViews = holder.dayViews[day] ?: emptyList()

            // 1-1. 각 행(박스)에 운동을 할당
            for (i in 0 until maxRows) {
                val tv = textViews.getOrNull(i)

                if (tv != null) {
                    if (i < exercises.size) {
                        // 해당 행에 운동 항목 할당
                        tv.text = exercises[i]
                        tv.textSize = 10f // 루틴 텍스트는 작게
                        tv.visibility = View.VISIBLE
                        tv.setTextColor(tv.context.getColor(android.R.color.black))
                    } else {
                        // 남은 행은 '□' 또는 공백으로 채움
                        tv.text = "" // 빈 문자열로 설정하여 깔끔하게 비움
                        tv.textSize = 18f
                        tv.visibility = View.VISIBLE
                    }
                }
            }

            // 1-2. 3개를 초과하는 루틴이 있는지 확인
            if (exercises.size > maxRows) {
                hasMoreRoutines = true
            }
        }

        // 2. '더보기' 표시 업데이트
        if (hasMoreRoutines) {
            holder.moreIndicator.text = "..." // 3개 초과 루틴이 있는 경우 '...' 표시
            holder.moreIndicator.visibility = View.VISIBLE
        } else {
            holder.moreIndicator.text = ""
            holder.moreIndicator.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int = routines.size

    /**
     * 외부에서 새로운 데이터 리스트를 받아와 RecyclerView를 업데이트하고 새로고침합니다.
     */
    fun updateRoutines(newRoutines: List<Routine>) {
        routines = newRoutines
        notifyDataSetChanged()
    }
}