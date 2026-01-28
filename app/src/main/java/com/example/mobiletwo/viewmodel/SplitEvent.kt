package com.example.mobiletwo.viewmodel

sealed class SplitEvent {
    data class UpdateTotal(val total: String) : SplitEvent()
    data class UpdatePersons(val persons: String) : SplitEvent()
    data class UpdateTipPercent(val tipPercent: String) : SplitEvent()
    object Calculate : SplitEvent()
    object Reset : SplitEvent()
}
