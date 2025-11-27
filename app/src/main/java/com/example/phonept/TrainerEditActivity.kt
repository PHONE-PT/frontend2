package com.example.phonept

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/**
 * 트레이너 루틴 편집/등록 화면 액티비티
 * - 루틴을 수정/등록하고, 완료 시 홈 화면으로 돌아갑니다.
 *
 * NOTE: 루틴 데이터 구조가 Map<String, List<String>>으로 변경됨에 따라,
 * 등록 로직을 수정했습니다.
 */
class TrainerEditActivity : AppCompatActivity() {

    // 기본 Placeholder 텍스트 정의
    private val DEFAULT_MEMBER_NAME = "OOO 회원"

    // 요일별 루틴 박스 ID 목록 정의 (onCreate 밖에서도 접근 가능하도록 멤버 변수로 정의)
    // TrainerEditActivity는 최대 7개 행까지 지원합니다.
    private val dayColumns = mapOf(
        "월" to listOf(R.id.tv_box1_mon, R.id.tv_box2_mon, R.id.tv_box3_mon, R.id.tv_box4_mon, R.id.tv_box5_mon, R.id.tv_box6_mon, R.id.tv_box7_mon),
        "화" to listOf(R.id.tv_box1_tue, R.id.tv_box2_tue, R.id.tv_box3_tue, R.id.tv_box4_tue, R.id.tv_box5_tue, R.id.tv_box6_tue, R.id.tv_box7_tue),
        "수" to listOf(R.id.tv_box1_wed, R.id.tv_box2_wed, R.id.tv_box3_wed, R.id.tv_box4_wed, R.id.tv_box5_wed, R.id.tv_box6_wed, R.id.tv_box7_wed),
        "목" to listOf(R.id.tv_box1_thu, R.id.tv_box2_thu, R.id.tv_box3_thu, R.id.tv_box4_thu, R.id.tv_box5_thu, R.id.tv_box6_thu, R.id.tv_box7_thu),
        "금" to listOf(R.id.tv_box1_fri, R.id.tv_box2_fri, R.id.tv_box3_fri, R.id.tv_box4_fri, R.id.tv_box5_fri, R.id.tv_box6_fri, R.id.tv_box7_fri)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trainer_edit)

        // 1. '회원 이름' TextView 설정 및 클릭 이벤트 추가
        val tvMemberNameCard: TextView = findViewById(R.id.tv_member_name_card)
        val memberList = arrayOf("홍길동 회원", "김철수 회원", "이영희 회원", "박민준 회원", "정하윤 회원")

        tvMemberNameCard.setOnClickListener {
            showMemberSelectionDialog(tvMemberNameCard, memberList)
        }

