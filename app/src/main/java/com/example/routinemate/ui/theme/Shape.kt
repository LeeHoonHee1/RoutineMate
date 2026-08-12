package com.example.routinemate.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val RoutineShapes = Shapes(

    // 작은 버튼이나 작은 UI 요소
    small = RoundedCornerShape(12.dp),

    // 일반 카드, 입력창
    medium = RoundedCornerShape(16.dp),

    // 주요 대시보드 카드
    large = RoundedCornerShape(20.dp)
)