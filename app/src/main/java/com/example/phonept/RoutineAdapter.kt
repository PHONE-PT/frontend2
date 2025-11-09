package com.example.phonept

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * RoutineCard 데이터를 RecyclerView에 표시하는 어댑터입니다.
 */
class RoutineAdapter(private val routineList: ArrayList<RoutineCard>) :
    RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    /**
     * ViewHolder: item_routine_card.xml 내부의 뷰들을 보관하는 객체입니다.
     */
    class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val memberNameTextView: TextView = itemView.findViewById(R.id.tv_member_name_card)
        // ... 나중에 요일별 TextView(□)들도 여기서 찾아서 데이터를 설정할 수 있습니다.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        // item_routine_card.xml 레이아웃을 inflate(객체화)합니다.
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_routine_card, parent, false)
        return RoutineViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        // 현재 위치(position)에 해당하는 데이터를 가져와 뷰에 설정합니다.
        val currentItem = routineList[position]
        holder.memberNameTextView.text = currentItem.memberName
    }

    override fun getItemCount(): Int {
        // 리스트에 있는 아이템의 총 개수를 반환합니다.
        return routineList.size
    }
}