        // 2. '루틴 등록하기' 버튼 설정 (유효성 검사 로직 및 데이터 저장 추가)
        val btnRegisterRoutine: Button = findViewById(R.id.btn_register_routine)
        btnRegisterRoutine.setOnClickListener {
            // [유효성 검사 1] 회원 선택 여부 확인
            if (tvMemberNameCard.text.toString() == DEFAULT_MEMBER_NAME) {
                Toast.makeText(this, "먼저 'OOO 회원' 부분을 눌러 등록할 회원을 선택해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 1. 요일별 루틴 데이터 수집
            val dailyRoutinesMap = collectDailyRoutines()

            // [유효성 검사 2] 최소한 하나의 요일에 루틴이 선택되었는지 확인
            if (dailyRoutinesMap.isEmpty()) {
                Toast.makeText(this, "요일별로 등록할 운동을 하나 이상 선택해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 회원이 선택되고 운동이 있는 경우: 데이터 추출 및 등록 진행
            val memberName = tvMemberNameCard.text.toString()

            // 2. 새로운 Routine 객체를 생성하고 DataSource에 추가합니다.
            val routine = Routine(
                memberName = memberName,
                dailyRoutines = dailyRoutinesMap // Map<String, List<String>> 형식으로 저장
            )

            RoutineDataSource.addRoutine(routine)

            // 3. 등록 완료 메시지 표시 및 화면 종료
            Toast.makeText(this, "$memberName 님의 루틴이 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        // 3. '취소' 버튼 설정
        val btnCancelEdit: Button = findViewById(R.id.btn_cancel_edit)
        btnCancelEdit.setOnClickListener {
            Toast.makeText(this, "루틴 등록이 취소되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        // 4. 다중 선택 AlertDialog 기능 연결
        // 공통 운동 옵션 목록 (운동 이름만)
        val commonOptions = arrayOf(
            "벤치 프레스", "데드리프트", "스쿼트", "OHP", "트라이셉스 익스텐션", "플랭크",
            "러닝", "이두 컬", "푸쉬다운", "카프 레이즈", "휴식/스트레칭", "레터럴 레이즈", "친업", "로우"
        )

        // 클릭 리스너 연결 (해당 요일의 1행 박스에만 리스너를 연결하여 전체 열 업데이트)
        dayColumns.forEach { (day, ids) ->
            // 각 요일의 첫 번째 박스 (tv_box1_xxx)에 클릭 리스너 연결
            findViewById<TextView>(ids.first()).setOnClickListener {
                showRoutineSelectorDialog("${day}요일 운동 선택", commonOptions, ids)
            }
        }
    }

    /**
     * 모든 요일의 루틴 박스를 순회하여 Map<요일, List<운동명>> 형태로 데이터를 수집합니다.
     */
    private fun collectDailyRoutines(): Map<String, List<String>> {
        val dailyRoutinesMap = mutableMapOf<String, List<String>>()

        dayColumns.forEach { (day, ids) ->
            val exercisesForDay = mutableListOf<String>()

            // 해당 요일의 모든 박스를 순회
            ids.forEach { id ->
                val tv = findViewById<TextView>(id)
                // 텍스트에서 '●\n' 또는 '□' 등 UI 표시 문자를 제거하고 실제 운동 항목만 추출
                val itemText = tv.text.toString().replace("●\n", "").replace("□", "").trim()
                if (itemText.isNotEmpty()) {
                    exercisesForDay.add(itemText)
                }
            }

            // 해당 요일에 운동 항목이 하나라도 있으면 맵에 추가
            if (exercisesForDay.isNotEmpty()) {
                dailyRoutinesMap[day] = exercisesForDay
            }
        }
        return dailyRoutinesMap
    }


    /**
     * 회원 목록을 AlertDialog로 표시하고 선택된 이름으로 TextView를 업데이트합니다.
     */
    private fun showMemberSelectionDialog(textView: TextView, members: Array<String>) {
        AlertDialog.Builder(this)
            .setTitle("회원 목록 선택")
            .setItems(members) { dialog, which ->
                // 선택된 회원 이름으로 TextView 업데이트
                val selectedMember = members[which]
                textView.text = selectedMember
                Toast.makeText(this, "$selectedMember 선택 완료", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    /**
     * 다중 선택(Multi-Select)이 가능한 AlertDialog를 표시하고 결과를 해당 열(Column)에 분산하여 반영합니다.
     * @param title 다이얼로그의 제목
     * @param options 선택 가능한 항목들의 배열
     * @param columnTextViewIds 선택 결과를 분산하여 표시할 TextView들의 ID 리스트 (ex: 월요일 1~7행)
     */
    private fun showRoutineSelectorDialog(title: String, options: Array<String>, columnTextViewIds: List<Int>) {

        // 1. 현재 열(Column)의 모든 TextView에서 선택된 항목들을 취합하여 초기 상태 파악
        val currentlySelected = mutableListOf<String>()
        columnTextViewIds.forEach { id ->
            val tv = findViewById<TextView>(id)
            // '●\n' 또는 '□' 문자열을 제거하고 실제 운동 항목만 추출
            val itemText = tv.text.toString().replace("●\n", "").replace("□", "").trim()
            if (itemText.isNotEmpty()) {
                currentlySelected.add(itemText)
            }
        }

        // 2. 초기 선택 상태를 저장할 부울 배열 생성
        val checkedItems = BooleanArray(options.size) { i ->
            currentlySelected.contains(options[i])
        }

        // 최종 선택된 항목들을 임시로 저장할 리스트
        val selectedItemsList = mutableListOf<String>()

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMultiChoiceItems(options, checkedItems) { _, which, isChecked ->
                checkedItems[which] = isChecked
                Log.d("Selector", "$title 항목 변경: ${options[which]}, 선택 상태: $isChecked")
            }
            // "확인" 버튼: 선택된 모든 항목을 TextView 열에 분산하여 적용
            .setPositiveButton("확인") { dialog, _ ->
                // 선택된 항목 리스트 재구성
                for (i in options.indices) {
                    if (checkedItems[i]) {
                        selectedItemsList.add(options[i])
                    }
                }

                // 3. 선택된 항목들을 TextView 열에 분산 배치 및 업데이트
                val maxVisibleRows = columnTextViewIds.size // 현재는 7개

                for (i in columnTextViewIds.indices) {
                    val tv = findViewById<TextView>(columnTextViewIds[i])

                    if (i < selectedItemsList.size) {
                        // 선택된 항목이 있다면 해당 TextView에 배치
                        val item = selectedItemsList[i]
                        tv.text = "$item"
                        tv.textSize = 14f // 텍스트 크기 조정
                        Log.d("Update", "Row ${i + 1} updated with: $item")
                    } else {
                        // 남은 행은 '□'로 비움
                        tv.text = "□"
                        tv.textSize = 18f // 텍스트 크기 복구
                        Log.d("Update", "Row ${i + 1} cleared.")
                    }
                }

                // 선택된 항목이 열의 최대 개수(7개)를 초과할 경우 경고 메시지 표시
                if (selectedItemsList.size > maxVisibleRows) {
                    val excessItems = selectedItemsList.size - maxVisibleRows
                    //Toast.makeText(this, "선택 가능한 최대 항목 수(${maxVisibleRows}개)를 초과하여 $excessItems개 항목은 저장되지만 화면에 표시되지 않습니다.", Toast.LENGTH_LONG).show()
                }

                dialog.dismiss()
            }
            // "취소" 버튼: 변경 없이 다이얼로그 종료
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}