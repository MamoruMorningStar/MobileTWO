package com.example.mobiletwo.viewmodel

import com.example.mobiletwo.data.Calculation

data class SplitUiState(
    val total: String = "",
    val persons: String = "",
    val tipPercent: String = "15",
    val calculations: List<Calculation> = emptyList(),
    val calculationIdForNavigation: String? = null
)
